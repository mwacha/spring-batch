package tk.mwacha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@RestController
@RequiredArgsConstructor
public class ProductImportController {

    private final JobLauncher jobLauncher;
    private final Job job;



    private final String TEMP_STORAGE = "/Users/Marcelo/developer/"; //TODO Change to your path
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
