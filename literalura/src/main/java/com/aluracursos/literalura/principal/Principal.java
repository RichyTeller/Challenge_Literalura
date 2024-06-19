package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibros;
import com.aluracursos.literalura.model.Libros;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

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

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0){
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
                    System.out.println("Hola");
                    break;
            }
        }
    }
    private void buscarLibroPorTitulo(){
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var busquedaUser = teclado.nextLine();
        var json = consumoAPI.obtenerDatosApi(URL_BASE + "?search=" + busquedaUser.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(busquedaUser.toUpperCase()))
                .findFirst();
//        var nuevoLibro = conversor.obtenerDatos(json, Libros.class);
//        System.out.println(nuevoLibro);
        if(libroBuscado.isPresent()){
            System.out.println(
                    "\n------------- LIBRO 📚  --------------" +
                            "\nTítulo: " + libroBuscado.get().titulo() +
                            "\nAutor: " + libroBuscado.get().autor().get(0).name() +
                            "\nIdioma: " + libroBuscado.get().idiomas()+
                            "\nNúmero de descargas: " + libroBuscado.get().numeroDeDescargas() +
                            "\n--------------------------------------\n");
        }else{
            System.out.println("El libro no fue encontrado");
        }
    }
}
