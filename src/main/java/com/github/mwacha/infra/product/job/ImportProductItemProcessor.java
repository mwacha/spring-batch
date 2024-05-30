package com.github.mwacha.infra.product.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import com.github.mwacha.domain.product.Product;

@Slf4j
public class ImportProductItemProcessor implements ItemProcessor<Product, Product> {

  // To simulate any process. In this case, we convert the names to uppercase
  @Override
  public Product process(final Product product) {
    final var code = product.getCode().toUpperCase();
    final var productName = product.getProductName().toUpperCase();
    final var description = product.getDescription().toUpperCase();

    final var transformedProduct =
        Product.builder().code(code).productName(productName).description(description).build();

    log.info("Converting ( {} ) into ( {} )", product, transformedProduct);

    return transformedProduct;
  }
}
