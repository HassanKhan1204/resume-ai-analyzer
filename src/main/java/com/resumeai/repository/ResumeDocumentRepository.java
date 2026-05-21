package com.resumeai.repository;

import com.resumeai.domain.ResumeDocument;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeDocumentRepository extends JpaRepository<ResumeDocument, UUID> {

    @Override
    @EntityGraph(attributePaths = "analysis")
    Optional<ResumeDocument> findById(UUID id);
}
