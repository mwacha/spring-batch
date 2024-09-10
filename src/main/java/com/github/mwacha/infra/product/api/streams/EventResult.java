package com.github.mwacha.infra.product.api.streams;

import com.github.mwacha.domain.product.enums.ImportStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EventResult(UUID id, ImportStatus status) {
}
