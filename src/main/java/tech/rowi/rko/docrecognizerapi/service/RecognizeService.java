package tech.rowi.rko.docrecognizerapi.service;

import tech.rowi.rko.docrecognizerapi.model.request.RecognizePassportRequest;
import tech.rowi.rko.docrecognizerapi.model.response.RecognizePassportResponse;
import org.springframework.http.ResponseEntity;

public interface RecognizeService {
    ResponseEntity<RecognizePassportResponse> recognizePassport(RecognizePassportRequest recognizeRequest);
}
