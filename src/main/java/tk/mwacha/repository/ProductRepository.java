package tk.mwacha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tk.mwacha.entity.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}