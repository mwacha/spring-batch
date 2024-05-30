package com.github.mwacha.application.product;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ProductImportProcess {

    private final JobLauncher jobLauncher;

    @Qualifier("importProductJob")
    private final Job importProductJob;

    private final String TEMP_STORAGE = "/Users/Marcelo/developer/"; // TODO Change to your path

    public void execute(final MultipartFile file) {
        try {
            File fileToImport = new File(TEMP_STORAGE + file.getOriginalFilename());
            file.transferTo(fileToImport);

            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addString("inputResource", TEMP_STORAGE + file.getOriginalFilename())
                            .addLong("startAt", System.currentTimeMillis())
                            .toJobParameters();

            jobLauncher.run(importProductJob, jobParameters);


        } catch (Exception e) {
            throw new RuntimeException("Job failed");
        }
    }
}
