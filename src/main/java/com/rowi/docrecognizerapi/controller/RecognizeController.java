package com.rowi.docrecognizerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rowi.docrecognizerapi.model.Doc;
import com.rowi.docrecognizerapi.model.request.PassportRequest;
import com.rowi.docrecognizerapi.model.request.RecognizePassportRequest;
import com.rowi.docrecognizerapi.model.response.DocResponse;
import com.rowi.docrecognizerapi.model.response.RecognizePassportResponse;
import com.rowi.docrecognizerapi.model.yandex_api.YandexCloudRequest;
import com.rowi.docrecognizerapi.service.DocService;
import com.rowi.docrecognizerapi.service.RecognizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Passport", description = "Passport management APIs")
@RestController
@RequestMapping("/recognize")
@RequiredArgsConstructor
public class RecognizeController {
    private final RecognizeService recognizeService;
    private final DocService docService;

    //Тест JSON
    @Operation(summary = "Test json", description = "Тестирование парсинга распознанного паспорта")
    @RequestMapping(value = "/test_json", method = RequestMethod.GET)
    public ResponseEntity<String> testJson() {
        try {
            YandexCloudRequest yandexCloudRequest = new YandexCloudRequest();
            yandexCloudRequest.addContent("content");
            ObjectMapper objectMapper = new ObjectMapper();
            String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(yandexCloudRequest);
            return new ResponseEntity<>(objJackson, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //1
    @Operation(summary = "Распознавание паспорта", description = "Распознавание документов по fileId из uni-file-archive ")
    @RequestMapping(value = "/passport", method = RequestMethod.POST)
    public ResponseEntity<RecognizePassportResponse> recognizePassport(@RequestBody RecognizePassportRequest request) {
       return recognizeService.recognizePassport(request);
    }
    //2
    @Operation(summary = "Получение данных паспорта по fileId", description = "Получение  документа по fileId из uni-file-archive(только действующие документы)")
    @RequestMapping(value = "/passport/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<DocResponse> recognizedPassport(@Parameter(description = "fileId из uni-file-archive") @PathVariable UUID fileId) {
        return new ResponseEntity<>(docService.searchDoc(fileId),HttpStatus.OK);
    }
    //3 !ДОБАВИТЬ  PAGE
    /**
     * Получение документов по фильтрам
     *
     * @param request  Запрос для фильтрации паспортов
     * @param pageable Объект для пейджинга и сортировки результатов
     * @return Список документов, отобранных по фильтрам, в виде объекта Page
     */
    @Operation(summary = "Получение документов по фильтрам", description = "Список документов, отобранных по фильтрам, возвращается в виде объекта Page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @RequestMapping(value = "/passport", method = RequestMethod.GET)
    public ResponseEntity<List<DocResponse>> passportPages(@ModelAttribute PassportRequest request, Pageable pageable) {
        if(request.getOrderId()==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<DocResponse> docPages = docService.searchDocWithFilter(request,pageable).getContent();
        if(docPages.size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(docPages,HttpStatus.OK);
    }
    //4
    @Operation(summary = "Редактирование документа", description = "Редактирование документа по его fileID, если данные были неправильно распознаны")
    @RequestMapping(value = "/passport/{fileId}", method = RequestMethod.PUT)
    public ResponseEntity<DocResponse> redactPassport(@Parameter(description = "UUID fileId из uni-file-archive") @PathVariable UUID fileId, @RequestBody Doc doc) {
        DocResponse dr = docService.redactDock(fileId, doc);
        if(dr != null)
            return new ResponseEntity<>(dr, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //5
    @Operation(summary = "Удаление данных документа", description = "При удалении в бд в поле deleted ставится true")
    @RequestMapping(value = "/passport/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity<DocResponse> deleteDoc(@Parameter(description = "UUID fileId из uni-file-archive") @PathVariable UUID fileId) {
        DocResponse dr = docService.deleteDoc(fileId);
        if(dr != null)
            return new ResponseEntity<>(dr, HttpStatus.OK);
        else  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}