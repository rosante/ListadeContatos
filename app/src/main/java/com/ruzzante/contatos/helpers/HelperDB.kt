package com.ruzzante.contatos.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.ruzzante.contatos.models.Contact
import com.ruzzante.contatos.singleton.ContactSingleton

class HelperDB (
    context: Context
    ): SQLiteOpenHelper (context, NOME_BANCO, null, VERSAO_ATUAL) {
    companion object{
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 1
    }


    val tableName = "contato"
    val createTable = "CREATE TABLE $tableName (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, telefone TEXT NOT NULL)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Não implementado
        onCreate(db)
    }

    fun getAllContacts():List<Contact>{
        Log.w("W/HelperDb", "getAllContacts - Entered")
        val db = readableDatabase ?: return mutableListOf()
        val contactList = mutableListOf<Contact>()
        val sql = "Select * from $tableName"
        var cursor = db.rawQuery(sql, null)
        while(cursor.moveToNext()){
            var contact = Contact(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("nome")),
                cursor.getString(cursor.getColumnIndex("telefone"))
            )
            contactList.add(contact)
        }
        Log.w("W/HelperDb", "Got ${contactList.count()} contacts")
        return contactList
    }

    fun getContactById(id:Int):Contact{
        Log.w("W/HelperDb", "getContactById - Entered with id: $id")
        val db = readableDatabase ?: return Contact(-1, "Não encontrado", "Não encontrado")
        var contact:Contact
        val sql = "Select * from $tableName where id ='$id'"
        Log.w("W/HelperDb", "getContactById query: $sql")
        var cursor = db.rawQuery(sql, null)
        contact = Contact(
            cursor.getInt(cursor.getColumnIndex("id")),
            cursor.getString(cursor.getColumnIndex("nome")),
            cursor.getString(cursor.getColumnIndex("telefone"))
        )
        Log.w("W/HelperDb", "getContactById got: ${contact.nome.toString()}")
        return contact
    }

    fun getFilteredContacts(texto:String):List<Contact>{
        Log.w("W/HelperDb", "getFilteredContacts - Entered")
        val db = readableDatabase ?: return mutableListOf()
        val contactList = mutableListOf<Contact>()
        val sql = "Select * from $tableName where nome like '%$texto%'"
        var cursor = db.rawQuery(sql, null)
        while(cursor.moveToNext()){
            var contact = Contact(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("nome")),
                cursor.getString(cursor.getColumnIndex("telefone"))
            )
            contactList.add(contact)
        }
        return contactList
    }

    fun deleteContact(index:Int):Boolean{
        Log.w("W/HelperDb", "deleteContact - Entered with id: $index")
        val db = readableDatabase ?: return false
        val sql = "delete from $tableName where id='$index'"
        Log.w("W/HelperDb", "deleteContact - query: $sql")
        return db.rawQuery(sql, null).count > 0
    }

    fun addContact(contact:Contact):Boolean{
        Log.w("W/HelperDb", "addContact - Entered with: ${contact.nome.toString()}")
        val db = readableDatabase ?: return false
        val sql = "Insert into ${tableName} (nome, telefone) values (\"${contact.nome}\", \"${contact.telefone}\")"
        Log.w("W/HelperDb", "addContact - query: $sql")
        var cursor = db.rawQuery(sql, null)
        ContactSingleton.contactList = getAllContacts()
        if (cursor.count > 0)
            return true
        return false
    }

    fun updateContact(contact:Contact):Boolean{
        Log.w("W/HelperDb", "updateContact - Entered with: ${contact.nome.toString()}")
        val db = readableDatabase ?: return false
        val sql = "Update ${tableName} set nome='${contact.nome}', telefone='${contact.telefone}' where id='${contact.id}'"
        Log.w("W/HelperDb", "updateContact - query: $sql")
        var cursor = db.rawQuery(sql, null)
        ContactSingleton.contactList = getAllContacts()
        if (cursor.count > 0)
            return true
        return false
    }
}