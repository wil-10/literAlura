package com.alura.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutor(@JsonAlias("name") String nombreAutor,
                         @JsonAlias ("birth_year") Integer fechaDeNacimiento,
                         @JsonAlias ("death_year") Integer fechaDeFallecimiento) {
}
