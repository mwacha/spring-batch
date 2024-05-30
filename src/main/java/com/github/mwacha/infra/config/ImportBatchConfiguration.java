package com.github.mwacha.infra.config;

import java.io.File;
import java.net.MalformedURLException;

import com.github.mwacha.domain.product.Product;
import com.github.mwacha.infra.product.job.ImportJobCompletionNotificationListener;
import com.github.mwacha.infra.product.job.ImportProductItemProcessor;
import com.github.mwacha.infra.product.job.ImportProductItemWriter;
import com.github.mwacha.infra.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class ImportBatchConfiguration {

  private final ProductRepository productRepository;

  private final ImportProductItemWriter productItemWriter;

  @Bean
  @StepScope
  public FlatFileItemReader<Product> reader(
      @Value("#{jobParameters['inputResource']}") String pathToFIle) throws MalformedURLException {
    FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
    reader.setResource(new FileSystemResource(new File(pathToFIle)));
    reader.setLinesToSkip(1);
    reader.setLineMapper(
        new DefaultLineMapper<Product>() {
          {
            setLineTokenizer(
                new DelimitedLineTokenizer() {
                  {
                    setNames("code", "productName", "description");
                  }
                });
            setFieldSetMapper(
                new BeanWrapperFieldSetMapper<Product>() {
                  {
                    setTargetType(Product.class);
                  }
                });
          }
        });
    return reader;
  }

  @Bean
  public ImportProductItemProcessor processor() {
    return new ImportProductItemProcessor();
  }

  @Bean
  public RepositoryItemWriter<Product> writer() {
    RepositoryItemWriter<Product> writer = new RepositoryItemWriter<>();
    writer.setRepository(productRepository);
    writer.setMethodName("save");
    return writer;
  }

  @Bean("importProductJob")
  public Job importProductJob(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ImportJobCompletionNotificationListener listener,
      @Qualifier("reader") FlatFileItemReader<Product> itemReader)
      throws MalformedURLException {

    return new JobBuilder("importProductJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(importStep(jobRepository, transactionManager, itemReader))
        .end()
        .build();
  }


  @Bean
  public Step importStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      @Qualifier("reader") FlatFileItemReader<Product> itemReader)
      throws MalformedURLException {
    return new StepBuilder("importStep", jobRepository)
        .<Product, Product>chunk(10, transactionManager) // commit-interval
        .reader(itemReader)
        .processor(processor())
        .writer(productItemWriter)
        .build();
  }
}
