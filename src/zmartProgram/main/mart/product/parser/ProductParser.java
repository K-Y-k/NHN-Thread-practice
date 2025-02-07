package zmartProgram.main.mart.product.parser;

import java.io.Closeable;
import java.io.InputStream;
import java.util.List;

import zmartProgram.main.mart.product.domain.Product;

public interface ProductParser extends Closeable {
    String PRODUCTS_DATA= "product_data.csv";
    List<Product> parse();
    
    default InputStream getProductsStream(){
        return this.getClass()
                .getClassLoader()
                .getResourceAsStream(PRODUCTS_DATA);
    }
}
