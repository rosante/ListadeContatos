package com.ruzzante.contatos.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.ruzzante.contatos.models.Contact
import com.ruzzante.contatos.singleton.ContactSingleton

class HelperDB (
    context: Context
    ): SQLiteOpenHelper (context, DATABASE_NAME, null, VERSION) {
    companion object{
        private val DATABASE_NAME = "contato.db"
        private val VERSION = 1
    }

    //Useful db names to use along the class
    val TABLE_NAME = "contato"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, telefone TEXT NOT NULL)"

    //Used to Create Table When it not Exists
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    //Used to apply your logic when you want to upgrade your Database. Just Change VERSION above and implement method below with your database changes
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Não implementado
        onCreate(db)
    }

    fun getAllContacts():List<Contact>{
        try{
            val db = readableDatabase ?: return mutableListOf()
            val contactList = mutableListOf<Contact>()
            val sql = "Select * from $TABLE_NAME"
            var cursor = db.rawQuery(sql, null)
            while(cursor.moveToNext()){
                var contact = Contact(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getString(cursor.getColumnIndex("telefone"))
                )
                contactList.add(contact)
            }
            db.close()
            return contactList
        }catch (ex:Exception){
            Log.e("HelperDb/getAllContacts", ex.toString())
        }
        return listOf<Contact>()
    }

    fun getContactById(id:Int):Contact{
        try{
            val db = readableDatabase ?: return Contact(-1, "Não encontrado", "Não encontrado")
            var contact:Contact
            val sql = "Select * from $TABLE_NAME where id =?"
            var cursor = db.rawQuery(sql, arrayOf(id.toString()))
            contact = Contact(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("nome")),
                cursor.getString(cursor.getColumnIndex("telefone"))
            )
            db.close()
            return contact
        }catch(ex:Exception){
            Log.e("HelperDb/getContactById", ex.toString())
        }
        return Contact(-1, "Não encontrado", "Não encontrado")
    }

    fun getFilteredContacts(textSearch:String):List<Contact>{
        try{
            val db = readableDatabase ?: return mutableListOf()
            val contactList = mutableListOf<Contact>()
            val sql = "Select * from $TABLE_NAME where nome like ?"
            var cursor = db.rawQuery(sql, arrayOf(textSearch))
            while(cursor.moveToNext()) {
                var contact = Contact(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getString(cursor.getColumnIndex("telefone"))
                )
                contactList.add(contact)
            }
            db.close()
            return contactList
        }catch(ex: Exception){
            Log.e("HelperDb/getFilteredCon", ex.toString())
        }
        return listOf<Contact>()
    }

    fun deleteContact(index:Int):Boolean{
        try{
            val db = writableDatabase ?: return false
            val sql = "delete from $TABLE_NAME where id=?"
            db.execSQL(sql, arrayOf(index.toString()))
            db.close()
            return true
        }catch(ex: Exception){
            Log.e("HelperDB/deleteContact", ex.toString())
            return false
        }
        return false
    }

    fun addContact(contact:Contact):Boolean{
        try{
            val db = writableDatabase ?: return false
            //Passing values as Arguments in order to avoid SQL Injections, as explained in the Bootcamp
            val sql = "INSERT INTO $TABLE_NAME (nome, telefone) VALUES (?,?)"
            val arguments = arrayOf(contact.nome, contact.telefone)
            db.execSQL(sql, arguments)
            db.close()
            return true
        }catch(ex: Exception){
            Log.e("HelperDB/addContact", ex.toString())
        }
        return false
        /*
        Another example of How to insert could be:
        var content = ContentValues()
        content.put("nome", contact.nome)
        content.put("telefone", contact.telefone)
        db.insert(tableName, null,  content)
         */
    }

    fun updateContact(contact:Contact):Boolean{
        try{
            val db = readableDatabase ?: return false
            val sql = "Update $TABLE_NAME set nome=?, telefone=? where id=?"
            val arguments = arrayOf(contact.nome, contact.telefone, contact.id)
            db.execSQL(sql, arguments)
            db.close()
            return true
        }catch(ex: Exception){
               Log.e("HelperDB/updateContact", ex.toString())
        }
        return false
    }
}