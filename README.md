# Challenge_Literalura
LiterAlura es un catálogo de libros interactivo que permite a los usuarios buscar libros y autores a través de una API específica y guardar los resultados en una base de datos relacional.

Descripción
Este proyecto tiene como objetivo interactuar con los usuarios a través de la consola, proporcionando 5 opciones de interacción. Los libros se buscan a través de la API de Gutendex.

Estructura del Proyecto LiterAlura
El proyecto se divide en varios paquetes para mantenerlo modular:

model: Contiene los registros Datos, DatosAutor y DatosLibros, así como la clase Libro y Autor, y un enum de Idiomas.
principal: Contiene la clase Principal, que maneja la interacción con el usuario y llama a los métodos correspondientes para cada opción del menú.
repository: Contiene las interfaces de los repositorios de Autor y Libro para interactuar con la base de datos.
service: Contiene las clases de servicio que contienen la lógica de negocio.
Funcionalidades
Buscar libro por título.
Listar libros registrados.
Listar autores registrados.
Listar autores vivos en un determinado año.
Listar libros por idioma.
Listar libros por título.
Tecnologías utilizadas
Java
Spring Boot
Jakarta Annotations
API de Gutendex
Cómo ejecutar el proyecto
Para ejecutar este proyecto, necesitarás tener instalado Java y Spring Boot en tu equipo. Te recomiendo usar un IDE como IntelliJ IDEA o Eclipse.

Clona el repositorio a tu equipo local.
Importa el proyecto a tu IDE.
Ejecuta el proyecto.
Sigue las instrucciones en la consola para interactuar con la aplicación.
Código principal
