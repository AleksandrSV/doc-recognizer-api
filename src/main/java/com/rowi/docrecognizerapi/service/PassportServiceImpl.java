package com.rowi.docrecognizerapi.service;

import com.rowi.docrecognizerapi.entity.DocEntity;
import com.rowi.docrecognizerapi.mapper.PassportMapper;
import com.rowi.docrecognizerapi.model.Passport;
import com.rowi.docrecognizerapi.repository.DocRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
@Service

public class PassportServiceImpl implements PassportService{
    public final DocRepository docRepository;

    private final PassportMapper passportMapper;

    private final RestTemplateBuilder restTemplate;


    @Value("${services.uni-file-archive.url}")
    private  String url;


    public PassportServiceImpl(DocRepository docRepository, PassportMapper passportMapper, RestTemplateBuilder restTemplate) {
        this.docRepository = docRepository;
        this.passportMapper = passportMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getEncodedPassportImage(UUID fileId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.build()
                    .exchange(url+fileId, HttpMethod.GET, entity, byte[].class);
            return  Base64.getEncoder().encodeToString(response.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Passport findPassportByFileID(UUID fileId){
        Optional<DocEntity> doc = docRepository.findByFileIdWithPassport(fileId);
        return doc.map(docEntity -> passportMapper.passportEntityToPassport(docEntity.getPassport())).orElse(null);
    }


}
