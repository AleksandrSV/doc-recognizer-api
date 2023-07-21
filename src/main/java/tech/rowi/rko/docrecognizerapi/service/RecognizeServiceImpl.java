package tech.rowi.rko.docrecognizerapi.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.rowi.rko.docrecognizerapi.api.YandexVisionApi;
import tech.rowi.rko.docrecognizerapi.entity.DocEntity;
import tech.rowi.rko.docrecognizerapi.entity.PassportEntity;
import tech.rowi.rko.docrecognizerapi.mapper.DocMapper;
import tech.rowi.rko.docrecognizerapi.mapper.PassportMapper;
import tech.rowi.rko.docrecognizerapi.model.Passport;
import tech.rowi.rko.docrecognizerapi.model.json.Entities;
import tech.rowi.rko.docrecognizerapi.model.json.Entity;
import tech.rowi.rko.docrecognizerapi.model.request.RecognizePassportRequest;
import tech.rowi.rko.docrecognizerapi.model.response.RecognizePassportResponse;
import tech.rowi.rko.docrecognizerapi.repository.DocRepository;
import tech.rowi.rko.docrecognizerapi.repository.PassportRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class RecognizeServiceImpl implements RecognizeService {
    private final DocRepository docRepository;
    private final PassportRepository passportRepository;
    private final PassportService passportService;
    private final YandexVisionApi yandexVisionApi;
    private final PassportMapper passportMapper;
    private final DocMapper docMapper;


    private final Logger logger = LoggerFactory.getLogger(RecognizeServiceImpl.class);

    /**
     * @return
     * @params {String.class} получает fileId документа
     */
    @Override
    public ResponseEntity<RecognizePassportResponse> recognizePassport(RecognizePassportRequest recognizeRequest) {
        Passport passport = passportService.findPassportByFileID(recognizeRequest.getFileId());
        //Проверка нет ли в бд паспорта если есть возвращаем данные
        if (passport != null) {
            RecognizePassportResponse recognizePassportResponse = new RecognizePassportResponse(recognizeRequest.getFileId(), passport);
            return new ResponseEntity<>(recognizePassportResponse, HttpStatus.OK);
        }
        //если паспорт не был найден в бд
        String base64Image = passportService.getEncodedPassportImage(recognizeRequest.getFileId());
        Passport pass = deserializeJson(yandexVisionApi.recognition(base64Image).getBody());
        RecognizePassportResponse recognizePass = new RecognizePassportResponse(recognizeRequest.getFileId(), pass);
        //сохранение данных в бд
        saveDoc(pass, recognizeRequest);

        return new ResponseEntity<>(recognizePass, HttpStatus.OK);
    }


    private Passport deserializeJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            String s = "{\"entities\":" + rootNode.findValue("entities") + "}";
            Entities newJsonNode = objectMapper.readValue(s, Entities.class);

            HashMap<String, String> map = new HashMap<>();
            for (Entity e : newJsonNode.getEntities()) {
                map.put(CaseUtils.toCamelCase(e.getName(), false, '_'), e.getText());
            }

            return objectMapper.convertValue(map, Passport.class);
        } catch (IOException e) {
            return null;
        }
    }

    private void saveDoc(Passport pass, RecognizePassportRequest recognizeRequest) {
        PassportEntity passportEntity = passportMapper.passportToPassportEntity(pass);
        DocEntity docEntity = docMapper.recognizePassportRequestToDocEntity(recognizeRequest);
        passportRepository.save(passportEntity);
        docEntity.setPassport(passportEntity);
        docEntity.setDeleted(false);
        docRepository.save(docEntity);
    }
}
