package tech.rowi.rko.docrecognizerapi.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RecognizePassportRequest {
    @NotNull
    UUID fileId;
    @NotNull
    Long orderId;
    Long globalCompanyId;
    Long globalPersonId;
}
