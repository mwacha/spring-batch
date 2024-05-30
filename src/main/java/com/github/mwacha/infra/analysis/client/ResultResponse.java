package com.github.mwacha.infra.analysis.client;

import com.github.mwacha.domain.analysis.enums.AnalysisStatus;

import java.util.UUID;

public record ResultResponse(Long clientId, AnalysisStatus status){
}
