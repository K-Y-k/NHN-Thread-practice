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

package com.nhnacademy.thread.customer;

import com.nhnacademy.customer.cart.CartItem;
import com.nhnacademy.customer.domain.Customer;
import com.nhnacademy.nhnmart.entring.EnteringQueue;
import com.nhnacademy.nhnmart.product.domain.Product;
import com.nhnacademy.nhnmart.product.service.ProductService;
import com.nhnacademy.nhnmart.product.service.impl.ProductServiceImpl;
import com.nhnacademy.thread.util.RequestChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

@Slf4j
public class CustomerShoppingTest {

    EnteringQueue enteringQueue;
    ProductService productService;
    RequestChannel checkoutChannel;
    CustomerShoppingHandler customerShoppingHandler;

    @BeforeEach
    void setUp(){
        //CustomerShoppingHandler를 테스트 하기위해서 의존관계에 있는 enteringQueue,productService,checkoutChannel mock를 생성 합니다.
        enteringQueue = Mockito.mock(EnteringQueue.class);
        productService = Mockito.mock(ProductServiceImpl.class);
        checkoutChannel = Mockito.mock(RequestChannel.class);

        customerShoppingHandler = new CustomerShoppingHandler(enteringQueue,productService,checkoutChannel);

        //getTotalCount()호출하면 5가 응답 되도록 설정 합니다., getTotalCount()는 제품의 전체 개수 입니다.
        Mockito.when(productService.getTotalCount()).thenReturn(5l);

        //getProduct() method를 호출하면 호출되는 count의 횟수를 index로 사용하여 productList의 product를 반환 합니다.

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
    @DisplayName("shopping")
    void shopping() throws InvocationTargetException, IllegalAccessException {

        CartLocal.initialize(new Customer(1l,"NHN아카데미1",100_0000));
        CartLocal.getCart().tryAddItem(new CartItem(1l,1));

        Optional<Method> methodOptional = ReflectionUtils.findMethod(CustomerShoppingHandler.class,"shopping");

        if(methodOptional.isEmpty()){
            fail("shopping() not found");
        }

        methodOptional.get().setAccessible(true);
        methodOptional.get().invoke(customerShoppingHandler);

        //쇼핑 후 장바구니에 제품이 담긴다.
        log.debug("cart item size : {} ", CartLocal.getCart().getCartItems().size());

        //TODO#9-1-16 카트에 담긴 제품 수 - CartLocal.getCart().getCartItems().size() > 0 검증 합니다.
        Assertions.assertTrue(CartLocal.getCart().getCartItems().size()>0);

        //제품을 들어서 카트에 담는다. -> 즉 제품의 수량 감소
        Mockito.verify(productService,Mockito.atLeast(1)).pickProduct(anyLong(),anyInt());

    }

    @Test
    @DisplayName("shopping, product-id:1 이미 장바구니에 담겨 있을 때")
    void shopping_prdouctAlreadyExist() throws InvocationTargetException, IllegalAccessException {

        CartLocal.initialize(new Customer(1l,"NHN아카데미1",100_0000));

        /*TODO#9-1-17 product id가 1인 제품을 장바구니에 추가 합니다. 수량은 5로 설정 합니다.
          - new CartItem()
        */
        CartLocal.getCart().tryAddItem(new CartItem(1l,5));

        Optional<Method> methodOptional = ReflectionUtils.findMethod(CustomerShoppingHandler.class,"shopping");

        if(methodOptional.isEmpty()){
            fail("shopping() not found");
        }

        methodOptional.get().setAccessible(true);
        methodOptional.get().invoke(customerShoppingHandler);

        Optional<CartItem> productOptional = CartLocal.getCart().getCartItems().stream()
                .filter(o->o.getProductId()==1l)
                .findFirst();

         //TODO#9-1-18 productOptional.get().getQuantity() 호출 후 수량이 5인지 검증 합니다.
         // - id가 1인 product는 이미 장바구니에 존재하고 있어 추가되지 않습니다.
        Assertions.assertEquals(5,productOptional.get().getQuantity());

        //제품을 들어서 카트에 담는다. -> 즉 제품의 수량 감소, pickProduct()를 최소 1회 이상 실행하는지 검증 합니다.
        Mockito.verify(productService,Mockito.atLeast(1)).pickProduct(anyLong(),anyInt());
    }
}
