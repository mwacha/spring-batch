package com.github.mwacha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import com.github.mwacha.infra.product.api.controller.ProductImportController;

@ExtendWith(MockitoExtension.class)
class ProductImportControllerTest {

  @InjectMocks private ProductImportController productImportController;

  @Mock private JobLauncher jobLauncher;

  @Mock private Job job;

  private MockMvc mockMvc;

  private static final String CONTEXT_ROOT = "/import-products";

  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(productImportController).build();
  }

  @Test
  void shouldImportProductsSuccessfully() throws Exception {
    Path tempDir = Files.createTempDirectory("productImport");
    Path tempFile = Files.createTempFile(tempDir, "products", ".csv");
    MultipartFile file =
        new MockMultipartFile(
            "file", tempFile.getFileName().toString(), "text/plain", "test data".getBytes());
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("inputResource", tempDir + File.separator + file.getOriginalFilename())
            .addLong("startAt", System.currentTimeMillis())
            .toJobParameters();
    when(jobLauncher.run(any(Job.class), any(JobParameters.class)))
        .thenReturn(mock(JobExecution.class));
    ResponseEntity<?> response = productImportController.importProducts(file);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("Products imported successfully", response.getBody());
  }

  @Test
  void shouldThrowExceptionWhenImportFails() throws Exception {
    mockMvc
        .perform(post(CONTEXT_ROOT).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andReturn();
  }
}
