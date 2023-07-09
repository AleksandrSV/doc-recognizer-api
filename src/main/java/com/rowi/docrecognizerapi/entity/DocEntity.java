package com.rowi.docrecognizerapi.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "doc")
public class DocEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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