package tech.rowi.rko.docrecognizerapi.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Doc {

    private UUID fileId;
    private Long orderId;
    private Long globalCompanyId;
    private Long globalPersonId;
    private Boolean deleted;

    private Passport passport;
}
