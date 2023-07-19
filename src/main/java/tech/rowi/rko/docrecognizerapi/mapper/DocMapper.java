package tech.rowi.rko.docrecognizerapi.mapper;

import tech.rowi.rko.docrecognizerapi.entity.DocEntity;
import tech.rowi.rko.docrecognizerapi.model.Doc;
import tech.rowi.rko.docrecognizerapi.model.request.RecognizePassportRequest;
import tech.rowi.rko.docrecognizerapi.model.response.DocResponse;
import tech.rowi.rko.docrecognizerapi.model.response.RecognizePassportResponse;
import org.mapstruct.Mapper;

@Mapper
public interface DocMapper {
    DocEntity docToDocEntity(Doc doc);
    Doc docEntityToDoc(DocEntity docEntity);
    RecognizePassportResponse docToRecognizePassportResponse(Doc doc);
    DocResponse docEntityToDocResponse(DocEntity docEntity);
    DocEntity recognizePassportRequestToDocEntity(RecognizePassportRequest recognizePassportResponse);
}
