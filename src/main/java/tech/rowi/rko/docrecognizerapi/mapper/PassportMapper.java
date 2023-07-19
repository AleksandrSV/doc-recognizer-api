package tech.rowi.rko.docrecognizerapi.mapper;

import tech.rowi.rko.docrecognizerapi.entity.PassportEntity;
import tech.rowi.rko.docrecognizerapi.model.Passport;
import org.mapstruct.Mapper;

@Mapper
public interface PassportMapper {
    PassportEntity passportToPassportEntity(Passport passport);
    Passport passportEntityToPassport(PassportEntity passportEntity);
}