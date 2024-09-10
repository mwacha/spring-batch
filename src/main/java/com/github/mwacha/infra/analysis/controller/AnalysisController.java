package com.github.mwacha.infra.analysis.controller;

import com.github.mwacha.application.analysis.AnalysisJobProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
public class AnalysisController {

 private final AnalysisJobProcess   analysisJobProcess;

  @PostMapping("/api/startJob")
  @Operation(description = "Processa oo job de importação pelos ids dos clientes.")
  public void jobProcess(@RequestBody List<Long> clientIds) {
    analysisJobProcess.execute(clientIds);
  }
}
