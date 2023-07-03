package com.rowi.docrecognizerapi.mapper;

import com.rowi.docrecognizerapi.entity.DocEntity;
import com.rowi.docrecognizerapi.model.Doc;
import com.rowi.docrecognizerapi.model.request.RecognizePassportRequest;
import com.rowi.docrecognizerapi.model.response.DocResponse;
import com.rowi.docrecognizerapi.model.response.RecognizePassportResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper
public interface DocMapper {
    DocEntity docToDocEntity(Doc doc);
    Doc docEntityToDoc(DocEntity docEntity);
    RecognizePassportResponse docToRecognizePassportResponse(Doc doc);
    DocResponse docEntityToDocResponse(DocEntity docEntity);
    DocEntity recognizePassportRequestToDocEntity(RecognizePassportRequest recognizePassportResponse);
}
