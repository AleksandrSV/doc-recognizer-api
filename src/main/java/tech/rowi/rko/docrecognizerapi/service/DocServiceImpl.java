package tech.rowi.rko.docrecognizerapi.service;

import lombok.RequiredArgsConstructor;
import org.webjars.NotFoundException;
import tech.rowi.rko.docrecognizerapi.entity.DocEntity;
import tech.rowi.rko.docrecognizerapi.mapper.DocMapper;
import tech.rowi.rko.docrecognizerapi.model.Doc;
import tech.rowi.rko.docrecognizerapi.model.request.PassportRequest;
import tech.rowi.rko.docrecognizerapi.model.response.DocResponse;
import tech.rowi.rko.docrecognizerapi.repository.DocRepository;
import tech.rowi.rko.docrecognizerapi.repository.PassportRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {
    private final DocMapper docMapper;
    private final DocRepository docRepository;
    private final PassportRepository passportRepository;

    @Override
    public DocResponse deleteDoc(UUID fileId) {
        Optional<DocEntity> deo = docRepository.findByFileIdWithPassport(fileId);
        if (deo.isPresent()) {
            deo.get().setDeleted(true);
            docRepository.save(deo.get());
            return docMapper.docEntityToDocResponse(deo.get());
        } else return null;
    }

    @Override
    public DocResponse searchDoc(UUID fileId) {
        Optional<DocEntity> doc = docRepository.findByFileIdNotDeleted(fileId);

        if (doc.isPresent()) {
            System.out.println(doc.get());
            return docMapper.docEntityToDocResponse(doc.get());
        } else {
            throw new NotFoundException("Document with fileId  " +fileId.toString()+ " will not find");
        }
    }

    @Override
    public DocResponse redactDock(UUID fileId, Doc doc) {
        Optional<DocEntity> deo = docRepository.findByFileIdWithPassport(fileId);
        if (deo.isPresent()) {
            DocEntity rde = docMapper.docToDocEntity(doc);
            rde.setId(deo.get().getId());
            rde.getPassport().setId(deo.get().getPassport().getId());
            docRepository.save(rde);
            return docMapper.docEntityToDocResponse(rde);
        } else {
            throw new NotFoundException("Document with fileId  " +fileId.toString()+ " does not exist");
        }
    }

    @Override
    public Page<DocResponse> searchDocWithFilter(PassportRequest request, Pageable pageable) {
        Page<DocEntity> docEntities = docRepository
                .findDocByFilters(request.getOrderId(), request.getGlobalCompanyId(), request.getGlobalPersonId(), pageable);
        log.debug("Page docentities {}", docEntities.getContent());
        return new PageImpl<>(docEntities.stream()
                .filter(doc -> !doc.getDeleted())
                .map(docMapper::docEntityToDocResponse)
                .collect(Collectors.toList()), docEntities.getPageable(), docEntities.getTotalElements());
    }
}
