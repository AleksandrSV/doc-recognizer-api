package com.rowi.docrecognizerapi.model.response;

import lombok.Data;

import java.util.List;

@Data
public class DocListResponse {
    List<DocResponse> docResponses;
}
