package zmartProgram.main.mart.product.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(long productId) {
        super(String.format("product already exists : {}",productId));
    }
}
