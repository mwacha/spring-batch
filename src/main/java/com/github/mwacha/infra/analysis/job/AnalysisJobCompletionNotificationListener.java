package com.github.mwacha.infra.analysis.job;

import com.github.mwacha.listener.JobNotificationListener;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@JobScope
public class AnalysisJobCompletionNotificationListener extends JobNotificationListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    // Retrieve parameter value from JobParameters
    final var params = jobExecution.getJobParameters().getString("clientIds");

    final var clientIds =
        Arrays.stream(params.split(",")).map(Long::valueOf).collect(Collectors.toList());

    // stores the parameter value in the ExecutionContext
    jobExecution.getExecutionContext().put("clientIds", clientIds);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! API JOB FINISHED! Time to verify the results");
    }
  }
}
