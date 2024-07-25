package com.github.mwacha.infra.product.repository;

import com.github.mwacha.domain.product.Product;
import com.github.mwacha.domain.product.enums.ImportStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  @Modifying
  @Query(value = "update product  set status = :status where id = :id", nativeQuery = true)
  void updateStatus(UUID id, ImportStatus status);
}
