package com.github.mwacha.infra.analysis.job;

import com.github.mwacha.domain.analysis.AnalysisResult;
import com.github.mwacha.infra.analysis.repository.AnalysisResultRepository;
import com.github.mwacha.process.AbstractItemWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalysisAnalysisResultItemWriter extends AbstractItemWriter<List<AnalysisResult>> {

  private final AnalysisResultRepository repository;

  @Override
  public void write(Chunk<? extends List<AnalysisResult>> chunk) throws Exception {
    log.info("Writer Thread {}", Thread.currentThread().getName());
    repository.saveAll(chunk.getItems().get(0));
  }
}
