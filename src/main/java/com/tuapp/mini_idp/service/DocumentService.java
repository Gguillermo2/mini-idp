package com.tuapp.mini_idp.service;
//import  com.tuapp.mini_idp.service.HuggingFaceClient;
import  com.tuapp.mini_idp.dto.DocumentRequest;
import com.tuapp.mini_idp.model.Document;
import  com.tuapp.mini_idp.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final GeminiClient geminiClient;

    public DocumentService(DocumentRepository documentRepository,
                           GeminiClient geminiClient) {

        this.documentRepository = documentRepository;
        this.geminiClient = geminiClient;
    }

    /**
     * Procesa el documento:
     * - Clasifica
     * - Guarda en BD
     */
    public Document processDocument(DocumentRequest request) {

        String classification = classifyDocument(request.getRawContent());

        // Preparado para IA 
        String aiSummary = geminiClient.summarizeDocument(request.getRawContent());

        Document document = new Document();

        document.setFileName(request.getFileName());
        document.setRawContent(request.getRawContent());
        document.setClassification(classification);
        document.setProcessedAt(LocalDateTime.now());
        document.setAiSummary(aiSummary);

        return documentRepository.save(document);
    }

    /**
     * Reglas simples NLP
     */
    private String classifyDocument(String content) {

        if (content == null || content.isBlank()) {
            return "GENERAL";
        }

        String normalizedContent = content.toLowerCase();

        if (normalizedContent.contains("factura")) {
            return "FINANZAS";
        }

        if (normalizedContent.contains("contrato")) {
            return "LEGAL";
        }

        return "GENERAL";
    }

}
