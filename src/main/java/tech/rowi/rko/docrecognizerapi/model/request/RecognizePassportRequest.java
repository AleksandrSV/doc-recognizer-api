package tech.rowi.rko.docrecognizerapi.model.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RecognizePassportRequest {
    UUID fileId;
    Long orderId;
    Long globalCompanyId;
    Long globalPersonId;
}
