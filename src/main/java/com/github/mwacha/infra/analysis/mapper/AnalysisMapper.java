package com.github.mwacha.infra.analysis.mapper;

import com.github.mwacha.domain.analysis.AnalysisResult;
import com.github.mwacha.infra.analysis.client.ResultResponse;

public class AnalysisMapper {
  public static AnalysisResult toAnalysisResult(ResultResponse resultResponse) {
    return AnalysisResult.builder()
        .clientId(resultResponse.clientId())
        .status(resultResponse.status())
        .build();
  }
}
