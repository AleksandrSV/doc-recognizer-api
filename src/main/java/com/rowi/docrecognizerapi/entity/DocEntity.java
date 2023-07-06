package com.rowi.docrecognizerapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "doc")
public class DocEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_id_seq")
    @SequenceGenerator(name = "doc", sequenceName = "doc_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "file_id")
    private UUID fileId;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "global_company_id")
    private Long globalCompanyId;
    @Column(name= "global_person_id")
    private Long globalPersonId;
    @Column(name="deleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "passport_id")
    private PassportEntity passport;
}