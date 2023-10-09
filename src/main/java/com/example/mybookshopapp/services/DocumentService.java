package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.DocumentEntity;
import com.example.mybookshopapp.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentService {
    private final DocumentRepository documentRepository;

    public List<DocumentEntity> getDocuments(){
        return documentRepository.findAllByOrderBySortIndex();
    }

    public DocumentEntity getDocumentBySlug(String documentSlug) {
        return documentRepository.findBySlug(documentSlug);
    }
}
