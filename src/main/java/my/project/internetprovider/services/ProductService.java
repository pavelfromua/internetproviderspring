package my.project.internetprovider.services;

import my.project.internetprovider.models.Product;
import my.project.internetprovider.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Product productFromDb = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id " + id + " does not exists"));

        return productFromDb;
    }

    @Transactional
    public void updateProduct(Product product) {
        Product productFromDb = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "product with id " + product.getId() + " does not exists"));

        productFromDb.setName(product.getName());

        productRepository.save(productFromDb);
    }

    public void deleteProduct(Long id) {
        boolean isExists = productRepository.existsById(id);

        if (!isExists)
            throw new IllegalStateException("product with id " + id
                    + " does not exists");

        productRepository.deleteById(id);
    }
}
