package com.github.mwacha.infra.product.repository;

import com.github.mwacha.domain.product.ImportProduct;
import com.github.mwacha.domain.product.enums.ImportStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImportProductRepository extends JpaRepository<ImportProduct, UUID> {
  @Modifying
  @Query(value = "update import_product  set status = :status where id = :id", nativeQuery = true)
  void updateStatus(UUID id, ImportStatus status);
}
