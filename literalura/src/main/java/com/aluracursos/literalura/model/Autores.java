package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;

    private String nombre;

    @Column(name = "fecha_nacimiento")
    private int fechaNacimiento;

    @Column(name = "fecha_fallecimiento")
    private int fechaFallecimiento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)  // Cambiado a FetchType.LAZY por buena práctica
    private List<Libros> libros;


    // Constructor con argumentos
    public Autores(String nombre, int fechaNacimiento, int fechaFallecimiento, List<Libros> libros) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
        this.libros = libros;
    }

    public Autores(List<DatosAutores> infoAutor) {
    }

    // Constructor sin argumentos
    public Autores() {
    }


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(int fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libros> getLibros() {
        return libros;
    }

    public void setLibros(List<Libros> libros) {
        this.libros = libros;
    }
}
