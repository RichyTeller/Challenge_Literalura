package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idiomas;
    private Integer numeroDeDescargas;

    public Libros(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        this.autor = datosLibros.autor().get(0).name();
        this.idiomas = datosLibros.idiomas().get(0).toUpperCase();
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    public Libros() {
        // Constructor por defecto necesario para la deserialización
    }

    public Long getId() {
        return Id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }


    public String getIdiomas() {
        return idiomas;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }
}

