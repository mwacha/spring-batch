package com.github.mwacha.infra.product.api.controller;

import java.io.File;

import com.github.mwacha.application.product.ProductImportProcess;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProductImportController {

  private final ProductImportProcess productImportProcess;

  @PostMapping("/api/import-products")
  @Operation(description = "Processa o job de importação por arquivo csv.")
  public ResponseEntity<?> importProducts(@RequestParam("file") MultipartFile file) {
    productImportProcess.execute(file);
    return ResponseEntity.ok("Products imported successfully");
  }
}
