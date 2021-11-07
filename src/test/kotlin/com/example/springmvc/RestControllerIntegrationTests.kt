package com.example.springmvc


import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.concurrent.ConcurrentHashMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestControllerIntegrationTests {

    private val headers = HttpHeaders()

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var addressBookService: AddressBookService

    @BeforeEach
    fun setUp(){
        headers.add("Cookie", logging())

        addressBookService.addNote(BookNote("Michail", "Ivanov", "Lenina Street", "+78756782233"))
        addressBookService.addNote(BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none"))
        addressBookService.addNote(BookNote("Artem", "Afimin", "Amyrskaya Street", "+228"))
    }

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    private fun logging(): String? {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap()
        request.set("username", "admin")
        request.set("password", "password")

        val response = restTemplate.postForEntity(url("login"), HttpEntity(request, HttpHeaders()), String::class.java)

        return response!!.headers["Set-Cookie"]!![0]
    }

    @ParameterizedTest
    @MethodSource("different book note")
    fun `should correct add notes`(bookNote: BookNote) {
        val response = restTemplate.exchange(
            url("api/add"),
            HttpMethod.POST,
            HttpEntity(bookNote, headers),
            BookNote::class.java
        )

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(bookNote.name, response.body!!.name)
    }

    @ParameterizedTest
    @MethodSource("different id")
    fun `should correct get notes`(id: Int) {
        val response = restTemplate.exchange(
            url("api/$id/view"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @ParameterizedTest
    @MethodSource("different id")
    fun `should return list of notes with conditional`(id: Int) {
        val response = restTemplate.exchange(
            url("api/list"),
            HttpMethod.POST,
            HttpEntity(mapOf("id" to id.toString()), headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `should return list`() {
        val response = restTemplate.exchange(
            url("api/list"),
            HttpMethod.POST,
            HttpEntity(emptyMap<String, String>(), headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @ParameterizedTest
    @MethodSource("different id")
    fun `should delete notes with id`(id: Int) {
        val response = restTemplate.exchange(
            url("api/$id/delete"),
            HttpMethod.DELETE,
            HttpEntity(emptyMap<String, String>(), headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @ParameterizedTest
    @MethodSource("id for edit and new book model")
    fun `should edit notes with id`(id: Int, bookNote: BookNote) {
        val response = restTemplate.exchange(
            url("api/$id/edit"),
            HttpMethod.PUT,
            HttpEntity(bookNote, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    companion object {
        @JvmStatic
        fun `different book note`() = listOf(
            BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none"),
            BookNote("Ivan", "Zhigalkin", "green Street", "72022"),
            BookNote("Maxom", "Kozlov", "Porty Street", "71018")
        )

        @JvmStatic
        fun `different id`() = listOf(
            2, 0, 1
        )

        @JvmStatic
        fun `id for edit and new book model`() = listOf(
            Arguments.of(1, BookNote("Maxom", "Kozlov", "Porty Street", "71018")),
            Arguments.of(0, BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none")),
            Arguments.of(2, BookNote("Maxom", "Kozlov", "Porty Street", "12"))
        )
    }
}