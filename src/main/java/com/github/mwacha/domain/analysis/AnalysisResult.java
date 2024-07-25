package com.github.mwacha.domain.analysis;

import com.github.mwacha.domain.analysis.enums.AnalysisStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
  @Id @UuidGenerator private UUID id;
  private Long clientId;

  @Enumerated(EnumType.STRING)
  private AnalysisStatus status;
}
