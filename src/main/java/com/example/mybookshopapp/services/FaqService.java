package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.FaqEntity;
import com.example.mybookshopapp.repositories.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaqService {
    private final FaqRepository faqRepository;

    public List<FaqEntity> getFaqs() {
        return faqRepository.findAllByOrderBySortIndex();
    }
}
