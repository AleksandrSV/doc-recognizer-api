package com.rowi.docrecognizerapi.model.response;

import com.rowi.docrecognizerapi.model.Passport;
import lombok.Data;

import java.util.UUID;

@Data
public class DocResponse {
    private UUID fileId;
    private Long orderId;
    private Long globalCompanyId;
    private Long globalPersonId;
    private Boolean deleted;

    private Passport passport;
}
