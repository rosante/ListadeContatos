package com.ruzzante.contatos.singleton

import com.ruzzante.contatos.models.Contact

object ContactSingleton {
    var contactList:List<Contact> = mutableListOf()
}