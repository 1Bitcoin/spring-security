package com.example.springmvc


import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityMvcTests {

    @Autowired
    private val context: WebApplicationContext? = null
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var addressBookService: AddressBookService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        addressBookService.addNote(BookNote("Michail", "Ivanov", "Lenina Street", "+78756782233"))
        addressBookService.addNote(BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none"))
        addressBookService.addNote(BookNote("Artem", "Afimin", "Amyrskaya Street", "+228"))
    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["APP_ROLE"])
    @Test
    fun `should succesfull return page for user with authorities = APP_ROLE`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk())

    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["ADMIN"])
    @Test
    fun `should succesfull return page for user with authorities = ADMIN`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk())

        mockMvc.perform(get("/api/list"))
            .andDo(print())
            .andExpect(status().is4xxClientError())

    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["APP_ROLE"])
    @Test
    fun `should return 403 error for user with authorities = APP_ROLE`() {
        mockMvc.perform(get("/api/list"))
            .andDo(print())
            .andExpect(status().is4xxClientError)

    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["API_ROLE"])
    @Test
    fun `should return 403 error for user with authorities = API_ROLE`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().is4xxClientError)

    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["APP_ROLE"])
    @Test
    fun `should correct add note with authorities = APP_ROLE`() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "Dmitry")
        note.add("surname", "Zhigalkin")
        note.add("address", "Lenina Street")
        note.add("telephone", "none")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
            .params(note))
            .andExpect(status().isOk)
            .andExpect(view().name("responseMessagePage"))
            .andExpect(content().string(StringContains.containsString("Note was added")))
    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["APP_ROLE"])
    @Test
    fun `should return list notes`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("listNotesPage"))
            .andExpect(content().string(StringContains.containsString("Search result:")))
    }

    @WithMockUser(username = "alex", password = "alex", authorities = ["APP_ROLE"])
    @ParameterizedTest
    @MethodSource("different id")
    fun `should delete note with different id`(id: Int) {
        mockMvc.perform(get("/app/$id/delete"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("responseMessagePage"))
            .andExpect(content().string(StringContains.containsString("Note was deleted")))
    }

    companion object {
        @JvmStatic
        fun `different id`() = listOf(
            2, 0, 1
        )
    }
}