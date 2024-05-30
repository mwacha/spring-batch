package com.github.mwacha.infra.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.mwacha.domain.analysis.AnalysisResult;
import java.util.UUID;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID> {}
