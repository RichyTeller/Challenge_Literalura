package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private LibroRepository repositorioLibros;
    private AutorRepository repositorioAutores;

    public Principal(LibroRepository repositoryLibro, AutorRepository repositoryAutor) {
        this.repositorioLibros = repositoryLibro;
        this.repositorioAutores = repositoryAutor;

    }

    public void muestraMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    ----------------
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores
                    4 - Listar autores vivos en un determinado año
                    5 - 
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine(); // Limpiar el buffer de entrada

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    getFechaAutor();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida");
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
                            "\nAutor: " + libroBuscado.get().autor().get(0).nombre() +
                            "\nIdioma: " + libroBuscado.get().idiomas().get(0).toUpperCase() +
                            "\nNúmero de descargas: " + libroBuscado.get().numeroDeDescargas() +
                            "\n--------------------------------------\n");
        } else {
            System.out.println("El libro no fue encontrado");
        }
        return libroBuscado;
    }


    private void buscarLibro() {
        // Obtiene un Optional que contiene los datos del libro, si existen
        Optional<DatosLibros> optionalLibro = getDatosLibros();

        // Verifica si los datos del libro están presentes
        if (optionalLibro.isPresent()) {
            // Obtiene los datos del libro del Optional
            DatosLibros libro = optionalLibro.get();
            // Obtiene la lista de autores del libro
            var autores = libro.autor();

            // Verifica si hay al menos un autor en la lista
            if (!autores.isEmpty()) {
                // Obtiene el nombre, año de nacimiento y año de fallecimiento del primer autor

                // Obtener el nombre del autor
                String nombreAutor = autores.get(0).nombre() != null ? autores.get(0).nombre() : "desconocida";

                // Obtener el año de nacimiento del autor
                int birthYear = autores.get(0).birthYear() != null ? autores.get(0).birthYear() : 0;

                // Obtener el año de fallecimiento del autor
                int deathYear = autores.get(0).deathYear() != null ? autores.get(0).deathYear() : 0;


                // Busca si el autor ya existe en el repositorio por nombre
                Optional<Autores> autorExiste = repositorioAutores.findByNombre(nombreAutor);

                Autores autorGuardar;
                // Si el autor ya existe, se obtiene del Optional
                if (autorExiste.isPresent()) {
                    autorGuardar = autorExiste.get();
                    System.out.println("Autor ya registrado en la base de datos: " + nombreAutor);
                } else {
                    // Si el autor no existe, se crea una nueva instancia y se guarda en el repositorio
                    autorGuardar = new Autores(nombreAutor, birthYear, deathYear, null);
                    repositorioAutores.save(autorGuardar);
                    System.out.println("Nuevo autor registrado: " + nombreAutor);
                }

                // Lógica para registrar el libro
                // Obtiene el título del libro
                String titulo = libro.titulo();
                // Busca si el libro ya existe en el repositorio por título
                Optional<Libros> libroExiste = repositorioLibros.findByTitulo(titulo);

                // Si el libro ya existe, se notifica
                if (libroExiste.isPresent()) {
                    System.out.println("El libro ya está registrado en la base de datos");
                } else {
                    // Si el libro no existe, se crea una nueva instancia y se guarda en el repositorio
                    Libros libroParaGuardar = new Libros(libro);
                    libroParaGuardar.setAutor(autorGuardar); // Asigna el autor al libro
                    repositorioLibros.save(libroParaGuardar);
                    System.out.println("Nuevo libro registrado: " + titulo);
                }
            } else {
                // Si el libro no tiene autores especificados, se notifica
                System.out.println("El libro no tiene autor(es) especificado(s)");
            }
        } else {
            // Si no se encontraron datos del libro, se notifica
            System.out.println("No se encontraron datos de libros para registrar");
        }
    }

    private void mostrarAutores() {
        // Obtener todos los autores junto con sus libros usando la consulta personalizada
        List<Autores> autores = repositorioAutores.findAllAutoresWithLibros();

        // Iterar sobre cada autor recuperado
        autores.forEach(autor -> {
            // Imprimir información básica del autor
            System.out.print(
                    "\n------------- Info autor  --------------" +
                            "\nAutor: " + autor.getNombre() +
                            "\nFecha de nacimiento: " + autor.getFechaNacimiento() +
                            "\nFecha de Fallecimiento: " + autor.getFechaFallecimiento() +
                            "\nLibros: ");

            // Construir la lista de títulos de libros del autor actual
            List<String> titulosLibros = new ArrayList<>();
            autor.getLibros().forEach(libro -> {
                titulosLibros.add(libro.getTitulo()); // Agregar el título de cada libro a la lista
            });

            // Imprimir la lista de títulos de libros en formato [titulo1, titulo2, ...]
            System.out.print("[" + String.join(", ", titulosLibros) + "]");

            // Imprimir separador para la próxima información de autor
            System.out.println("\n--------------------------------------\n");
        });
    }

    private List<Libros> mostrarLibros() {
        List<Libros> libros = repositorioLibros.findAll();
        libros.forEach(libro -> {
            System.out.println(
                    "\n------------- LIBRO   --------------" +
                            "\nTítulo: " + libro.getTitulo() +
                            "\nAutor: " + libro.getAutor().getNombre() + // Asumiendo que es una cadena simple, ajusta según tu implementación
                            "\nIdioma: " + libro.getIdiomas() + // Asumiendo que es una lista, ajusta según tu implementación
                            "\nNúmero de descargas: " + libro.getNumeroDeDescargas() +
                            "\n--------------------------------------\n");
        });
        return libros;
    }

    private List<Autores> getFechaAutor() {
        System.out.println("Ingrese un año para ver los autores vivos de la época: ");
        int year = teclado.nextInt();
        teclado.nextLine(); // Limpiar el buffer de entrada

        // Utiliza la consulta personalizada para obtener autores junto con sus libros
        List<Autores> autores = repositorioAutores.findAutoresByFecha(year);

        // Filtrar y mostrar los autores
        for (Autores autor : autores) {
            // Construir la lista de títulos de libros del autor actual
            List<String> titulosLibros = new ArrayList<>();
            autor.getLibros().forEach(libro -> {
                titulosLibros.add(libro.getTitulo()); // Agregar el título de cada libro a la lista
            });

            // Utilizar String.join para concatenar los títulos de los libros en una sola cadena, separados por comas
            // String.join toma dos argumentos: el delimitador (", ") y la colección de elementos (titulosLibros)
            // Esto produce una cadena en el formato "titulo1, titulo2, ..."
            String librosConcatenados = String.join(", ", titulosLibros);

            // Imprimir la información del autor incluyendo la lista de títulos de libros en una sola línea
            System.out.println(
                    "\nAutor vivo en el año de: " + year +
                            "\n--------------------------------------" +
                            "\nAutor: " + autor.getNombre() +
                            "\nFecha de nacimiento: " + autor.getFechaNacimiento() +
                            "\nFecha de fallecimiento: " + autor.getFechaFallecimiento() +
                            // Incluir la cadena concatenada de libros en la línea de salida
                            "\nLibros: [" + librosConcatenados + "]" +
                            "\n--------------------------------------\n"
            );
        }

        return autores;
    }


}

