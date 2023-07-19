package tech.rowi.rko.docrecognizerapi.model;

import lombok.Data;


@Data
public class Passport {
    String name;
    String surname;
    String middleName;
    String birthDate;
    String birthPlace;
    String number;
    String issuedBy;
    String subdivision;
    String issueDate;
    String gender;
    String expirationDate;
    String citizenship;
}