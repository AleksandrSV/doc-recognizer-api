package com.rowi.docrecognizerapi.model.response;

import com.rowi.docrecognizerapi.model.Passport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecognizePassportResponse {
    UUID fileId;
    Passport passport;


}
