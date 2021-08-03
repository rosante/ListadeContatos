package com.ruzzante.contatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import com.ruzzante.contatos.MainActivity
import com.ruzzante.contatos.helpers.HelperDB
import com.ruzzante.contatos.models.Contact
import com.ruzzante.contatos.singleton.ContactSingleton
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recyclerview_row.*
import java.lang.Exception
import java.lang.Integer.parseInt

class ContactActivity : AppCompatActivity() {

    private var index : Int = -1
    var helperDB: HelperDB? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        configLayout()
        helperDB = HelperDB(this)

        buttonDelete.setOnClickListener {
            progressBarContact.visibility = View.VISIBLE
            Thread(Runnable {
                val result = deleteContact()
                runOnUiThread{
                    if (result)
                        Toast.makeText(this, "Contato removido", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, "Erro, contato não removido", Toast.LENGTH_SHORT).show()
                    progressBarContact.visibility = View.INVISIBLE
                    this.onBackPressed()
                }
            }).start()
        }
        buttonSave.setOnClickListener {
            progressBarContact.visibility = View.VISIBLE
            index = intent.getIntExtra("index", -1)
            val contact = Contact(nome=editTextTextNameContact.text.toString(), telefone=editTextTextPhoneContact.text.toString(), id=index)
            Thread(Runnable {
                val result = saveContact(contact)
                val message:String
                if (result)
                    message = "Contato Adicionado"
                else
                    message = "Erro. Contato não adicionado"
                runOnUiThread{
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    progressBarContact.visibility = View.INVISIBLE
                    this.onBackPressed()
                }
            }).start()
        }

    }

    private fun configLayout(){
        try{
            buttonDelete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);
            buttonSave.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_update_contact, 0, 0, 0);
            index = intent.getIntExtra("index", -1)
            if (index == -1){
                textViewTitleContact.setText("Novo Contato")
                buttonDelete.visibility = View.GONE
                return
            }
            textViewTitleContact.setText("Editar Contato")
            val contact = ContactSingleton.contactList.find { it.id == index } ?: Contact(-1, "Não encontrado","Não encontrado.")
            textViewId.text = index.toString()
            editTextTextNameContact.setText(contact.nome)
            editTextTextPhoneContact.setText(contact.telefone)
        }catch(ex: Exception){
            Log.e("E/LAYOUT", ex.toString())
        }

    }
    private fun deleteContact():Boolean{
        try{
            index = intent.getIntExtra("index", -1)
            helperDB?.deleteContact(index)
            return true
        }catch(ex: Exception){
            Log.e("ContactActivity/deleteC", ex.toString())
        }
        return false
    }
    private fun saveContact(contact:Contact):Boolean{
        try{
            if (contact.id == -1)
                helperDB?.addContact(contact)
            else
                helperDB?.updateContact(contact)
            return true
        }catch(ex: Exception){
            Log.e("ContactActivity/saveCon", ex.toString())
        }
        return false
    }

}