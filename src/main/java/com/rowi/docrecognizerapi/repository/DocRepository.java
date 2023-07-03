package com.rowi.docrecognizerapi.repository;

import com.rowi.docrecognizerapi.entity.DocEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocRepository extends JpaRepository<DocEntity, Long> {
    @Query("SELECT d FROM DocEntity d LEFT JOIN FETCH d.passport p WHERE d.fileId = :fileId")
    Optional<DocEntity> findByFileIdWithPassport(UUID fileId);
    @Query("SELECT d FROM DocEntity d WHERE d.fileId = :fileId AND d.deleted = false")
    Optional<DocEntity> findByFileIdNotDeleted(UUID fileId);

    @Query("SELECT d FROM DocEntity  d where d.orderId = :orderId\n" +
            "            AND (:globalCompanyId is null or d.globalCompanyId = :globalCompanyId)\n" +
            "            AND (:globalPersonId is null or d.globalPersonId = :globalPersonId)")
    Page<DocEntity> findDocByFilters(@Param("orderId") Long orderId,
                                     @Param("globalCompanyId") Long globalCompanyId,
                                     @Param("globalPersonId") Long globalPersonId,
                                     Pageable pageable);

//    Page<DocEntity> findByOrderIdAndGlobalCompanyIdAndGlobalPersonIdIsNull(Long orderId,  Long globalCompanyId,  Long globalPersonId, Pageable pageable);
}
