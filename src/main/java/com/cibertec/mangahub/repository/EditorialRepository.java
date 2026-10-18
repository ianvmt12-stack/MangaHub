package com.cibertec.mangahub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.mangahub.model.Editorial;

public interface EditorialRepository extends JpaRepository<Editorial, Integer> {
}