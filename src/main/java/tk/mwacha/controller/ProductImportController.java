package tk.mwacha.controller;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@AllArgsConstructor
public class ProductImportController {

    private  final JobLauncher jobLauncher;
    private final Job job;



    private final String TEMP_STORAGE = "/Users/Marcelo/developer/";
    @PostMapping("/import-products")
    public ResponseEntity<?> importProducts(@RequestParam("file") MultipartFile file) {
        try {
            File fileToImport = new File(TEMP_STORAGE + file.getOriginalFilename());
            file.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("inputResource", TEMP_STORAGE + file.getOriginalFilename())
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            jobLauncher.run(job, jobParameters);


            return ResponseEntity.ok("Products imported successfully");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
