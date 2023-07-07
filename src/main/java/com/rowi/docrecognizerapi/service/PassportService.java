package com.rowi.docrecognizerapi.service;

import com.rowi.docrecognizerapi.model.Passport;

import java.util.UUID;


 public interface PassportService {
    String getEncodedPassportImage(UUID fileId);

     Passport findPassportByFileID(UUID fileId);
}
