package com.github.mwacha.infra.product.api.consumer;

import com.github.mwacha.application.product.ProductUpdate;
import com.github.mwacha.infra.product.api.streams.EventResult;
import com.github.mwacha.shared.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

  private final ProductUpdate productUpdate;

  @RabbitListener(queues = "${spring.rabbitmq.consumer}")
  public void receiveInventoryUpdate(String message) {
    productUpdate.execute(JsonMapper.toObject(message, EventResult.class));
  }
}
