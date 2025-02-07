package zmartProgram.main.mart.product.service;

import zmartProgram.main.mart.product.domain.Product;

public interface ProductService {
    // product 조회
    Product getProduct(long id);

    // product 등록 후 product id를 응답
    void saveProduct(Product product);

    // product 삭제
    void deleteProduct(long id);

    // 전체 상품의 개수
    long getTotalCount();

    //제품의 수량을 수정
    void updateQuantity(long id, int quantity);

    // 제품을 들어서 카트에 담을 때 해당 제품의 수량을 차감
    void pickProduct(long id, int buyQuantity);

    // 제품을 반납 합니다. 반납된 수량을 더한 수량 반환
    int returnProduct(long id, int returnQuantity);
}
