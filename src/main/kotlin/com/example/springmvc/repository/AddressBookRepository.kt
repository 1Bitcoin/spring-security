package com.example.springmvc.repository

import com.example.springmvc.DAO.BookNote
import org.springframework.context.annotation.Bean
import java.util.concurrent.ConcurrentHashMap

class AddressBookRepository(private val addressBook: ConcurrentHashMap<Int, BookNote>) {

    fun saveNote(addressBookModel: BookNote) {
        addressBook.put(addressBook.size, addressBookModel)
    }

    fun getAllNotes(): ConcurrentHashMap<Int, BookNote> {
        return if (addressBook.isEmpty())
            ConcurrentHashMap()
        else
            addressBook
    }
    fun editNote(id: Int, addressBookNew: BookNote): BookNote? {
        if (addressBookNew.name.isNotEmpty())
            addressBook[id]!!.name = addressBookNew.name

        if (addressBookNew.surname.isNotEmpty())
            addressBook[id]!!.surname = addressBookNew.surname

        if (addressBookNew.address.isNotEmpty())
            addressBook[id]!!.address = addressBookNew.address

        if (addressBookNew.telephone.isNotEmpty())
            addressBook[id]!!.telephone = addressBookNew.telephone

        return addressBook[id]
    }

    fun deleteNote(id: Int): BookNote? {
        val note = addressBook[id]
        addressBook.remove(id)

        return note
    }

    fun getNoteByID(query: Map<String, String>): ConcurrentHashMap<Int, BookNote>{
        val resultSearch = ConcurrentHashMap<Int, BookNote>()
        val id = query["id"]!!.toInt()

        addressBook[id]?.let { resultSearch.put(id, it) }

        return resultSearch
    }
}