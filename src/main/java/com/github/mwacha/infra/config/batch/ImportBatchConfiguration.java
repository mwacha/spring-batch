package com.github.mwacha.infra.config.batch;

import java.io.File;
import java.net.MalformedURLException;
import java.util.UUID;

import com.github.mwacha.config.AbstractBatchJobConfig;
import com.github.mwacha.domain.product.Product;
import com.github.mwacha.infra.product.job.ImportJobCompletionNotificationListener;
import com.github.mwacha.infra.product.job.ImportProductItemProcessor;
import com.github.mwacha.infra.product.job.ImportProductItemWriter;
import com.github.mwacha.infra.product.repository.ProductRepository;
import com.github.mwacha.listener.JobNotificationListener;
import com.github.mwacha.process.AbstractItemReader;
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
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


@RequiredArgsConstructor
public class ImportBatchConfiguration extends AbstractBatchJobConfig {

  private final ProductRepository productRepository;

  private final ImportProductItemWriter productItemWriter;

    @Bean
    @StepScope
    public FlatFileItemReader<Product> reader(
            @Value("#{jobParameters['inputResource']}") String pathToFile,
            @Value("#{jobParameters['importId']}") String importId) throws MalformedURLException {

        FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(new File(pathToFile)));
        reader.setLinesToSkip(1);

        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("code", "productName", "description");
        lineMapper.setLineTokenizer(tokenizer);

        // Using lambda for FieldSetMapper to set importId
        BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>() {
            @Override
            public Product mapFieldSet(FieldSet fieldSet) throws BindException {
                Product product = super.mapFieldSet(fieldSet);
                product.setImportProductId(UUID.fromString(importId));
                return product;
            }
        };
        fieldSetMapper.setTargetType(Product.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);

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

    @Override
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager, JobNotificationListener listener, AbstractItemReader reader) {
        return null;
    }


    @Bean
  public Step importStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      @Qualifier("reader") FlatFileItemReader<Product> itemReader)
      throws MalformedURLException {
    return new StepBuilder("importStep", jobRepository)
        .<Product, Product>chunk(100, transactionManager) // commit-interval
        .reader(itemReader)
        .processor(processor())
        .writer(productItemWriter)
        .build();
  }


    @Override
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, AbstractItemReader reader) {
        return null;
    }
}
