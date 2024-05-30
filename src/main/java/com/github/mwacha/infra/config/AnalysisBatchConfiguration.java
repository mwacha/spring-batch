package com.github.mwacha.infra.config;

import com.github.mwacha.infra.analysis.job.AnalysisJobCompletionNotificationListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import com.github.mwacha.domain.analysis.AnalysisResult;
import com.github.mwacha.domain.analysis.Charge;
import com.github.mwacha.infra.analysis.job.AnalysisAnalysisResultItemProcessor;
import com.github.mwacha.infra.analysis.job.AnalysisChargeItemReader;
import com.github.mwacha.infra.analysis.job.AnalysisAnalysisResultItemWriter;
import com.github.mwacha.infra.analysis.repository.AnalysisResultRepository;

import java.util.List;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
@Order(2)
public class AnalysisBatchConfiguration {

  private final AnalysisResultRepository analysisResultRepository;

  private final AnalysisAnalysisResultItemWriter itemWriter;

  @Bean
  @StepScope
  @Primary
  public AnalysisChargeItemReader readerDataBase(@Value("#{jobParameters['clientIds']}") List<Long> clientIds) {
    final var item = new AnalysisChargeItemReader();
    item.setClientIds(clientIds);

    return item;
  }

  @Bean
  public ItemProcessor<List<Charge>, List<AnalysisResult>> apiProcessor() {
    return new AnalysisAnalysisResultItemProcessor();
  }

  @Bean
  public RepositoryItemWriter<AnalysisResult> resultApiWriter() {
    RepositoryItemWriter<AnalysisResult> writer = new RepositoryItemWriter<>();
    writer.setRepository(analysisResultRepository);
    writer.setMethodName("save");
    return writer;
  }

  @Bean
  public Job apiJob(
          JobRepository jobRepository,
          PlatformTransactionManager transactionManager,
          AnalysisJobCompletionNotificationListener listener,
          ItemReader itemReader) {

    return new JobBuilder("apiJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(apiStep(jobRepository, transactionManager, itemReader))
            .end()
            .build();
  }

  @Bean
  public Step apiStep(
          JobRepository jobRepository,
          PlatformTransactionManager transactionManager,
          ItemReader itemReader) {

    return new StepBuilder("apiStep", jobRepository)
            .<Charge, AnalysisResult>chunk(10, transactionManager) // commit-interval
            .reader(itemReader)
            .processor(apiProcessor())
            .writer(itemWriter)
            .build();
  }
}
