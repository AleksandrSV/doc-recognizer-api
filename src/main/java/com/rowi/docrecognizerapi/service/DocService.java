package com.rowi.docrecognizerapi.service;

import com.rowi.docrecognizerapi.model.Doc;
import com.rowi.docrecognizerapi.model.request.PassportRequest;
import com.rowi.docrecognizerapi.model.response.DocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DocService {
    DocResponse deleteDoc(UUID fileId);

    DocResponse searchDoc(UUID fileId);

    DocResponse redactDock(UUID fileId, Doc doc);

    Page<DocResponse> searchDocWithFilter(PassportRequest request, Pageable pageable);
}
