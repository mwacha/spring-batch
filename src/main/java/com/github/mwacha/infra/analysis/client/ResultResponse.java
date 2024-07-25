package com.github.mwacha.infra.analysis.client;

import com.github.mwacha.domain.analysis.enums.AnalysisStatus;

public record ResultResponse(Long clientId, AnalysisStatus status) {}
