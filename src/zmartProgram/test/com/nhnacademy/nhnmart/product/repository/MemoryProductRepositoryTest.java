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

package com.nhnacademy.nhnmart.product.repository;

import com.nhnacademy.nhnmart.product.domain.Product;
import com.nhnacademy.nhnmart.product.repository.impl.MemoryProductRepository;
import org.junit.jupiter.api.*;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemoryProductRepositoryTest {

    private static ProductRepository productRepository;

    @BeforeAll
    static void beforeAllSetUp() {
        productRepository = new MemoryProductRepository();
        productRepository.save(new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,100));
    }

    @Test
    @Order(1)
    @DisplayName("product 등록")
    void save() {
        Product actual = new Product(2l,"주방세제","헨켈","(750㎖) 프릴 베이킹소다 퓨어레몬","개",8900,100);
        productRepository.save(actual);

        //2l 해당되는 product가 정상 등록되었는지 검증 합니다.
        Optional<Product> exceptedOptional = productRepository.findById(2l);
        Assertions.assertEquals(exceptedOptional.get(),actual);
    }

    @Test
    @Order(2)
    @DisplayName("1l -> product 조회")
    void findById() {
        Optional<Product> actualOptional = productRepository.findById(1l);
        Assertions.assertAll(
            //1l 해당되는 product의 attribute를 검증 합니다.
            ()->Assertions.assertEquals(1l,actualOptional.get().getId()),
            ()->Assertions.assertEquals("주방세제",actualOptional.get().getItem()),
            ()->Assertions.assertEquals("LG",actualOptional.get().getMaker()),
            ()->Assertions.assertEquals("(750㎖) 자연퐁 스팀워시 레몬",actualOptional.get().getSpecification()),
            ()->Assertions.assertEquals("개",actualOptional.get().getUnit()),
            ()->Assertions.assertEquals(9900,actualOptional.get().getPrice()),
            ()->Assertions.assertEquals(100,actualOptional.get().getQuantity())
        );
    }

    @Test
    @Order(3)
    @DisplayName("id:2 -> 삭제")
    void deleteById() {
        //id : 2l 인 product 를 삭제하고 정상처리 되었는지 검증 합니다.

        productRepository.deleteById(2l);
        Assertions.assertFalse(productRepository.existById(2l));
    }

    @Test
    @Order(4)
    @DisplayName("product 존재여부 체크")
    void existById() {
        //existById() 이용해서 제품 존재여부를 체크할 수 있도록 검증합니다.
        Assertions.assertAll(
                ()->Assertions.assertTrue(productRepository.existById(1l)),
                ()->Assertions.assertFalse(productRepository.existById(2l))
        );
    }

    @Test
    @Order(5)
    @DisplayName("productRepository에 등록된 전체 product count")
    void count() {
        //count() 검증, productRepository에 등록된 전체 제품 수
        Assertions.assertEquals(1l, productRepository.count());
    }

    @Test
    @Order(6)
    @DisplayName("특정 product의 수량")
    void countQuantityById() {
        //countQuantityById() 검증, id:1 에 해당되는 제품수량 검증
        Assertions.assertEquals(100,productRepository.countQuantityById(1l));
    }

    @Test
    @Order(7)
    @DisplayName("product 수량 변경")
    void updateQuantityById() {
        //id:1 에 해당되는 product의 수량을 변경하고 변경된 결과가 반영 되었는지 검증 합니다.

        productRepository.updateQuantityById(1l,50);
        Assertions.assertEquals(50,productRepository.countQuantityById(1l));
    }
}