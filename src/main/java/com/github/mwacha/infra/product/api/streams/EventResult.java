package com.github.mwacha.infra.product.api.streams;

import com.github.mwacha.domain.product.enums.ImportStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record EventResult(UUID id, ImportStatus status) {}
