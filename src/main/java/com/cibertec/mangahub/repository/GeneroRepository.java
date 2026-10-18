package com.cibertec.mangahub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.mangahub.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Integer> {
}