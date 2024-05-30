package com.github.mwacha.infra.analysis.controller;

import com.github.mwacha.application.analysis.AnalysisJobProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AnalysisController {

 private final AnalysisJobProcess   analysisJobProcess;

  @PostMapping("/api/startJob")
  public void jobProcess(@RequestBody List<Long> clientIds) {
    analysisJobProcess.execute(clientIds);
  }
}
