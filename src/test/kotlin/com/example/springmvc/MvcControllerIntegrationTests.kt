package com.example.springmvc


import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@SpringBootTest
@AutoConfigureMockMvc
class MvcControllerIntegrationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressBookService: AddressBookService

    @BeforeEach
    fun setUp() {
        addressBookService.addNote(BookNote("Michail", "Ivanov", "Lenina Street", "+78756782233"))
        addressBookService.addNote(BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none"))
        addressBookService.addNote(BookNote("Artem", "Afimin", "Amyrskaya Street", "+228"))
    }

    @Test
    fun `should correct add note`() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "Dmitry")
        note.add("surname", "Zhigalkin")
        note.add("address", "Lenina Street")
        note.add("telephone", "none")

        mockMvc.perform(post("/app/add")
            .params(note))
            .andExpect(status().isOk)
            .andExpect(view().name("responseMessagePage"))
            .andExpect(content().string(containsString("Note was added")))
    }

    @Test
    fun `should return list notes`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("listNotesPage"))
            .andExpect(content().string(containsString("Search result:")))
    }

    @Test
    fun `should return note with id = 2 in query `() {
        mockMvc.perform(get("/app/list?id=0"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("listNotesPage"))
            .andExpect(content().string(containsString("Search result:")))
    }

    @Test
    fun `should edit note with id = 0`() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "Roman")
        note.add("surname", "Fedorov")
        note.add("address", "Red Street")
        note.add("telephone", "1337")

        mockMvc.perform(post("/app/0/edit")
            .params(note))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("responseMessagePage"))
            .andExpect(content().string(containsString("Note was edited")))
    }

    @ParameterizedTest
    @MethodSource("different id")
    fun `should delete note with different id`() {
        mockMvc.perform(get("/app/2/delete"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("responseMessagePage"))
            .andExpect(content().string(containsString("Note was deleted")))
    }

    @ParameterizedTest
    @MethodSource("different id")
    fun `should return note with different id`() {
        mockMvc.perform(get("/app/0/view"))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun `should return menuPage`() {
        mockMvc.perform(get("/menuPage.html"))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun `should return addNotePage`() {
        mockMvc.perform(get("/app/add"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("addNotePage"))
    }

    companion object {
        @JvmStatic
        fun `different id`() = listOf(
            2, 0, 1
        )
    }
}