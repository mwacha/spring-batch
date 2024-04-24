package tk.mwacha.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import tk.mwacha.entity.Product;

@Slf4j
public class ProductItemProcessor implements ItemProcessor<Product, Product> {


    @Override
    public Product process(final Product product) {
        final var code = product.getCode().toUpperCase();
        final var productName = product.getProductName().toUpperCase();
        final var description = product.getDescription().toUpperCase();

        final var transformedProduct = Product.builder()
                .code(code).productName(productName)
                .description(description).build();

        log.info("Converting ( {} ) into ( {} )", product, transformedProduct);

        return transformedProduct;
    }
}
