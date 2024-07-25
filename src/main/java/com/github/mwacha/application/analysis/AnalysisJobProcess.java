package com.github.mwacha.application.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisJobProcess {

  private final JobLauncher jobLauncher;

  @Qualifier("analysisJob")
  private final Job analysisJob;

  public void execute(final List<Long> clientIds) {
    int batchSize =
        10; // Adjust this value according to the maximum length allowed for the parameter string
    final var batches = partition(clientIds, batchSize);

    for (List<Long> batch : batches) {
      String clientIdsStr = batch.stream().map(Object::toString).collect(Collectors.joining(","));

      JobParameters jobParameters =
          new JobParametersBuilder().addString("clientIds", clientIdsStr).toJobParameters();

      try {
        jobLauncher.run(analysisJob, jobParameters);
      } catch (JobInstanceAlreadyCompleteException
          | JobExecutionAlreadyRunningException
          | JobRestartException
          | JobParametersInvalidException e) {
        throw new RuntimeException("A job instance already exists and is complete");
      }
    }
  }

  private static <T> List<List<T>> partition(List<T> list, int size) {
    List<List<T>> partitions = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
      partitions.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
    }
    return partitions;
  }
}
