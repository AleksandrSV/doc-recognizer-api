package com.rowi.docrecognizerapi.model.yandex_api;

public class YandexCloudRequest {
    public String folderId = "b1gsqn59ai1omvhgrkkh";
    public AnalyzeSpecs[] analyze_specs = new AnalyzeSpecs[] {new AnalyzeSpecs()};

    public void addContent(String content) {
        analyze_specs[0].content = content;
    }
}
