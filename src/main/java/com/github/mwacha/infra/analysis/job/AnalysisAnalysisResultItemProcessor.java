package com.github.mwacha.infra.analysis.job;

import com.github.mwacha.domain.analysis.AnalysisResult;
import com.github.mwacha.domain.analysis.Charge;
import com.github.mwacha.infra.analysis.client.ExternalApiClient;
import com.github.mwacha.infra.analysis.client.ResultResponse;
import com.github.mwacha.process.AbstractItemProcessor;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnalysisAnalysisResultItemProcessor
    extends AbstractItemProcessor<List<Charge>, List<AnalysisResult>> {

  @Autowired private ExternalApiClient externalApiClient;

  @Override
  public List<AnalysisResult> process(List<Charge> items) throws Exception {
    final List<Long> ids = items.stream().map(Charge::getClientId).collect(Collectors.toList());
    final List<ResultResponse> clientCall = externalApiClient.getEndpointData(ids);

    return clientCall.stream()
        .map(
            item ->
                AnalysisResult.builder().clientId(item.clientId()).status(item.status()).build())
        .collect(Collectors.toList());
  }
}
