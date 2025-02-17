package ztestExam.quiz05;

import java.util.Random;
import java.util.random.RandomGenerator;

public class Producer implements Runnable {
    // TODO: 필드를 선언하세요.
    private Market market;
    private long waitTime;
    private RandomGenerator generator = new Random();

    public Producer(Market market, long maxWaitTime) {
        // TODO: 인수 검증과 필드 초기화를 하세요.
        if (market == null || maxWaitTime <= 0) {
            throw new IllegalArgumentException("Require market is not null and maxWaitTime > 0");
        }

        this.market = market;
        this.waitTime = maxWaitTime;
    }

    @Override
    public void run() {
        // TODO: 요구 기능을 구현하세요.
        try {
            while (true) {
                int randomIndex = generator.nextInt(0, market.productList.size());
                String findProduct = market.productList.get(randomIndex);

                boolean isTimeOut = false;
                long startTime = System.currentTimeMillis();

                while (!isTimeOut) {
                    long currentTime = System.currentTimeMillis();
                    long diffTime = currentTime - startTime;

                    if (market.map.get(findProduct) + 1 <= market.maxCapacity) {
                        market.storeProduct(findProduct, 1);
                        break;
                    }

                    if (diffTime <= 3000) {
                        isTimeOut = true;
                    } else {
                        return;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}