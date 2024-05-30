package com.github.mwacha.domain.product;

import jakarta.persistence.Entity;
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
public class Product {
  @Id @UuidGenerator private UUID id;
  private String code;
  private String productName;
  private String description;
}
