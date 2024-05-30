package com.github.mwacha.infra.analysis.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "externalApi", url = "http://localhost:8091")
public interface ExternalApiClient {

    @PostMapping(path = "/results")
    List<ResultResponse> getEndpointData(List<Long> ids);
}