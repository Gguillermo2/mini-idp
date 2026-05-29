package com.tuapp.mini_idp.repository;
import com.tuapp.mini_idp.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends  JpaRepository<Document, Long> {
    /**
     * Cuenta documentos FINANZAS procesados hoy
     */
    @Query(value = """
            SELECT COUNT(*)
            FROM documents
            WHERE classification = 'FINANZAS'
            AND CAST(processed_at AS DATE) = CURRENT_DATE
            """, nativeQuery = true)
    Long countFinanceDocumentsToday();

}
