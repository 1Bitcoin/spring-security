package com.example.springmvc.service

import com.example.springmvc.DAO.BookNote
import com.example.springmvc.repository.AddressBookRepository
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class AddressBookService {
    private val addressBookRepository = AddressBookRepository(ConcurrentHashMap())

    fun addNote(bookNote: BookNote) = addressBookRepository.saveNote(bookNote)

    fun getNoteByID(query: Map<String, String>): ConcurrentHashMap<Int, BookNote> {
        if (query.isEmpty())
            return getAllNotes()
        else
            return addressBookRepository.getNoteByID(query)
    }

    fun getAllNotes(): ConcurrentHashMap<Int, BookNote> = addressBookRepository.getAllNotes()

    fun editNote(id: Int, bookNote: BookNote): BookNote? = addressBookRepository.editNote(id, bookNote)

    fun deleteNote(id: Int): BookNote? = addressBookRepository.deleteNote(id)
}