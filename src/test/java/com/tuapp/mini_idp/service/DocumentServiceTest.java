package com.tuapp.mini_idp.service;
import com.tuapp.mini_idp.dto.DocumentRequest;
import com.tuapp.mini_idp.model.Document;
import com.tuapp.mini_idp.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private GeminiClient geminiClient;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        // Gemini siempre devuelve un resumen simulado
        // para que los tests no dependan de la red
        when(geminiClient.summarizeDocument(anyString()))
                .thenReturn("Resumen simulado para tests.");

        // El repositorio devuelve el mismo documento que recibe
        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Texto con 'Factura' debe clasificarse como FINANZAS")
    void shouldClassifyAsFinanzas() {
        DocumentRequest request = new DocumentRequest();
        request.setFileName("factura_enero.pdf");
        request.setRawContent("Esta es una Factura por servicios prestados.");

        Document result = documentService.processDocument(request);

        assertEquals("FINANZAS", result.getClassification());
    }

    @Test
    @DisplayName("Texto con 'Contrato' debe clasificarse como LEGAL")
    void shouldClassifyAsLegal() {
        DocumentRequest request = new DocumentRequest();
        request.setFileName("contrato_arrendamiento.pdf");
        request.setRawContent("El presente Contrato establece las condiciones.");

        Document result = documentService.processDocument(request);

        assertEquals("LEGAL", result.getClassification());
    }

    @Test
    @DisplayName("Texto sin palabras clave debe clasificarse como GENERAL")
    void shouldClassifyAsGeneral() {
        DocumentRequest request = new DocumentRequest();
        request.setFileName("memo_interno.pdf");
        request.setRawContent("Reunión de equipo el próximo lunes a las 10am.");

        Document result = documentService.processDocument(request);

        assertEquals("GENERAL", result.getClassification());
    }

    @Test
    @DisplayName("Texto nulo debe clasificarse como GENERAL sin lanzar excepción")
    void shouldHandleNullContent() {
        DocumentRequest request = new DocumentRequest();
        request.setFileName("vacio.pdf");
        request.setRawContent(null);

        Document result = documentService.processDocument(request);

        assertEquals("GENERAL", result.getClassification());
    }

    @Test
    @DisplayName("El resumen de Gemini debe guardarse en el documento")
    void shouldSaveAiSummary() {
        DocumentRequest request = new DocumentRequest();
        request.setFileName("doc.pdf");
        request.setRawContent("Contenido cualquiera.");

        Document result = documentService.processDocument(request);

        assertNotNull(result.getAiSummary());
        assertFalse(result.getAiSummary().isBlank());
    }
}
