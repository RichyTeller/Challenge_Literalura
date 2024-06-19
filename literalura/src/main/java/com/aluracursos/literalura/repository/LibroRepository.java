package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libros, Long> {
}
