package com.huaxia.atlas.domain.product;

import com.huaxia.atlas.domain.product.dto.ProductForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Page<Product> list(int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return repo.findAll(pageable);
    }

    public Optional<Product> get(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Product create(ProductForm form) {
        Product product = new Product();
        applyForm(form, product);
        return repo.save(product);
    }

    @Transactional
    public Optional<Product> update(Long id, ProductForm form) {
        return repo.findById(id).map(existing -> {
            applyForm(form, existing);
            return repo.save(existing);
        });
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private void applyForm(ProductForm form, Product product) {
        product.setName(form.getName().trim());
        product.setDescription(blankToNull(form.getDescription()));
        product.setPrice(form.getPrice());
        product.setImageUrl(blankToNull(form.getImageUrl()));
        product.setStock(form.getStock());
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
