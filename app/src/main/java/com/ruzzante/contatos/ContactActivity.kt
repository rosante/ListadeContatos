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
            deleteContact()
        }
        buttonSave.setOnClickListener {
            saveContact()
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
            textViewId.setText(index)

            Log.w("W/ContactActivity", "Contact found: ${contact.id.toString()} - ${contact.nome.toString()} - ${contact.telefone.toString()}")
        }catch(ex: Exception){
            Log.e("E/LAYOUT", ex.toString())
        }

    }
    private fun deleteContact(){
        try{
            index = intent.getIntExtra("index", -1)
            if (helperDB?.deleteContact(index) == true)
                Toast.makeText(this, "Contato removido", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Erro ao remover contato", Toast.LENGTH_SHORT).show()
            this.onBackPressed()
        }catch(ex: Exception){
            Log.e("E/CONTATO", ex.toString())
        }
    }
    private fun saveContact(){
        try{
            if (textViewTitleContact.text.toString() == "Editar Contato")
                helperDB?.updateContact(Contact(nome=editTextTextNameContact.text.toString(), telefone=editTextTextPhoneContact.text.toString(), id=parseInt(textViewId.text.toString())))
            else
                helperDB?.addContact(Contact(nome=editTextTextNameContact.text.toString(), telefone=editTextTextPhoneContact.text.toString()))
            this.onBackPressed()
        }catch(ex: Exception){
            Log.e("E/CONTATO", ex.toString())
        }
    }

}