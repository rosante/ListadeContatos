package com.ruzzante.contatos

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
        configureRecycleView()

        editTextSearch.doAfterTextChanged {
            val contactList = ContactSingleton.contactList
            val filtered = contactList.filter{it.nome.lowercase().contains(editTextSearch.text.toString().lowercase())}
            showInRecyclerView(filtered)
        }
        buttonAdd.setOnClickListener(){
            val intent = Intent(this, ContactActivity::class.java)
            intent.putExtra("index", -1)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        progressBarMain.visibility = View.VISIBLE
        updateRecyclerView()
    }
    private fun showInRecyclerView(list:List<Contact>){
        contactAdapter = ContactAdapter(list){onClickItemRecycleView(it)}
        recyclerViewContacts.adapter = contactAdapter
    }
    private fun updateRecyclerView(){
        progressBarMain.visibility = View.VISIBLE
        Thread(Runnable {
            try{
                ContactSingleton.contactList = helperDB?.getAllContacts() ?: mutableListOf()
                contactAdapter = ContactAdapter(ContactSingleton.contactList){onClickItemRecycleView(it)}
            }catch(ex: Exception){
                Log.e("MainActivity/updateRecy", ex.toString())
            }
            runOnUiThread {
                recyclerViewContacts.adapter = contactAdapter
                progressBarMain.visibility = View.INVISIBLE
            }
        }).start()
    }
    private fun configureRecycleView(){
        contactAdapter = ContactAdapter(ContactSingleton.contactList){onClickItemRecycleView(it)}
        recyclerViewContacts.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewContacts.adapter = contactAdapter
        recyclerViewContacts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
    private fun onClickItemRecycleView(index:Int){
        val intent = Intent(this, ContactActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }


}