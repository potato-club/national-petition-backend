package com.example.nationalpetition.controller;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class ApiDocumentUtils {

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        // TODO: 2021-09-16 나중에 도메인 연결하면 바꾸기
                        .host("docs.api.com")
                        .removePort(),
                prettyPrint());
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

}
