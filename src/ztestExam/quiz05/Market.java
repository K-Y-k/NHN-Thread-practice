package ztestExam.quiz05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Market {
    // TODO: 필드를 선언하세요.
    static final int MAX_CONSUMER_TO_ENTER = 5;
    final int maxCapacity;
    Map<String, Integer> map = new ConcurrentHashMap<>();
    Semaphore semaphore = new Semaphore(MAX_CONSUMER_TO_ENTER);
    Semaphore consumers = new Semaphore(0);

    // Map<String, Integer> map = new HashMap<>();
    // int maxCapacity;
    // List<String> productList = new ArrayList<>();
    // Consumer currentConsumer;
    // Thread currentThread;

    public Market(Set<String> products, int maxCapacity) {
        // TODO: 인수 검증과 필드 초기화를 하세요.
        if (products == null || products.isEmpty() || maxCapacity <= 0) {
            throw new IllegalArgumentException("Require products is not null and not empty and maxCapacity > 0");
        }

        // for (String productName : products) {
        //     map.put(productName, maxCapacity);
        //     productList.add(productName);
        // }

        this.maxCapacity = maxCapacity;
    }

    public synchronized void storeProduct(String product, int quantity) throws InterruptedException {
        if (this.maxCapacity > map.get(product) + quantity) {
            map.put(product, map.get(product) + quantity);
        }
    }

    public synchronized void storeProduct(String product, int quantity, long waitTime) throws InterruptedException {
        // TODO: 요구 기능을 구현하세요.
        wait(waitTime);

        if (this.maxCapacity > map.get(product) + quantity) {
            map.put(product, map.get(product) + quantity);
        }
    }

    public synchronized boolean buyProduct(String product, int quantity) {
        // TODO: 요구 기능을 구현하세요.
        if (map.get(product) >= quantity) {
            map.put(product, map.get(product) - quantity);
            return true;
        }

        return false;
    }

    public void enterMarket() throws InterruptedException {
        // TODO: 요구 기능을 구현하세요.
        currentConsumer = new Consumer(this);
        currentThread = new Thread(currentConsumer);
        currentThread.start();
    }

    public void leaveMarket() {
        // TODO: 요구 기능을 구현하세요.
        currentThread.interrupt();
    }

    public synchronized int getStock(String product) {
        // TODO: 요구 기능을 구현하세요.
        return map.get(product);
    }
}
