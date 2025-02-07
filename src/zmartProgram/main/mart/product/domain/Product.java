package zmartProgram.main.mart.product.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * mart에서 판매되는 제품
 */
public class Product {

    // product-id
    private final long id;

    // 상품명
    private final String item;

    // 제조사
    private final String maker;

    // 스펙
    private final String specification;

    // 단위
    private final String unit;

    // 가격
    private final int price;

    // 수량
    private int quantity;


    public Product(long id, String item, String maker, String specification, String unit, int price, int quantity) {
        // product 생성자의 parameter 검증을 통과하지 못한다면 IllegalArgumentException이 발생됩니다.
        if( id< 0 || price < 0 || quantity < 0 || StringUtils.isEmpty(item) || StringUtils.isEmpty(maker) || StringUtils.isEmpty(specification) || StringUtils.isEmpty(unit) ){
            throw new IllegalArgumentException();
        }

        // product attribute를 초기화 합니다.
        this.id = id;
        this.item = item;
        this.maker = maker;
        this.specification = specification;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        // product id 반환
        return id;
    }

    public String getItem() {
        // item 반환
        return item;
    }

    public String getMaker() {
        // maker 반환
        return maker;
    }

    public String getSpecification() {
        // specification 반환
        return specification;
    }

    public String getUnit() {
        // unit 반환
        return unit;
    }

    public int getPrice() {
        // price 반환
        return price;
    }

    public int getQuantity() {
        // quantity 반환
        return quantity;
    }

    public void setQuantity(int quantity) {
        // qunatity 수정, quantity < 0 이면 IllegalArgumentException 발생
        if(quantity < 0 ){
            throw new IllegalArgumentException("quantity >=0");
        }
        this.quantity = quantity;
    }

    // Product의 모든 필드를 기준으로 equals 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && price == product.price && quantity == product.quantity && Objects.equals(item, product.item) && Objects.equals(maker, product.maker) && Objects.equals(specification, product.specification) && Objects.equals(unit, product.unit);
    }

    // Product의 모든 필드를 기준으로 hashCode를 구현
    @Override
    public int hashCode() {
        return Objects.hash(id, item, maker, specification, unit, price, quantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", item='" + item + '\'' +
                ", maker='" + maker + '\'' +
                ", specification='" + specification + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}

