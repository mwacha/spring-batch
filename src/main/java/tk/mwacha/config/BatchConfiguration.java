package tk.mwacha.config;



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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import tk.mwacha.entity.Product;
import tk.mwacha.job.JobCompletionNotificationListener;
import tk.mwacha.job.ProductItemProcessor;
import tk.mwacha.job.ProductItemWriter;
import tk.mwacha.repository.ProductRepository;

import java.io.File;
import java.net.MalformedURLException;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
 public class BatchConfiguration {

    private final ProductRepository productRepository;

    private final ProductItemWriter productItemWriter;


    @Bean
    @StepScope
    public FlatFileItemReader<Product> reader(@Value("#{jobParameters['inputResource']}") String pathToFIle) throws MalformedURLException {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(new File(pathToFIle)));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Product>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("code", "productName", "description");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
                setTargetType(Product.class);
            }});
        }});
        return reader;
    }


    @Bean
    public ProductItemProcessor processor() {
        return new ProductItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Product> writer() {
        RepositoryItemWriter<Product> writer = new RepositoryItemWriter<>();
        writer.setRepository(productRepository);
        writer.setMethodName("save");
        return writer;
    }

        @Bean
        public Job importProductJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, JobCompletionNotificationListener listener, FlatFileItemReader<Product> itemReader) throws MalformedURLException {

            return new JobBuilder("importProductJob", jobRepository)
                    .incrementer(new RunIdIncrementer())
                    .listener(listener)
                    .flow(step1(jobRepository, transactionManager, itemReader))
                    .end()
                    .build();
        }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Product> itemReader) throws MalformedURLException {
        return new StepBuilder("step1", jobRepository)
                .<Product, Product> chunk(10, transactionManager) // intervalo de commits
                .reader(itemReader)
                .processor(processor())
                .writer(productItemWriter)
                .taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
}
