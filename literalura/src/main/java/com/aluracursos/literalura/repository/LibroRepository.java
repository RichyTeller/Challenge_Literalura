package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libros, Long> {

    Optional<Libros> findByTitulo(String titulo);

    @Query("SELECT l FROM Libros l WHERE l.idiomas = :idiomas")
    List<Libros> findLibrosByIdiomas(@Param("idiomas") String idiomas);

}
