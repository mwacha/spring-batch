package com.github.mwacha.infra.analysis.controller;

import com.github.mwacha.application.analysis.AnalysisJobProcess;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnalysisController {

  private final AnalysisJobProcess analysisJobProcess;

  @PostMapping("/api/startJob")
  public void jobProcess(@RequestBody List<Long> clientIds) {
    analysisJobProcess.execute(clientIds);
  }
}
