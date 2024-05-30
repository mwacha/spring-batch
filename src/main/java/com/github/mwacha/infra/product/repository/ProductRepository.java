package com.github.mwacha.infra.product.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.mwacha.domain.product.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {}
