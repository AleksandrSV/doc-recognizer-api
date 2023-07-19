package tech.rowi.rko.docrecognizerapi.model.response;

import tech.rowi.rko.docrecognizerapi.model.Passport;
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
