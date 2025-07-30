package es.cic.curso._5.proy009.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso._5.proy009.exception.ArbolException;
import es.cic.curso._5.proy009.model.Arbol;
import es.cic.curso._5.proy009.repository.ArbolRepository;

@Transactional
@Service
public class ArbolService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArbolService.class);

    @Autowired
    private ArbolRepository arbolRepository;

    public Arbol create(Arbol arbol) {
        LOGGER.info("Plantando el árbol " + arbol);
        arbol = arbolRepository.save(arbol);
        return arbol;
    }

    @Transactional(readOnly = true)
    public Arbol get(Long id) {
        LOGGER.info("Buscando el árbol con ID " + id);
        return arbolRepository.findById(id)
            .orElseThrow(() -> new ArbolException(id));
    }
    

    @Transactional(readOnly = true)
    public List<Arbol> get() {
        LOGGER.info("Mostrando lista de árboles");
        return arbolRepository.findAll();
    }

    public Arbol update(Arbol arbol) {
        LOGGER.info("Actualizando árbol con ID = " + arbol.getId());    
        
        Long id = arbol.getId(); 
        
        if (!arbolRepository.existsById(id)) {
            LOGGER.error("Intento de actualizar un árbol inexistente " + id);
            throw new ArbolException(id);
        }

        return arbolRepository.save(arbol);
    }

    public void delete(Long id) {
        LOGGER.info("Eliminando árbol con ID " + id);

        if (!arbolRepository.existsById(id)) {
            LOGGER.error("Intento de eliminar un árbol inexistente " + id);
            throw new ArbolException(id);
        }

        arbolRepository.deleteById(id);
    }
}
