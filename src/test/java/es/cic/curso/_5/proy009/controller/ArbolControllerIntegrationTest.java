package es.cic.curso._5.proy009.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso._5.proy009.model.Arbol;
import es.cic.curso._5.proy009.repository.ArbolRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ArbolControllerIntegrationTest")
public class ArbolControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArbolRepository arbolRepository;

    @Test
    @DisplayName("POST /arboles crea arbol y devuelve JSON con ID")
    public void shouldCreateArbol() throws Exception {
        Arbol input = new Arbol(
            "NombreTest", "EspecieTest", LocalDate.of(2025, 1, 1)
        );
        String jsonIn = objectMapper.writeValueAsString(input);

        MvcResult res = mockMvc.perform(post("/arboles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.nombre").value("NombreTest"))
            .andExpect(jsonPath("$.especie").value("EspecieTest"))
            .andReturn();

        Arbol created = objectMapper.readValue(
            res.getResponse().getContentAsString(),
                            Arbol.class
        );
        assertTrue(arbolRepository.existsById(created.getId()), "El árbol debería persistirse en la BD");
    }

    @Test
    @DisplayName("GET /arboles/{id} devuelve 200 o 404 según existencia")
    public void shouldReturnArbolOrNotFound() throws Exception {
        Arbol saved = arbolRepository.save(
            new Arbol(
                "Exist", "EspecieExist", LocalDate.of(2025, 1, 1)
            )
        );

        mockMvc.perform(get("/arboles/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(saved.getId()))
            .andExpect(jsonPath("$.nombre").value("Exist"))
            .andExpect(jsonPath("$.especie").value("EspecieExist"));

        long missingId = saved.getId() + 999;
        String expectedMessage = "Arbol con ID " + missingId + " no encontrado.";

        mockMvc.perform(get("/arboles/{id}", missingId))
            .andExpect(status().isNotFound())
            .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("GET /arboles devuelve lista completa")
    public void shouldGetAllArboles() throws Exception {
        arbolRepository.deleteAll();
        Arbol a1 = arbolRepository.save(
            new Arbol(
                "A", "EspecieA", LocalDate.of(2025, 1, 1)
            )
        );
        Arbol a2 = arbolRepository.save(
            new Arbol(
                "B", "EspecieB", LocalDate.of(2025, 1, 1)
            )
        );

        mockMvc.perform(get("/arboles")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0].id").value(a1.getId()))
            .andExpect(jsonPath("$.[1].id").value(a2.getId()));   
    }

     @Test
     @DisplayName("PUT /arboles/{id} actualiza y devuelve JSON")
     public void shouldUpdateArbol() throws Exception {
        Arbol original = arbolRepository.save(
            new Arbol("Original", "EspecieOriginal", LocalDate.of(2025, 1, 1))
        );
        original.setNombre("NuevoNombre");
        String jsonIn = objectMapper.writeValueAsString(original);

        mockMvc.perform(put("/arboles/{id}", original.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("NuevoNombre"));
        
        Arbol updated = arbolRepository.findById(original.getId()).orElseThrow();
        assertTrue(updated.getNombre().equals("NuevoNombre"), "El cambio debe reflejarse en la BD");
     }

     @Test
     @DisplayName("DELETE /arboles/{id} elimina el árbol")
     public void shouldDeleteArbol() throws Exception {
        Arbol toDelete = arbolRepository.save(
            new Arbol("Delete", "EspecieDelete", LocalDate.of(2015, 1, 1))  
        );

        mockMvc.perform(delete("/arboles/{id}", toDelete.getId()))
                .andExpect(status().isOk());
        
        assertFalse(arbolRepository.existsById(toDelete.getId()),
                    "El árbol debería haber sido");
     }
}
