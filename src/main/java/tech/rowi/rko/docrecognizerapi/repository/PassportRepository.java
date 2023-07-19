package tech.rowi.rko.docrecognizerapi.repository;

import tech.rowi.rko.docrecognizerapi.entity.PassportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<PassportEntity,Long> {
}
