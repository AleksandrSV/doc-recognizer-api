package com.rowi.docrecognizerapi.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rowi.docrecognizerapi.api.YandexVisionApi;
import com.rowi.docrecognizerapi.entity.DocEntity;
import com.rowi.docrecognizerapi.entity.PassportEntity;
import com.rowi.docrecognizerapi.mapper.DocMapper;
import com.rowi.docrecognizerapi.mapper.PassportMapper;
import com.rowi.docrecognizerapi.model.Passport;
import com.rowi.docrecognizerapi.model.json.Entities;
import com.rowi.docrecognizerapi.model.json.Entity;
import com.rowi.docrecognizerapi.model.request.RecognizePassportRequest;
import com.rowi.docrecognizerapi.model.response.RecognizePassportResponse;
import com.rowi.docrecognizerapi.repository.DocRepository;
import com.rowi.docrecognizerapi.repository.PassportRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        //Проверка нет ли в бд уже документа
        Passport passport = passportService.findPassportByFileID(recognizeRequest.getFileId());
        if (passport != null) {
            RecognizePassportResponse recognizePassportResponse = new RecognizePassportResponse(recognizeRequest.getFileId(), passport);
            return new ResponseEntity<>(recognizePassportResponse, HttpStatus.OK);
        }
        // тут будет сервис который достает из хранилища а пока так
        String url = "https://telegra.ph/file/35584c9c3ef0ff725d89a.jpg";
        String base64Image = passportService.getEncodedPassportImage(url);

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
