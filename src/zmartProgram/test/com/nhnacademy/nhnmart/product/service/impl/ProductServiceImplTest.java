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

package com.nhnacademy.nhnmart.product.service.impl;

import com.nhnacademy.nhnmart.product.exception.OutOfStockException;
import com.nhnacademy.nhnmart.product.exception.ProductAlreadyExistsException;
import com.nhnacademy.nhnmart.product.domain.Product;
import com.nhnacademy.nhnmart.product.exception.ProductNotFoundException;
import com.nhnacademy.nhnmart.product.parser.ProductParser;
import com.nhnacademy.nhnmart.product.repository.ProductRepository;
import com.nhnacademy.nhnmart.product.service.ProductService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceImplTest {
    ProductService productService;
    ProductRepository productRepository;
    ProductParser productParser;

    @BeforeEach
    void beforeAllSetUp(){
        productRepository = Mockito.mock(ProductRepository.class);
        productParser = Mockito.mock(ProductParser.class);
        Mockito.when(productParser.parse()).thenReturn(Collections.emptyList());
        productService = new ProductServiceImpl(productRepository,productParser);
    }

    @Test
    @Order(1)
    @DisplayName("instance of ProductService")
    void constructorTest1(){
        //productParser가 ProductService.class의 구현체 인지 검증 합니다.
        Assertions.assertInstanceOf(ProductService.class, productService);
    }

    @Test
    @Order(2)
    @DisplayName("parameter null check")
    void constructorTest2(){
        //ProductServiceImpl 생성할 때 parameter {productRepository, productParser} 가 null 이면 IllegalArgumentException 이 발생 하는지 검증 합니다.

        Assertions.assertAll(
            ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                new ProductServiceImpl(null, productParser);
            }),
            ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
               new ProductServiceImpl(productRepository,null);
            })
        );
    }

    @Test
    @Order(3)
    @DisplayName("product 조회")
    void getProduct() {

        Product excepted = new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,100);
        // productRepository가 mock 객체 임으로  findById(1l) 호출시 excepted 반환됨을 가정 합니다.
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(excepted));

        Product actual = productService.getProduct(1l);

        //excepted 와 actual 일치하는지 검증 합니다.
        Assertions.assertEquals(excepted,actual);
    }

    @Test
    @Order(4)
    @DisplayName("product 조회 - 재품이 존재하지 않을 때")
    void getProduct_notFound() {

        //productRepository.findById(1l) 호출하면 Optional.empty() 반환됨을 가정 합니다.
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        //id-> 1 제품이 존재하지 않다면 ProductNotFoundException 발생하는지 검증 합니다.
        Assertions.assertThrows(ProductNotFoundException.class,()->{
            Product actual = productService.getProduct(1l);
        });
    }

    @Test
    @Order(5)
    @DisplayName("제품 등록")
    void saveProduct() {
        Mockito.when(productRepository.existById(anyLong())).thenReturn(false);
        Mockito.doNothing().when(productRepository).save(any(Product.class));

        Product product = new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,100);
        productService.saveProduct(product);

        //productService.saveProduct(product)를 호출하면 productRepository.existById(1l) 1회 호출되었는지 검증 합니다.
        Mockito.verify(productRepository, Mockito.times(1)).existById(anyLong());

        //productService.saveProduct(product)를 호출하면 productRepository.save(product)가  1회 호출 되었는지 검증하는 코드를 작성하세요
        Mockito.verify(productRepository, Mockito.times(1) ).save(any());
    }

    @Test
    @Order(6)
    @DisplayName("이미 제품이 등록되어 있다면")
    void saveProduct_ProductAlreadyExistsException(){
        Mockito.doNothing().when(productRepository).save(any(Product.class));

        //productRepository.existById()를 호출하면 true 반환 되도록 코드를 작성합니다.
        Mockito.when(productRepository.existById(anyLong())).thenReturn(true);

        Product product = new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,100);

        Assertions.assertThrows(ProductAlreadyExistsException.class,()->{
            productService.saveProduct(product);
        });

        Mockito.verify(productRepository, Mockito.times(1) ).existById(anyLong());
    }

    @Test
    @Order(7)
    @DisplayName("제품삭제")
    void deleteProduct() {
        Mockito.when(productRepository.existById(anyLong())).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteProduct(1l);

        Mockito.verify(productRepository,Mockito.times(1)).existById(anyLong());
        Mockito.verify(productRepository,Mockito.times(1)).deleteById(anyLong());

    }

    @Test
    @Order(8)
    @DisplayName("전체 제품의 수")
    void getTotalCount() {
        Mockito.when(productRepository.count()).thenReturn(10L);
        long actual = productService.getTotalCount();
        Assertions.assertEquals(10l, actual);
        Mockito.verify(productRepository,Mockito.times(1)).count();
    }

    @Test
    @Order(9)
    @DisplayName("제품 수량 변경")
    void updateQuantity() {
        Mockito.doNothing().when(productRepository).updateQuantityById(anyLong(),anyInt());
        Mockito.when(productRepository.existById(anyLong())).thenReturn(true);

        productService.updateQuantity(1l,50);

        Mockito.verify(productRepository,Mockito.times(1)).updateQuantityById(anyLong(),anyInt());
        Mockito.verify(productRepository,Mockito.times(1)).existById(anyLong());
    }

    @Test
    @Order(10)
    @DisplayName("제품 수량 변경 - 제품이 존재하지 않을 떄")
    void updateQuantity_ProductNotFoundException(){
        Mockito.when(productRepository.existById(anyLong())).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class,()->{
            productService.updateQuantity(1l,50);
        });

        Mockito.verify(productRepository,Mockito.times(1)).existById(anyLong());
    }

    @Test
    @Order(11)
    @DisplayName("제품을 장바구니에 담을 떄 : 제품의 수량 감소")
    void pickProduct() {

        Product product = new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,5);

        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).updateQuantityById(anyLong(),anyInt());
        Mockito.when(productRepository.existById(anyLong())).thenReturn(true);

        productService.pickProduct(1l,2);

        Mockito.verify(productRepository,Mockito.times(1)).updateQuantityById(anyLong(),anyInt());
    }

    @Test
    @Order(12)
    @DisplayName("제품을 장바구니에 담을 때 : 제품의 수량이 부족할 떄")
    void pickProduct_OutOfStockException(){
        Product product = new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,5);

        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.existById(anyLong())).thenReturn(true);

        Assertions.assertThrows(OutOfStockException.class,()->{
            //수량이 5개 남은 상황에서 10개를 장바구니에 담으려고 시도.
            productService.pickProduct(1l,10);
        });
    }

    @Test
    @Order(11)
    @DisplayName("장바구니에 담긴 제품을 매대에 반납 합니다.")
    void returnProduct() {

        Product product = new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,5);
        Mockito.when(productRepository.existById(anyLong())).thenReturn(true);
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).updateQuantityById(anyLong(),anyInt());

        //productService.returnProduct() 호출하여 매대에 제품을 반납 합니다. 반납된 제품의 수량이 정확히 계산 되었는지 검증하는 코드를 작성하세요
        int actual = productService.returnProduct(1l,10);
        Assertions.assertEquals(15,actual);

    }
}