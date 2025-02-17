/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.thread.scc;

import com.nhnacademy.customer.cart.Cart;
import com.nhnacademy.customer.cart.CartItem;
import com.nhnacademy.customer.domain.Customer;
import com.nhnacademy.nhnmart.product.domain.Product;
import com.nhnacademy.nhnmart.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

@Slf4j
class SelfCheckoutRequestTest {

    Customer customer;

    Cart cart;

    ProductService productService;

    SelfCheckoutRequest selfCheckoutRequest;

    @BeforeEach
    void setUp(){

        cart = new Cart();
        //장바구니에 제품 2개를 추가 합니다.
        cart.tryAddItem(new CartItem(1l,1));
        cart.tryAddItem(new CartItem(2l,1));
        productService = Mockito.mock(ProductService.class);
        Mockito.when(productService.getProduct(anyLong())).thenAnswer(new Answer<Product>() {

            int count = 0;

            List<Product> productList = new ArrayList<>(){{
                add(new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,100));
                add(new Product(2l,"주방세제","헨켈","(750㎖) 프릴 베이킹소다 퓨어레몬","개",8900,100));
                add(new Product(3l,"주방세제","LG","(490㎖) 자연퐁POP 솔잎","개",5300,100));
                add(new Product(4l,"키친타올","유한","크리넥스 150매×6","개",8600,100));
                add(new Product(5l,"행주","유한","향균 블루 행주 타올 45매×4","개",10400,100));
                add(new Product(6l,"냅킨","유한","크리넥스 카카오 홈냅킨 130매×6","개",6280,100));
                add(new Product(7l,"호일","대한","대한웰빙호일 25㎝×30m×15","개",3980,100));
                add(new Product(8l,"크린랩","유한","(30매) 크린종이호일 26.7㎝","개",5280,100));
                add(new Product(9l,"랩","크린랲","크린랲 22㎝×50m","개",5480,100));
                add(new Product(10l,"위생봉지","크린랩","(200매입) 크린롤백 30㎝×40㎝","개",8080,100));
            }};

            @Override
            public Product answer(InvocationOnMock invocation) throws Throwable {
                Product product = productList.get(count++);
                log.debug("product:{}",product);
                return product;
            }
        });
    }

    @Test
    void constructorTest(){

        //TODO#9-2-5 SelfCheckoutRequest 생성시 parameter 검증합니다. null 이면 IllegalArgumentException이 발생 합니다.
        //customer, cart, productService를 검증 합니다.
        Assertions.assertAll(
            () -> {
                Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new SelfCheckoutRequest(null,cart,productService);
                });
            },
            () -> {
                Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new SelfCheckoutRequest(customer,null,productService);
                });
            },
            () -> {
                Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new SelfCheckoutRequest(customer,cart,null);
                });
            }
        );
    }

    @Test
    @DisplayName("결제 후 고객의 money 검증")
    void execute1() {
        customer = new Customer(1l,"NHN아카데미1",100_0000);
        selfCheckoutRequest = new SelfCheckoutRequest(customer,cart,productService);

        selfCheckoutRequest.execute();

        //TODO#9-2-5 customer money : 100_0000 - 18800 = 981200 검증 합니다.
        Assertions.assertEquals(981200,customer.getMoney());
    }

    @Test
    @DisplayName("결제 시도 , 고객의 money 부족")
    void execute2(){
        /*
         * 장바구니에 담긴 아이템
         * - 1l - 1개 - 9900원
         * - 2l - 1개 - 8900원
         * - 9900 + 8900 = 18800원
         * */
        Mockito.when(productService.returnProduct(anyLong(),anyInt())).thenReturn(anyInt());

        //고객의 money를 1_0000으로 설정 합니다.
        customer = new Customer(1l,"NHN아카데미1",1_0000);

        selfCheckoutRequest = new SelfCheckoutRequest(customer,cart,productService);
        selfCheckoutRequest.execute();

        //TODO#9-2-7 customer의 money 부족으로 제품을 모두 반납합니다. 현제 cart에 {1l,2l} 제품이 있음으로 productService.returnProduct() 2회 호출 됩니다.
        // Mockito.verify()이용해서 검증합니다.
        Mockito.verify(productService,Mockito.times(2)).returnProduct(anyLong(),anyInt());
    }

    @Test
    @DisplayName("카트에 담긴 제품의 가격 계산")
    void getTotalAmountFromCart() {
        /*
        * 장바구니에 담긴 아이템
        * - 1l - 1개 - 9900원
        * - 2l - 1개 - 8900원
        * - 9900 + 8900 = 18800원
        * */

        customer = new Customer(1l,"NHN아카데미1",1_0000);
        selfCheckoutRequest = new SelfCheckoutRequest(customer,cart,productService);

        int totalAmountFromCart = selfCheckoutRequest.getTotalAmountFromCart();
        Assertions.assertEquals(18800, totalAmountFromCart);
    }
}