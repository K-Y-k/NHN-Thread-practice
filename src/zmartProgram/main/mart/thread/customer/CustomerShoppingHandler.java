package zmartProgram.main.mart.thread.customer;

import java.util.Random;

import zmartProgram.main.customer.domain.Cart;
import zmartProgram.main.customer.domain.CartItem;
import zmartProgram.main.customer.domain.Customer;
import zmartProgram.main.mart.entering.EnteringQueue;
import zmartProgram.main.mart.product.domain.Product;
import zmartProgram.main.mart.product.service.ProductService;
import zmartProgram.main.mart.thread.pay.SelfCheckoutRequest;
import zmartProgram.main.mart.thread.util.RequestChannel;

/**
 * 고객(Customer)이 쇼핑을 합니다. 
 * 쇼핑이 완료된 고객은 checkoutChannel을 통해서 결제 대기열에 등록합니다.
 */
public class CustomerShoppingHandler implements Runnable {

    // 마트 입장 대기열 Queue
    private final EnteringQueue enteringQueue;

    // 제품 관련 service
    private final ProductService productService;

    // random generator
    // https://docs.oracle.com/en/java/javase/21/docs//api/java.base/java/util/random/RandomGenerator.html
    private final RandomGenerator generator;

    // 고객이 쇼핑 완료 후 결제를 위한 대기 channel
    private final RequestChannel checkoutChannel;

    public CustomerShoppingHandler(EnteringQueue enteringQueue, ProductService productService, RequestChannel checkoutChannel) {

        // enteringQueue, productService, checkoutChannel null 이면 IllegalArgumentException 발생
        if (enteringQueue == null || productService == null || checkoutChannel == null) {
            throw new IllegalArgumentException();
        }

        // enteringQueue, productService,checkoutChannel,generator 초기화 합니다.
        this.enteringQueue = enteringQueue;
        this.productService = productService;
        this.checkoutChannel = checkoutChannel;
        this.generator = new Random();
    }


    @Override
    public void run() {

        while(!Thread.currentThread().isInterrupted()){
            try {

                /* enteringQueue 대기열에 있는 고객이 마트에 입장합니다.
                   CartManager.initialize를 호출해서 
                   해당 Thread내에서 Customer(고객), Cart(장바구니) 공유될 수 있도록 설정합니다.
                 */

                //enteringQueue(입장 대기열) 부터 입장시킬 고객 얻기
                Customer customer = enteringQueue.getCustomer();

                //CartLocal CartLocal.initialize() 호출하여 초기화 합니다.
                CartLocal.initialize(customer);


                //TODO#9-1-4 1~10초 랜덤하게 sleep 합니다. s값을 구현합니다.
                int s = generator.nextInt(1, 11);
                Thread.sleep(s * 1000);

                // 쇼핑 시작
                shopping();

                // 쇼핑 후 결제 대기열 등록
                joinCheckoutChannel();

            } catch (InterruptedException ie){
                Thread.currentThread().interrupt();
            } catch (Exception e){
                System.out.printf("shopping : %s\n", e.getMessage(), e);
            } finally {
                // 해당 Thread는 checkoutChannel(결제 대기열)에 등록 후 
                // CartLocal.reset() 호출하여 customerLocal, cartLocal 초기화
                CartLocal.reset();
            }
        }
    }

    private void shopping(){
        /* shopping method 구현
            - 제품 : getProductIdByRand()
            - 쇼핑 횟수 : getShoppingCountByRand()
            - 장바구에 담을 제품의 수량 : getBuyCountByRand()

            제품을 장바구니에 추가합니다.
        */

        //CartLocal 부터 고객의 장바구니 얻기
        Cart cart = CartLocal.getCart();

        //쇼핑 횟수(랜덤) 만큼 쇼핑 합니다.
        for (int i = 0; i < getShoppingCountByRand(); i++) {

            long productId = getProductIdByRand();
            Product product = productService.getProduct(productId);
            int buyCount = getBuyCountByRand();

            //구매 수량보다 제품의 수량이 부족하다면 해당 제품은 카트에 담지 않습니다, if 조건을 변경하세요.
            if (buyCount > product.getQuantity()) {
                continue;
            }

            //장바구니에 cartItem을 추가 합니다. productService.pickProduct() 호출 후 추가한 제품의 수량을 감산 합니다.
            //추가하는 과정에서 Exception이 발생하면 log.debug()를 이용해서 로그를 작성 합니다.
            CartItem cartItem = new CartItem(productId, buyCount);

            try {
                cart.tryAddItem(cartItem);
                productService.pickProduct(productId, buyCount);
            } catch (Exception e){
                System.out.printf("장바구니 Error : %s\n", e.getMessage(), e);
            }
        }

    }

    public void joinCheckoutChannel(){
        /*
            쇼핑 후 결제 대기열 등록
            - checkoutChannel을 이용해서 SelfCheckoutRequest 요청을 등록합니다.
         */
        Cart cart = CartLocal.getCart();
        Cart cloneCart = SerializationUtils.clone(cart);
        Customer customer = CartLocal.getCustomer();

        checkoutChannel.addRequest(new SelfCheckoutRequest(customer, cloneCart, productService));    
    }

    private int getBuyCountByRand(){
        // 장바구니에 담는 제품의 수량 1-5 random 숫자 반환
        return generator.nextInt(1, 6);
    }

    private int getShoppingCountByRand(){
        // 장바구니에 담는 제품의 개수 1-10 random 숫자 반환
        return generator.nextInt(1, 11);
    }

    private long getProductIdByRand(){
        // 쇼핑할 제품의 id - 1 ~ productService.gettotalCount() 범위의 random 숫자 반환
        return generator.nextLong(1l, productService.getTotalCount() + 1);
    }

}
