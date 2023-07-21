package tech.rowi.rko.docrecognizerapi.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class PassportRequest {
    @NotNull
    Long orderId;
    Long globalCompanyId;
    Long globalPersonId;
}
