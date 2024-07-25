package com.github.mwacha.domain.product;

import com.github.mwacha.domain.product.enums.ImportStatus;
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
public class ImportProduct {
  @Id @UuidGenerator private UUID id;

  @Enumerated(EnumType.STRING)
  private ImportStatus status;
}
