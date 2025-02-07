package zmartProgram.main.mart.product.repository.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import zmartProgram.main.mart.product.domain.Product;
import zmartProgram.main.mart.product.repository.ProductRepository;

public class MemoryProductRepository implements ProductRepository {
    /*
        MemoryProductRepository를 구현합니다.
        - List, Map, Set 등등 memory 기반의 저장소를 사용하여 구현합니다.
        - List, Map, Set의 구현체는 multi thread 환경에서 Thread Safety 해야 합니다.
        - MemoryProductRepository는 파싱한 product 객체를 저장소이며 product 데이터 관련된 처리를 합니다.
    */

    private final ConcurrentMap<Long, Product> productConcurrentMap = new ConcurrentHashMap<>();

    @Override
    public void save(Product product) {
        // product 저장
        productConcurrentMap.put(product.getId(),product);
    }

    @Override
    public Optional<Product> findById(long id) {
        // id에 해당되는 product 조회
        Product product = productConcurrentMap.get(id);
        return Objects.isNull(product) ? Optional.empty() : Optional.of(product);
    }

    @Override
    public void deleteById(long id) {
        // id에 해당하는 product 삭제
        productConcurrentMap.remove(id);
    }

    @Override
    public boolean existById(long id) {
        // id에 해당하는 product 존재여부를 체크해서 반환
        return productConcurrentMap.containsKey(id);
    }

    @Override
    public long count() {
        // 전체 product 수 반환
        return productConcurrentMap.size();
    }

    @Override
    public int countQuantityById(long id) {
        // id에 해당되는 product의 수량 반환(즉 제고 확인)
        Product product = productConcurrentMap.get(id);
        return product.getQuantity();
    }

    @Override
    public void updateQuantityById(long id, int quantity) {
        // id에 해당되는 product의 수량 변경
        Product product = productConcurrentMap.get(id);
        product.setQuantity(quantity);
    }
}
