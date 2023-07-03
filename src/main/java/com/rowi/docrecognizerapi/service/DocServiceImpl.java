package com.rowi.docrecognizerapi.service;

import com.rowi.docrecognizerapi.entity.DocEntity;
import com.rowi.docrecognizerapi.mapper.DocMapper;
import com.rowi.docrecognizerapi.model.Doc;
import com.rowi.docrecognizerapi.model.request.PassportRequest;
import com.rowi.docrecognizerapi.model.response.DocResponse;
import com.rowi.docrecognizerapi.repository.DocRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocServiceImpl implements DocService{
    private final DocMapper docMapper;
    private final DocRepository docRepository;
    private final Logger logger = LoggerFactory.getLogger(DocServiceImpl.class);
    @Override
    public DocResponse deleteDoc(UUID fileId) {
        Optional<DocEntity> deo = docRepository.findByFileIdWithPassport(fileId);
        if(deo.isPresent()){
            deo.get().setDeleted(true);
            docRepository.save(deo.get());
            return docMapper.docEntityToDocResponse(deo.get());
        }else return null;
    }

    @Override
    public DocResponse searchDoc(UUID fileId) {
        Optional<DocEntity> doc = docRepository.findByFileIdNotDeleted(fileId);

        if(doc.isPresent()){
            System.out.println(doc.get());
            return  docMapper.docEntityToDocResponse( doc.get());
        } else {
            return null;
        }
    }

    @Override
    public DocResponse redactDock(UUID fileId, Doc doc) {
        Optional<DocEntity> deo = docRepository.findByFileIdWithPassport(fileId);
        if(deo.isPresent()){
            DocEntity rde = docMapper.docToDocEntity(doc);
            rde.setId(deo.get().getId());
            docRepository.save(rde);
            return docMapper.docEntityToDocResponse(rde);
        }else return null;
    }

    @Override
    public Page<DocResponse> searchDocWithFilter(PassportRequest request, Pageable pageable) {
        Page<DocEntity> docEntities = docRepository
                .findDocByFilters(request.getOrderId(), request.getGlobalCompanyId(), request.getGlobalPersonId(), pageable);
        logger.debug("Page docentities {}", docEntities.getContent());
        return new PageImpl<>(docEntities.stream()
              .filter(doc -> !doc.getDeleted())
              .map(docMapper::docEntityToDocResponse)
              .collect(Collectors.toList()), docEntities.getPageable(), docEntities.getTotalElements());
    }
}
