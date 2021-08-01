package com.ruzzante.contatos

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruzzante.contatos.adapter.ContactAdapter
import com.ruzzante.contatos.helpers.HelperDB
import com.ruzzante.contatos.models.Contact
import com.ruzzante.contatos.singleton.ContactSingleton
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var helperDB: HelperDB? = null
        private set

    private lateinit var contactAdapter:ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helperDB = HelperDB(this)

        try{
            configureRecycleView()
        }catch(ex: Exception){
            Log.e("SQLITE", ex.toString())
        }

        editTextSearch.doAfterTextChanged {
            val contactList = ContactSingleton.contactList
            val filtered = contactList.filter{it.nome.lowercase().contains(editTextSearch.text.toString().lowercase())}
            updateRecyclerView(filtered)
        }
        buttonAdd.setOnClickListener(){
            val intent = Intent(this, ContactActivity::class.java)
            intent.putExtra("index", -1)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        ContactSingleton.contactList = helperDB?.getAllContacts() ?: mutableListOf()
        updateRecyclerView(ContactSingleton.contactList)
    }

    private fun updateRecyclerView(list:List<Contact>){
        if (list.isNullOrEmpty() && editTextSearch.text.isNullOrBlank()){
            try{
                ContactSingleton.contactList = helperDB?.getAllContacts() ?: mutableListOf()
                contactAdapter = ContactAdapter(ContactSingleton.contactList){onClickItemRecycleView(it)}
            }catch(ex: Exception){
                Log.e("E/SQLITE", ex.toString())
            }
        }else{
            contactAdapter = ContactAdapter(list){onClickItemRecycleView(it)}
        }
        val recyclerView:RecyclerView = findViewById(R.id.recyclerViewContacts)
        recyclerView.adapter = contactAdapter
        contactAdapter.notifyDataSetChanged()
    }
    private fun configureRecycleView(){
        ContactSingleton.contactList = helperDB?.getAllContacts() ?: mutableListOf()
        val recyclerView:RecyclerView = findViewById(R.id.recyclerViewContacts)
        contactAdapter = ContactAdapter(ContactSingleton.contactList){onClickItemRecycleView(it)}
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = contactAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        contactAdapter.notifyDataSetChanged()
    }
    private fun onClickItemRecycleView(index:Int){
        val intent = Intent(this, ContactActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }


}