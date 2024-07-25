package com.github.mwacha.infra.analysis.repository;

import com.github.mwacha.domain.analysis.AnalysisResult;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID> {}
