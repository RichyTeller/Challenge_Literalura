package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autores;
import com.aluracursos.literalura.model.DatosAutores;
import com.aluracursos.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface AutorRepository extends JpaRepository<Autores, Long> {
    Optional<Autores> findByNombre(String nombre);

    @Query("SELECT a FROM Autores a JOIN FETCH a.libros")
    List<Autores> findAllAutoresWithLibros();

    // En tu repositorio de Autores, define un método para obtener autores con sus libros
    // Consulta personalizada utilizando JPQL para obtener autores con libros basado en un año dado
    @Query("SELECT a FROM Autores a LEFT JOIN FETCH a.libros WHERE a.fechaNacimiento <= :year AND a.fechaFallecimiento >= :year")
    List<Autores> findAutoresByFecha(@Param("year") int year);

}
