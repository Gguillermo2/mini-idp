package com.tuapp.mini_idp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private final RestTemplate restTemplate;

    // En application.properties: gemini.api.key=TU_KEY
    @Value("${gemini.api.key:}")
    private String apiKey;

    // URL base de Google AI Studio (modelo gratuito)
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-2.0-flash:generateContent?key=";

    public GeminiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Llama a Gemini y devuelve un resumen corto del texto recibido.
     * Si la API key no está configurada o falla, devuelve un mensaje
     * neutral para no romper el flujo principal.
     */
    public String summarizeDocument(String rawContent) {

        if (apiKey == null || apiKey.isBlank()) {
            return "Resumen no disponible: API key no configurada.";
        }

        try {
            String prompt = """
                    Analiza el siguiente documento y devuelve un resumen
                    ejecutivo en máximo 3 oraciones en español.
                    No incluyas títulos ni bullets, solo el párrafo.
                    
                    Documento:
                    """ + rawContent;

            // Construimos el body que espera la API de Gemini
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> httpEntity =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    GEMINI_URL + apiKey,
                    httpEntity,
                    Map.class
            );

            // Navegacion hacia  la respuesta JSON de Gemini:
            // response -> candidates[0] -> content -> parts[0] -> text
            List candidates = (List) response.getBody().get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            return (String) firstPart.get("text");

        } catch (Exception e) {
            // No rompemos el flujo si Gemini falla
            return "Resumen no disponible: " + e.getMessage();
        }
    }
}