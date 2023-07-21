package tech.rowi.rko.docrecognizerapi.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class Doc {
    @NotNull
    private UUID fileId;
    @NotNull
    private Long orderId;
    private Long globalCompanyId;
    private Long globalPersonId;
    private Boolean deleted;

    private Passport passport;
}
