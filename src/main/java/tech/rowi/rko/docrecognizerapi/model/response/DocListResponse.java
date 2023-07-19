package tech.rowi.rko.docrecognizerapi.model.response;

import lombok.Data;

import java.util.List;

@Data
public class DocListResponse {
    List<DocResponse> docResponses;
}
