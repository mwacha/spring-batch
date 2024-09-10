package com.github.mwacha.application.product;

import com.github.mwacha.infra.product.api.streams.EventResult;
import com.github.mwacha.infra.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductUpdate {


    private final ProductRepository repository;

    @Transactional
    public void execute(final EventResult event) {
        repository.updateStatus(event.id(), event.status());
    }
}
