package com.rowi.docrecognizerapi.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "passport")
public class PassportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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