package com.github.mwacha.infra.product.job;

import com.github.mwacha.process.AbstractItemProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.item.ItemProcessor;
import com.github.mwacha.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

import static com.github.mwacha.shared.JsonMapper.toJson;

@Slf4j
public class ImportProductItemProcessor extends AbstractItemProcessor<Product, Product> {
  @Autowired
  private  RabbitTemplate rabbitTemplate;
 @Value("${spring.rabbitmq.producer}")
  private  String producerQueueName;


  @Override
  public Product process(final Product product) {
    final var code = product.getCode().toUpperCase();
    final var productName = product.getProductName().toUpperCase();
    final var description = product.getDescription().toUpperCase();
    final var importProductId = product.getImportProductId();

    final var transformedProduct =
        Product.builder().id(UUID.randomUUID()).code(code).importProductId(importProductId).productName(productName).description(description).build();

    rabbitTemplate.convertAndSend(producerQueueName, toJson(transformedProduct));

    return transformedProduct;
  }
}
