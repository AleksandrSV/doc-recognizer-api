package tech.rowi.rko.docrecognizerapi.model.request;

import lombok.Data;

@Data
public class PassportRequest {
    Long orderId;
    Long globalCompanyId;
    Long globalPersonId;
}
