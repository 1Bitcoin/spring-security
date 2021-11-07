package com.example.springmvc


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print


@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityTests {

    @Autowired
    private val context: WebApplicationContext? = null
    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @WithUserDetails()
    @Test
    fun `should succesfull return page`() {
        val alex = user("alex").password("alex").
        mockMvc.perform(get("/app/list").with(alex))
            .andDo(print())
            .andExpect(status().isOk())

    }
}