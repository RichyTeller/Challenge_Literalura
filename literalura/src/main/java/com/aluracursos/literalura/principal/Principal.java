package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
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
        System.out.println("Que libro desea buscar");
        var busquedaUser = teclado.nextLine();
        var json = consumoAPI.obtenerDatosApi(URL_BASE + "?search=" + busquedaUser.replace(" ","+"));
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);
    }
}
