package com.rowi.docrecognizerapi.mapper;

import com.rowi.docrecognizerapi.entity.PassportEntity;
import com.rowi.docrecognizerapi.model.Passport;
import org.mapstruct.Mapper;

@Mapper
public interface PassportMapper {
    PassportEntity passportToPassportEntity(Passport passport);
    Passport passportEntityToPassport(PassportEntity passportEntity);
}