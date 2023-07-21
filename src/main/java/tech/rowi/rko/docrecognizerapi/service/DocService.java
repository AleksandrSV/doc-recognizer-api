package tech.rowi.rko.docrecognizerapi.service;

import org.springframework.http.ResponseEntity;
import tech.rowi.rko.docrecognizerapi.model.Doc;
import tech.rowi.rko.docrecognizerapi.model.request.PassportRequest;
import tech.rowi.rko.docrecognizerapi.model.response.DocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DocService {
    DocResponse deleteDoc(UUID fileId);

    ResponseEntity searchDoc(UUID fileId);

    ResponseEntity<DocResponse> redactDock(UUID fileId, Doc doc);

    Page<DocResponse> searchDocWithFilter(PassportRequest request, Pageable pageable);
}
