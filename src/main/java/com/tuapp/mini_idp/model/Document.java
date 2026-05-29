package com.tuapp.mini_idp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que  representa la Tabla de documentos  den la base de datos H2.
 */

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "raw_content", columnDefinition = "CLOB")
    private String rawContent;

    @Column(name = "classification")
    private String classification;

    @Column(name = "ai_summary", columnDefinition = "CLOB")
    private String aiSummary;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
}
