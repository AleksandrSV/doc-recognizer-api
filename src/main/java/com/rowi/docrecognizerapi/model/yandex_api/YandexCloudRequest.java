package com.rowi.docrecognizerapi.model.yandex_api;

import org.springframework.beans.factory.annotation.Value;

public class YandexCloudRequest {
    @Value("${yandexapi.folderId}")
    public String folderId;
    public AnalyzeSpecs[] analyze_specs = new AnalyzeSpecs[] {new AnalyzeSpecs()};

    public void addContent(String content) {
        analyze_specs[0].content = content;
    }
}
