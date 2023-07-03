package com.rowi.docrecognizerapi.repository;

import com.rowi.docrecognizerapi.entity.PassportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<PassportEntity,Long> {
}
