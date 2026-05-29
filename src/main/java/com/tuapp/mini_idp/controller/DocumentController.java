package com.tuapp.mini_idp.controller;
import  com.tuapp.mini_idp.dto.DocumentRequest;
import  com.tuapp.mini_idp.model.Document;
import  com.tuapp.mini_idp.service.DocumentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Endpoint:
     * POST /api/documents/process
     */
    @PostMapping("/process")
    public ResponseEntity<Document> processDocument(
            @RequestBody DocumentRequest request) {

        Document processedDocument =
                documentService.processDocument(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(processedDocument);
    }
}
