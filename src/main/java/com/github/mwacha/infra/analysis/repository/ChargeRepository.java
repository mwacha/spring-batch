package com.github.mwacha.infra.analysis.repository;

import com.github.mwacha.domain.analysis.Charge;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, UUID> {
  Optional<Charge> findByClientId(Long clientId);
}
