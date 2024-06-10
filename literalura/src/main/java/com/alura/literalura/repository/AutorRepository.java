package com.alura.literalura.repository;
import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT f FROM Autor f WHERE f.fechaDeNacimiento < :fechaDeFallecimiento AND f.fechaDeFallecimiento > :fechaDeFallecimiento ORDER BY f.nombre ASC")
    List<Autor> findByFechaDeFallecimiento(Integer fechaDeFallecimiento);
}
