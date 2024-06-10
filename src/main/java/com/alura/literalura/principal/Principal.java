package com.alura.literalura.principal;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.ListLibros;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL = "http://gutendex.com/books/?search=";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    private Integer opcion = -1;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    private Optional<DatosLibro> getLibro(String nombreLibro) {
        try {

            String url = URL + nombreLibro.replace(" ", "%20");
            String json = consumoApi.obtenerDatos(url);

            if (json == null || json.isEmpty()) {
                System.out.println("No se recibieron datos de la API");
                return Optional.empty();
            }

            ListLibros listLibros = conversor.obtenerDatos(json, ListLibros.class);
            if (listLibros == null) {
                System.out.println("Error al convertir JSON");
                return Optional.empty();
            }
            List<DatosLibro> libros = listLibros.libros();
            if (libros == null || libros.isEmpty()) {
                return Optional.empty();
            }
            return libros.stream()
                    .filter(l -> l.titulo() != null && l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener el libro: " + e.getMessage());
            return Optional.empty();
        }
    }

    private void leerLibro(Libro libro) {
        System.out.println("************* LIBRO *************");
        System.out.println("Titulo: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor().getNombre());
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas());
        System.out.println("*********************************\n");
    }

    private void leerAutor(Autor autor) {
        System.out.println("************* AUTOR *************");
        System.out.println("Nombre: " + autor.getNombre());
        System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
        System.out.println("Fecha de fallecimiento: " + autor.getFechaDeFallecimiento());
        List<String> libros = autor.getLibros().stream()
                .map(l -> l.getTitulo())
                .collect(Collectors.toList());
        System.out.println("Libros: " + libros);
        System.out.println("*********************************\n");
    }

    public void muestraElMenu() {
        while (opcion != 0) {
            System.out.println("""
                    
                    +++SELECCIONE UNA OPCIÓN:+++++++++++++++++++++++++++++++++
                    1- Buscar libro por título (Guarda en la base de datos)  +
                    2- Ver libros registrados                                +
                    3- Ver autores registrados                               +
                    4- Ver autores vivos en un determinado año               +
                    5- Ver libros por idioma                                 +
                    0- Salir de la aplicación                                +
                    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    """);
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el nombre del libro que desea buscar:");
                    String nombreLibro = scanner.next();
                    Optional<DatosLibro> optionalLibro = getLibro(nombreLibro);

                    if (optionalLibro.isPresent()) {
                        DatosLibro datosLibro = optionalLibro.get();
                        if (!libroRepository.existsByTitulo(datosLibro.titulo())) {
                            Libro libro = new Libro(datosLibro);
                            leerLibro(libro);
                            libroRepository.save(libro);
                            System.out.println("Libro guardado exitosamente");
                        } else {
                            Libro libro = new Libro(datosLibro);
                            leerLibro(libro);
                            System.out.println("Este libro ya existe en la base de datos");
                        }
                    } else {
                        System.out.println("El libro no ha sido encontrado");
                    }
                    scanner.nextLine();
                    break;
                case 2:
                    List<Libro> libros = libroRepository.findAll();

                    if (libros.isEmpty()){
                        System.out.println("No hay libros registrados en la base de datos aún");
                    } else {
                        libros.stream()
                                .forEach(this::leerLibro);
                    }
                    break;
                case 3:
                    List<Autor> autores = autorRepository.findAll();

                    if (autores.isEmpty()){
                        System.out.println("No hay autores registrados en la base de datos aún");
                    } else {
                        autores.stream()
                                .forEach(this::leerAutor);
                    }
                    break;
                case 4:
                    autores = autorRepository.findAll();

                    if (autores.isEmpty()){
                        System.out.println("No hay autores registrados en la base de datos aún");
                    } else {
                        System.out.println("Ingresa el año vivo de autor(es) que desea buscar:");
                        Integer fechaDeFallecimiento = scanner.nextInt();
                        autores = autorRepository.findByFechaDeFallecimiento(fechaDeFallecimiento);
                        if (autores.isEmpty()){
                            System.out.println("No hay autores vivos registrados para el año " + fechaDeFallecimiento);
                        } else {
                            autores.stream()
                                    .forEach(this::leerAutor);
                        }
                    }
                    break;
                case 5:
                    libros = libroRepository.findAll();

                    if (libros.isEmpty()){
                        System.out.println("No hay libros registrados en la base de datos aún");
                    } else {
                        System.out.println("Ingrese el idioma del libro a buscar:");
                        System.out.println("es - Español");
                        System.out.println("en - Inglés");
                        System.out.println("pt - Portugués");
                        System.out.println("fr - Frances");
                        String buscarIdioma = scanner.next();
                        libros = libroRepository.findByIdioma(buscarIdioma);
                        if(libros.isEmpty()){
                            System.out.println("No hay libros registrados en " +
                                    (buscarIdioma.equals("es") ? "español" :
                                            buscarIdioma.equals("en") ? "inglés" :
                                                    buscarIdioma.equals("pt") ? "portugués" :
                                                            buscarIdioma.equals("fr") ? "francés" :
                                                                    "este idioma")
                                    );
                        }else{
                            System.out.println("<---Aquí tienes los libro en " +
                                    (buscarIdioma.equals("es") ? "español--->" :
                                            buscarIdioma.equals("en") ? "inglés--->" :
                                                    buscarIdioma.equals("pt") ? "portugués--->" :
                                                            buscarIdioma.equals("fr") ? "francés--->" :
                                                                    "")
                            );
                            libros.stream()
                                    .forEach(this::leerLibro);
                        }
                    }
                    break;

                case 0:
                    System.out.println("Finalizando la aplicación. Hasta pronto..." +
                            "\n++++(Developed by William M.)++++\n\n");
                    break;
                default:
                    System.out.println("Opción inválida, selecione una de las siguientes:");
            }
        }
    }
}
