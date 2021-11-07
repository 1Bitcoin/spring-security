package com.example.springmvc.controller

import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api")
class RestController @Autowired constructor(private val addressBookService: AddressBookService) {

    @PostMapping("/list")
    fun getNote(@RequestBody query: Map<String, String>): ResponseEntity<ConcurrentHashMap<Int, BookNote>> {
        val searchResult = addressBookService.getNoteByID(query)

        return ResponseEntity(searchResult, HttpStatus.OK)
    }

    @PostMapping("/add")
    fun addNote(@RequestBody bookNote: BookNote): ResponseEntity<BookNote> {
        addressBookService.addNote(bookNote)

        return ResponseEntity(bookNote, HttpStatus.CREATED)
    }

    @GetMapping("/{id}/view")
    fun getNoteByID(@PathVariable id: String): ResponseEntity<ConcurrentHashMap<Int, BookNote>> {
        val searchResult = addressBookService.getNoteByID(mapOf("id" to id))

        return ResponseEntity(searchResult, HttpStatus.OK)
    }

    @PutMapping("/{id}/edit")
    fun editNote(@PathVariable id: String, @RequestBody addressBook: BookNote): ResponseEntity<BookNote> {
        val tmp = addressBookService.editNote(id.toInt(), addressBook)

        return ResponseEntity(tmp, HttpStatus.OK)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteNote(@PathVariable id: String): ResponseEntity<BookNote> {
        return ResponseEntity(addressBookService.deleteNote(id.toInt()), HttpStatus.OK)
    }
}