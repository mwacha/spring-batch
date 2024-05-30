package com.github.mwacha.infra.product.job;

import com.github.mwacha.infra.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportJobCompletionNotificationListener implements JobExecutionListener {


  private final ProductRepository repository;

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! IMPORT JOB FINISHED! Time to verify the results");

      repository
          .findAll()
          .forEach(product -> log.info("Found < {} > in the database.", product));
    }
  }
}
