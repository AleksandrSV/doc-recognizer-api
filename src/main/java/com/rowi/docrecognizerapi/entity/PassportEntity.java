package com.rowi.docrecognizerapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "passport")
public class PassportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passport_id_seq")
    @SequenceGenerator(name = "passport", sequenceName = "passport_id_seq", allocationSize = 1)

    private Long id;

    private String name;
    private String surname;
    private String middleName;
    private String birthDate;
    private String birthPlace;
    private String number;
    private String issuedBy;
    private String subdivision;
    private String issueDate;
    private String gender;
    private String expirationDate;
    private String citizenship;
}