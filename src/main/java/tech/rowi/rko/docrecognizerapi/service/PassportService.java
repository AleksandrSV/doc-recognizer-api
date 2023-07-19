package tech.rowi.rko.docrecognizerapi.service;

import tech.rowi.rko.docrecognizerapi.model.Passport;

import java.util.UUID;


 public interface PassportService {
    String getEncodedPassportImage(UUID fileId);

     Passport findPassportByFileID(UUID fileId);
}
