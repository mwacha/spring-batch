package tk.mwacha.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import tk.mwacha.entity.Product;
import tk.mwacha.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductItemWriter implements ItemWriter<Product> {

    private final ProductRepository repository;


    @Override
    public void write(Chunk<? extends Product> chunk) throws Exception {
        System.out.println("Writer Thread "+Thread.currentThread().getName());
        repository.saveAll(chunk);
    }
}
