package es.cic.curso._5.proy009.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso._5.proy009.exception.ModificationSecurityException;
import es.cic.curso._5.proy009.model.Arbol;
import es.cic.curso._5.proy009.service.ArbolService;

@RestController
@RequestMapping("/arboles")
public class ArbolController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArbolController.class);

    @Autowired
    private ArbolService arbolService;

    @PostMapping
    public Arbol create(@RequestBody Arbol arbol) {
        if(arbol.getId() != null) {
            LOGGER.info("La petición para sembrar el árbol " + arbol + " tiene un ID distinto de null, por lo que no puede ser procesada.");
            throw new ModificationSecurityException("No puedes crear un árbol con ID predefinido.");
        } else {
            LOGGER.info("Se está gestionando la petición para sembrar el árbol " + arbol);
            return arbolService.create(arbol);
        }
    }

    @GetMapping("/{id}")
    public Arbol get(@PathVariable("id") Long id) {
        LOGGER.info("Se está gestionando la petición para leer el árbol con ID " + id);
        return arbolService.get(id);
    }

    @GetMapping
    public List<Arbol> get() {
        LOGGER.info("Se está gestionando la petición para listar todos los árboles");
        List<Arbol> listaArboles = arbolService.get();
        return listaArboles;
    }

    @PutMapping("/{id}")
    public Arbol update(@PathVariable("id") Long id, @RequestBody Arbol arbol) {
        if (!id.equals(arbol.getId())) {
            LOGGER.error("IDs contradictorios entre URL " + id + " y cuerpo " + arbol.getId());
            throw new ModificationSecurityException("El ID del árbol en la URL no coincide con el del cuerpo.");
        }

        arbolService.get(id);

        LOGGER.info("Actualizando el árbol con ID " + id);
        return arbolService.update(arbol);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Borrando árbol con ID " + id);
        arbolService.delete(id);
    }
}
