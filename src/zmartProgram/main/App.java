package zmartProgram.main;

import zmartProgram.main.customer.generator.CustomerGenerator;
import zmartProgram.main.mart.entering.EnteringQueue;
import zmartProgram.main.mart.product.parser.ProductParser;
import zmartProgram.main.mart.product.parser.impl.CsvProductParser;
import zmartProgram.main.mart.product.repository.ProductRepository;
import zmartProgram.main.mart.product.repository.impl.MemoryProductRepository;
import zmartProgram.main.mart.product.service.ProductService;
import zmartProgram.main.mart.product.service.impl.ProductServiceImpl;
import zmartProgram.main.mart.thread.ThreadPool;
import zmartProgram.main.mart.thread.customer.CustomerShoppingHandler;
import zmartProgram.main.mart.thread.util.RequestChannel;
import zmartProgram.main.mart.thread.util.RequestHandler;

public class App {
    public static void main(String[] args) {
        // capacity를 100으로 enteringQueue를 초기화 합니다.
        EnteringQueue enteringQueue = new EnteringQueue(100);

        // customerGenerator를 이용해서 thread를 생성합니다.
        CustomerGenerator customerGenerator = new CustomerGenerator(enteringQueue);
        Thread enteringThread = new Thread(customerGenerator);

        // enteringThread의 이름을 'entering-thread'로 설정하고
        // enteringThread를 시작합니다.
        enteringThread.setName("entering-thread");
        enteringThread.start();


        // MemoryProductRepository 구현체를 이용해서 ProductRepository 객체를 생성 합니다.
        ProductRepository productRepository = new MemoryProductRepository();
        
        //CsvProductParser 구현체를 이용해서 ProductParser 객체를 생성 합니다.
        ProductParser productParser = new CsvProductParser();
        
        //ProductServiceImpl 구현체를 이용해서 ProductService 객체를 생성 합니다.
        ProductService productService = new ProductServiceImpl(productRepository,productParser);

        /*
            쇼핑은 동시에 최대 10명까지 할 수 있습니다.
            CartStore에서 cart를 대여 한다.
            cart를 대여 후 쇼핑을 한다.
            쇼핑이 완료되면 계산을 할 수 있도록 대기합니다.
        */

        // checkout 대기열의 queueSize : 20으로 설정합니다.
        RequestChannel checkoutChannel = new RequestChannel(20);

        // shoppingThreadPool, poolSize=10 생성후 실행합니다.
        CustomerShoppingHandler customerRunnable = new CustomerShoppingHandler(enteringQueue,productService, checkoutChannel);
        ThreadPool shoppingThreadPool = new ThreadPool(10, customerRunnable);
        shoppingThreadPool.start();

        // checkout을 하기위한 threadPool을 생성합니다. poolSize = 3
        // 즉 동시에 3군대서 계산을 진행할 수 있습니다.
        RequestHandler requestHandler = new RequestHandler(checkoutChannel);
        ThreadPool checkOutThreadPool = new ThreadPool(3, requestHandler);
        checkOutThreadPool.start();

        // 60초 후 프로그램이 종료 됩니다.
        // enteringThread, shoppingThreadPool, checkOutThreadPool
        try {
            Thread.sleep(60000);

            enteringThread.interrupt();
            shoppingThreadPool.stop();
            checkOutThreadPool.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        // application 실행 후 결과 확인하기
        System.out.println("shopping Application Exit!");
    }
}

