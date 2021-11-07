package com.example.springmvc.controller

import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/app")
class MvcController @Autowired constructor(val addressBookService: AddressBookService) {

    @GetMapping("/add")
    fun getNoteAddForm(): String{
        return "addNotePage"
    }

    @GetMapping("/{id}/edit")
    fun getNoteEditPage(@PathVariable id: String): String{
        return "editNotePage"
    }

    @GetMapping("/list")
    fun getNote(@RequestParam query: Map<String, String>, model: Model): String {
        val searchResult = addressBookService.getNoteByID(query)

        model.addAttribute("result", searchResult)
        model.addAttribute("msg", "Search result:")

        return "listNotesPage"
    }

    @GetMapping("/{id}/view")
    fun getNoteByID(@PathVariable id: String, model: Model): String {
        val searchResult = addressBookService.getNoteByID(mapOf("id" to id))
        model.addAttribute("result", searchResult)

        return "viewNotePage"
    }

    @GetMapping("/{id}/delete")
    fun deleteNote(@PathVariable id: String, model: Model): String {
        addressBookService.deleteNote(id.toInt())
        model.addAttribute("result", "Note was deleted")

        return "responseMessagePage"
    }

    @PostMapping("/add")
    fun addNote(@ModelAttribute bookNote: BookNote, model: Model): String {
        addressBookService.addNote(bookNote)
        model.addAttribute("result", "Note was added")

        return "responseMessagePage"
    }

    @PostMapping("/{id}/edit")
    fun editNote(@PathVariable id: String, @ModelAttribute bookNote: BookNote, model: Model): String {
        addressBookService.editNote(id.toInt(), bookNote)
        model.addAttribute("result", "Note was edited")

        return "responseMessagePage"
    }
}