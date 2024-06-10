# LiterAlura


LiterAlura es una aplicacion de consola la cual se conecta a una base de datos de libros (https://gutendex.com) en la que se realiza una busqueda especifica y se recupera informacion relacionada a los libros como el título, nombre de los autores, idiomas y numero de descargas; a su vez la informacion general de cada autor como el nombre, fecha de nacimiento y fecha de fallecimiento.

## Tabla de Contenidos

- [Uso](#uso)
- [Ejemplo](#ejemplo)
- [Autor](#autor)

### Uso

Aplicación de consola (IntelliJ IDEA)
* Abrir el el proyecto en IntelliJ IDEA
* Crear una base de datos en Postgres y las variables de entorno nombradas como se muestran en el archivo application.properties de este mismo repositorio.
* Ejecuta el archivo LiteraluraApplication
* Elegir una de las opciones que el menú proporciona. (Ver ejemplo).


#### Ejemplo

```bash
+++SELECCIONE UNA OPCIÓN:+++++++++++++++++++++++++++++++++
1- Buscar libro por título (Guarda en la base de datos)  +
2- Ver libros registrados                                +
3- Ver autores registrados                               +
4- Ver autores vivos en un determinado año               +
5- Ver libros por idioma                                 +
0- Salir de la aplicación                                +
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

(Usuario selecciona la opción 1)

Ingrese el nombre del libro que desea buscar:

(Usuario ingresa: Don quijote)

************* LIBRO *************
Titulo: Don Quijote
Autor: Cervantes Saavedra, Miguel de
Idioma: es
Numero de descargas: 14926
********************************* 

```
