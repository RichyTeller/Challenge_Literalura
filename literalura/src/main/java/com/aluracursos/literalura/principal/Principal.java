package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibros;
import com.aluracursos.literalura.model.Libros;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private LibroRepository repositorio;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ----------------
                    Elija la opcion a traves de su numero:
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibros();
                    break;
            }
        }
    }

    private Optional<DatosLibros> getDatosLibros() {
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatosApi(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datos.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println(
                    "\n------------- LIBRO 📚  --------------" +
                            "\nTítulo: " + libroBuscado.get().titulo() +
                            "\nAutor: " + libroBuscado.get().autor().get(0).name() +
                            "\nIdioma: " + libroBuscado.get().idiomas().get(0).toUpperCase() +
                            "\nNúmero de descargas: " + libroBuscado.get().numeroDeDescargas() +
                            "\n--------------------------------------\n");
        } else {
            System.out.println("El libro no fue encontrado");
        }
        return libroBuscado;
    }

    private void buscarLibroPorTitulo() {
        Optional<DatosLibros> optionalLibro = getDatosLibros();
        DatosLibros libro = optionalLibro.get();
        if (optionalLibro.isPresent()){
            var titulo = optionalLibro.get().titulo();
            Optional<Libros> libroExiste = repositorio.findByTitulo(titulo);
            if (libroExiste.isPresent()){
                System.out.println("El libro ya está registrado en el sistema");
            }else {
                Libros libroParaGuardar = new Libros(libro);
                repositorio.save(libroParaGuardar);
            }
        }
    }

    private List<Libros> mostrarLibros() {
        List<Libros> libros = repositorio.findAll();
        libros.forEach(libro -> {
            System.out.println(
                    "\n------------- LIBRO 📚  --------------" +
                            "\nTítulo: " + libro.getTitulo() +
                            "\nAutor: " + libro.getAutor() + // Asumiendo que es una cadena simple, ajusta según tu implementación
                            "\nIdioma: " + libro.getIdiomas() + // Asumiendo que es una lista, ajusta según tu implementación
                            "\nNúmero de descargas: " + libro.getNumeroDeDescargas() +
                            "\n--------------------------------------\n");
        });
        return libros;
    }
}