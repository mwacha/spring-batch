package com.github.mwacha.infra.product.job;

import com.github.mwacha.process.AbstractItemReader;
import com.github.mwacha.process.AbstractItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import com.github.mwacha.domain.product.Product;
import com.github.mwacha.infra.product.repository.ProductRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductItemWriter extends AbstractItemWriter<Product> {

  private final ProductRepository repository;

  @Override
  public void write(Chunk<? extends Product> chunk) throws Exception {
   log.info("Writer Thread {}", Thread.currentThread().getName());
    repository.saveAll(chunk);
  }
}
