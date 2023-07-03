package com.rowi.docrecognizerapi.service;

import com.rowi.docrecognizerapi.entity.DocEntity;
import com.rowi.docrecognizerapi.mapper.PassportMapper;
import com.rowi.docrecognizerapi.model.Passport;
import com.rowi.docrecognizerapi.repository.DocRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class PassportServiceImpl implements PassportService{
    public final DocRepository docRepository;

    private final PassportMapper passportMapper;

    @Override
    public String getEncodedPassportImage(UUID fileId) {
        return null;
    }

    @Override
    public Passport findPassportByFileID(UUID fileId){
        Optional<DocEntity> doc = docRepository.findByFileIdWithPassport(fileId);
        return doc.map(docEntity -> passportMapper.passportEntityToPassport(docEntity.getPassport())).orElse(null);
    }

    @Override
    public String getEncodedPassportImage(String urlLink) {
        try {
            URL url = new URL(urlLink);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
            byte[] imageBytes = bis.readAllBytes();
            bis.close();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
