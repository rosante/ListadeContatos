package com.ruzzante.contatos.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.ruzzante.contatos.R
import com.ruzzante.contatos.models.Contact
import kotlinx.android.synthetic.main.recyclerview_row.view.*

internal class ContactAdapter(private val list: List<Contact>, private val onClick:((Int) -> Unit)) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewNome: TextView = itemView.findViewById(R.id.textViewNome)
        val textViewTelefone: TextView = itemView.findViewById(R.id.textViewTelefone)
        val llItem: LinearLayout = itemView.findViewById(R.id.llItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var item = list[position]
        holder.textViewNome.text = " ${item.nome}"
        holder.textViewTelefone.text = " ${item.telefone}"
        holder.textViewNome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact, 0, 0, 0);
        holder.textViewTelefone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);

        holder.llItem.setOnClickListener { onClick(item.id) }
    }

    override fun getItemCount() = list.size
}



