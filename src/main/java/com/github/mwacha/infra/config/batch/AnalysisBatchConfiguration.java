package com.github.mwacha.infra.config.batch;

import com.github.mwacha.config.AbstractBatchJobConfig;
import com.github.mwacha.domain.analysis.AnalysisResult;
import com.github.mwacha.domain.analysis.Charge;
import com.github.mwacha.infra.analysis.job.AnalysisAnalysisResultItemProcessor;
import com.github.mwacha.infra.analysis.job.AnalysisAnalysisResultItemWriter;
import com.github.mwacha.infra.analysis.job.AnalysisChargeItemReader;
import com.github.mwacha.infra.analysis.job.AnalysisJobCompletionNotificationListener;
import com.github.mwacha.infra.analysis.repository.AnalysisResultRepository;
import java.util.List;

import com.github.mwacha.listener.JobNotificationListener;
import com.github.mwacha.process.AbstractItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@RequiredArgsConstructor
public class AnalysisBatchConfiguration extends AbstractBatchJobConfig {

  private final AnalysisResultRepository analysisResultRepository;

  private final AnalysisAnalysisResultItemWriter itemWriter;

  @Qualifier("analysisJobCompletionNotificationListener")
  private final AnalysisJobCompletionNotificationListener analysisJobCompletionNotificationListener;

  @Bean
  @StepScope
  public AnalysisChargeItemReader readerDataBase(
      @Value("#{jobParameters['clientIds']}") List<Long> clientIds) {
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


  @Override
  @Bean("analysisJob")
  public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                 @Qualifier("analysisJobCompletionNotificationListener") JobNotificationListener listener,  @Qualifier("analysisChargeItemReader") AbstractItemReader reader) {
    return new JobBuilder("analysisJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(analysisJobCompletionNotificationListener)
            .flow(step(jobRepository, transactionManager, reader))
            .end()
            .build();
  }

  @Override
  //@Bean
  public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, @Qualifier("analysisChargeItemReader") AbstractItemReader reader) {
    return new StepBuilder("analysisStep", jobRepository)
            .<Charge, AnalysisResult>chunk(10, transactionManager) // commit-interval
            .reader(reader)
            .processor(apiProcessor())
            .writer(itemWriter)
            .build();
  }
}
