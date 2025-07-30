package es.cic.curso._5.proy009.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "arbol")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Arbol {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especie;

    private LocalDate fechaPlantado;

    @OneToMany(
        mappedBy        = "arbol",
        cascade         = CascadeType.ALL,
        orphanRemoval   = true,
        fetch           = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Rama> listaRamas = new ArrayList<>();

    public Arbol() {

    }

    public Arbol(String nombre, String especie, LocalDate fechaPlantado) {
        this.nombre         = nombre;
        this.especie           = especie;
        this.fechaPlantado  = fechaPlantado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public LocalDate getFechaPlantado() {
        return fechaPlantado;
    }

    public void setFechaPlantado(LocalDate fechaPlantado) {
        this.fechaPlantado = fechaPlantado;
    }

    public List<Rama> getListaRamas() {
        return List.copyOf(listaRamas);
    }

    public void setListaRamas(List<Rama> listaRamas) {
        this.listaRamas = listaRamas;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arbol other = (Arbol) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Arbol [id=" + id + ", nombre=" + nombre + ", tipo=" + especie + ", fechaPlantado=" + fechaPlantado + "]";
    }
}
