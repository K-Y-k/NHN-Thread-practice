package zmartProgram.main.mart.product.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(long productId) {
        super( String.format("product : %d -  재고 부족", productId ));
    }
}
