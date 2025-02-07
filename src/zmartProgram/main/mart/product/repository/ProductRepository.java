package zmartProgram.main.mart.product.repository;

import java.util.Optional;

import zmartProgram.main.mart.product.domain.Product;

public interface ProductRepository {
    // 등록
    void save(Product product);

    // 조회
    Optional<Product> findById(long id);

    // 삭제
    void deleteById(long id);

    // product 존재여부 체크
    boolean existById(long id);

    // prdocut 전체 count;
    long count();

    int countQuantityById(long id);

    void updateQuantityById(long id, int quantity);
}