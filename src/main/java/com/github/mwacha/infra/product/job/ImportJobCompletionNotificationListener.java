package com.github.mwacha.infra.product.job;

import com.github.mwacha.domain.product.ImportProduct;
import com.github.mwacha.domain.product.enums.ImportStatus;
import com.github.mwacha.infra.product.repository.ImportProductRepository;
import com.github.mwacha.infra.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportJobCompletionNotificationListener implements JobExecutionListener {


  private final ProductRepository repository;

  private final ImportProductRepository productImportRepository;

//  @Override
//  public void beforeJob(JobExecution jobExecution) {
//    productImportStatusRepository.save(
//            ImportProduct.builder()
//                    .id(UUID.randomUUID())
//                    .status(ImportStatus.PROCESSING)
//                    .build());
//  }


  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    final var importId = jobExecution.getJobParameters().getString("importId");
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! IMPORT JOB FINISHED! Time to verify the results");

      productImportRepository.updateStatus(UUID.fromString(importId), ImportStatus.SUCCESS);

    }
    else {
      productImportRepository.updateStatus(UUID.fromString(importId), ImportStatus.SUCCESS);
    }
  }
}
