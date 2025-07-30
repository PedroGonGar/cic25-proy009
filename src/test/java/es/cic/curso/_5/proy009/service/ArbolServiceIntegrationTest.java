package es.cic.curso._5.proy009.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.cic.curso._5.proy009.exception.ArbolException;
import es.cic.curso._5.proy009.model.Arbol;
import es.cic.curso._5.proy009.repository.ArbolRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@DisplayName("ArbolServiceIntegrationTest")
public class ArbolServiceIntegrationTest {
    @Autowired
    private ArbolService arbolService;

    @Autowired
    private ArbolRepository arbolRepository;

    @Test
    @DisplayName("Persiste un árbol nuevo y asigna ID")
    void shouldCreateArbol() {
        Arbol input = new Arbol(
            "ArbolCreate", "EspecieCreate", LocalDate.of(2025, 1, 1)
        );

        Arbol result = arbolService.create(input);

        assertNotNull(result.getId(), "El ID no debe ser null");
        assertTrue(arbolRepository.existsById(result.getId()),
                            "El árbol debe existir en la BD");      
    }

    @Test
    @DisplayName("get(id) devuelve árbol existente")
    void shouldGetExisting() {
        Arbol saved = arbolRepository.save(
            new Arbol("Existing", "EspecieExisting", LocalDate.of(2025, 1, 1))
        );

        Arbol found = arbolService.get(saved.getId());

        assertEquals("Existing", found.getNombre(), "El nombre debe coincidir");
        assertEquals("EspecieExisting", found.getEspecie(), "La especie debe coincidir");
    }

    @Test
    @DisplayName("get(id) lanza excepción cuando no existe")
    void shouldThrowWhenNotFound() {
        long nonExistentId = 999L;
        assertThrows(ArbolException.class,
        () -> arbolService.get(nonExistentId),
        "Debe lanzar ArbolException para ID inexistente");
    }

    @Test
    @DisplayName("get() devuelve todos los árboles")
    void shouldGetAll() {
        arbolRepository.deleteAll();
        arbolRepository.save(
            new Arbol(
                "A",
                "Especie A",
                LocalDate.of(2025, 1, 1)
            )
        );
        arbolRepository.save(
            new Arbol(
                "B",
                "EspecieB",
                LocalDate.of(2025, 2, 3)
            )
        );

        List<Arbol> list = arbolService.get();
        assertEquals(2, list.size(), "Deben existir dos árboles");
    }

    @Test
    @DisplayName("update() modifica un árbol existente")
    void shouldUpdateArbol() {
        Arbol existing = arbolRepository.save(
            new Arbol(
                "OldName",
                "OldEspecie",
                LocalDate.of(2025, 3, 1)
            )
        );
        existing.setEspecie("NewEspecie");

        Arbol updated = arbolService.update(existing);
        Arbol fromDb = arbolRepository.findById(existing.getId())
                .orElseThrow();
        
        assertEquals("NewEspecie", fromDb.getEspecie(), "La especie debe haberse actualizado");
        assertEquals(existing.getId(), updated.getId(), "El ID no debe cambiar");
    }

    @Test
    @DisplayName("delete(id) elimina árbol existente")
    void shouldDeleteArbol() {
        Arbol toDelete = arbolRepository.save(
            new Arbol(
                "ToDelete",
                "DeleteEspecie",
                LocalDate.of(2025, 4, 1)
            )
        );
        Long id = toDelete.getId();

        arbolService.delete(id);

        assertFalse(arbolRepository.existsById(id),
                "El árbol debería haber sido eliminado");
    }
}
