package zmartProgram.main.mart.product.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * project Id를 생성하는 util
 */
public final class ProductIdGenerator {
    // idGenerator를 0으로 초기화 합니다.
    private final static AtomicLong idGenerator = new AtomicLong(0);

    public static long getNewId(){
        // idGenerator를 1증가시키고 반환합니다.
        return idGenerator.incrementAndGet();
    }
}
