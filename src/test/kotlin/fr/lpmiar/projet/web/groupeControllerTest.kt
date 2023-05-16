package fr.lpmiar.projet.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet
.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request
.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result
.MockMvcResultMatchers
import org.springframework.test.web.servlet.result
.MockMvcResultMatchers.jsonPath
import com.fasterxml.jackson.databind.ObjectMapper
import fr.lpmiar.projet.model.Groupe
import org.junit.jupiter.api.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class groupeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var mapper: ObjectMapper

    @Test
    @Order(1)
    fun emptyGroupe() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/groupe")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn()
    }
    @Test
    @Order(2)
    fun postGroupeOk() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/groupe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Groupe("1","LP MIAR")))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nom").value("LP MIAR"))
                .andReturn();
    }
}