package com.example.springmvc


import com.example.springmvc.DAO.BookNote
import com.example.springmvc.service.AddressBookService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityRestTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private val context: WebApplicationContext? = null

    @Autowired
    private lateinit var addressBookService: AddressBookService

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @BeforeEach
    fun setup() {

        addressBookService.addNote(BookNote("Michail", "Ivanov", "Lenina Street", "+78756782233"))
        addressBookService.addNote(BookNote("Dmitry", "Zhigalkin", "Lenina Street", "none"))
        addressBookService.addNote(BookNote("Artem", "Afimin", "Amyrskaya Street", "+228"))
    }

    @Test
    fun `should return list`() {
        val result: ResponseEntity<String> = restTemplate.withBasicAuth("john", "john")
            .getForEntity("/api/list", String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        Assertions.assertNotNull(result.body)
    }

    @Test
    fun `should return book with id = 0`() {
        val result: ResponseEntity<String> = restTemplate.withBasicAuth("john", "john")
            .getForEntity("/api/0/view", String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        Assertions.assertNotNull(result.body)
    }

//    @Test
//    fun `should return list1`() {
//        logging()
//        val bookNote = BookNote("Maxom", "Kozlov", "Porty Street", "71018")
//
//        val response = restTemplate.exchange(
//            url("api/add"),
//            HttpMethod.POST,
//            HttpEntity(bookNote),
//            BookNote::class.java
//        )
//
//        assertEquals(HttpStatus.CREATED, response.statusCode)
//    }

//    @Test
//    fun `should return list1`() {
//        val a = BookNote("Maxom", "Kozlov", "Porty Street", "71018")
//
//        val result: ResponseEntity<String> = restTemplate.withBasicAuth("john", "john")
//            .postForEntity("/api/add", HttpEntity(a), String::class.java)
//
//        assertEquals(HttpStatus.CREATED, result.statusCode)
//    }

//    @ParameterizedTest
//    @MethodSource("different book note")
//    fun `should correct add notes`(bookNote: BookNote) {
//        val response = restTemplate.exchange(
//            url("api/add"),
//            HttpMethod.POST,
//            HttpEntity(bookNote, headers),
//            BookNote::class.java
//        )
//
//        assertEquals(HttpStatus.CREATED, response.statusCode)
//        Assertions.assertNotNull(response.body)
//        assertEquals(bookNote.name, response.body!!.name)
//    }

}