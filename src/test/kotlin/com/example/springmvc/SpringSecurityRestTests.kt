package com.example.springmvc


import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import java.awt.print.Book
import java.util.concurrent.ConcurrentHashMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityRestTests(@LocalServerPort var port: Int) {

    private val headers = HttpHeaders()

    @Autowired
    private lateinit var addressBookService: AddressBookService

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val fullURI = "http://localhost:$port/"

    private fun getCookie(
        username: String,
        password: String,
        loginURI: String = "$fullURI/login"
    ): String {

        val form = LinkedMultiValueMap<String, String>()
        form.set("username", username)
        form.set("password", password)

        val loginResponse = restTemplate
            .postForEntity(
                loginURI,
                HttpEntity(form, HttpHeaders()),
                String::class.java
            )

        return loginResponse.headers["Set-Cookie"]!![0]
    }

    @BeforeEach
    fun setup() {
        val cookie = getCookie("john", "john")
        headers.add("Cookie", cookie)

        addressBookService.addNote(BookNote("Michail", "Ivanov", "Lenina Street", "+78756782233"))
        addressBookService.addNote(BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none"))
        addressBookService.addNote(BookNote("Artem", "Afimin", "Amyrskaya Street", "+228"))
    }

    @ParameterizedTest
    @MethodSource("different notes")
    fun `should add address with authorities = API_ROLE`(note: BookNote) {
        val URL = "$fullURI/api/add"

        val response = restTemplate
            .exchange(
                URL,
                HttpMethod.POST,
                HttpEntity(note, headers),
                BookNote::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertTrue(response.body!! == note)
    }

    @Test
    fun `should get list of addresses`() {
        val map = emptyMap<String, String>()

        val URL = "$fullURI/api/list"

        val response = restTemplate.exchange(
            URL, HttpMethod.POST,
            HttpEntity(map, headers),
            Map::class.java)

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body)
    }

    @ParameterizedTest
    @MethodSource("different notes")
    fun `should update address`(note: BookNote) {
        val URL = "$fullURI/api/0/edit"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.PUT,
            HttpEntity(note, headers),
            BookNote::class.java, 1)

        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(note.name, response.body!!.name)
        assertEquals(note.surname, response.body!!.surname)
        assertEquals(note.address, response.body!!.address)
        assertEquals(note.telephone, response.body!!.telephone)
    }

    @ParameterizedTest
    @MethodSource("different id")
    fun `should delete address`(id: Int) {
        val URL = "$fullURI/api/$id/delete"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.DELETE,
            HttpEntity(null, headers),
            BookNote::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `should return book with id = 0`() {
        val URL = "$fullURI/api/0/view"

        val response = restTemplate.exchange(
            URL,
            HttpMethod.GET,
            HttpEntity(null, headers),
            ConcurrentHashMap::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    companion object {
        @JvmStatic
        fun `different notes`() = listOf(
            BookNote("Ivan", "Ivanov", "Lenina Street", "+1"),
            BookNote("Dmitry", "Petrov", "Omskaya Street", "+72022"),
            BookNote("artem", "nayd", "1", "228")
        )
        @JvmStatic
        fun `different id`() = listOf(
            2, 0, 1
        )
    }
}