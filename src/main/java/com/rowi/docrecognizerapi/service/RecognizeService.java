package com.rowi.docrecognizerapi.service;

import com.rowi.docrecognizerapi.model.request.RecognizePassportRequest;
import com.rowi.docrecognizerapi.model.response.RecognizePassportResponse;
import org.springframework.http.ResponseEntity;

public interface RecognizeService {
    ResponseEntity<RecognizePassportResponse> recognizePassport(RecognizePassportRequest recognizeRequest);
}
