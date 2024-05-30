package com.github.mwacha.domain.analysis;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Charge {
  @Id @UuidGenerator private UUID id;


  private Long clientId;
  private LocalDate dueDate;
  private Long negotiationNumber;
  private BigDecimal fee;
  private BigDecimal totalAmount;
  @Builder.Default private boolean active = true;

}
