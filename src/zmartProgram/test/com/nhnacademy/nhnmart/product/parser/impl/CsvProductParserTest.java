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

package com.nhnacademy.nhnmart.product.parser.impl;

import com.nhnacademy.nhnmart.product.domain.Product;
import com.nhnacademy.nhnmart.product.parser.ProductParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CsvProductParserTest {
    public static ProductParser productParser;
    @BeforeAll
    static void beforeSetUp(){
        //@BeforeAll은 CsvProductParserTest에서 테스트 시작전 한 번 실행 됩니다.
        //CsvProductParser 객체를 생성 합니다.
        productParser = new CsvProductParser();
    }
    @AfterAll
    static void tearDown() throws IOException {
        //@AfterAll은 CsvProductParserTest 테스트 종료 시점에 한 번 실행 됩니다.
        //CsvProductParserTest 종료되면 productParser.close()를 호출하여 자원을 해지 합니다.
        if(Objects.nonNull(productParser)){
            productParser.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("instance of ProductParser")
    void constructorTest1(){
        Assertions.assertInstanceOf(ProductParser.class,productParser);
    }

    @Test
    @Order(2)
    @DisplayName("inputStream is null")
    void constructorTest2(){
        //CsvProductParser 객체를 생성시 inputstream == null 이면 IllegalArgumentException이 발생하는지 검증 합니다.
        Assertions.assertThrows(IllegalArgumentException.class,()->{
           new CsvProductParser(null);
        });
    }

    @Test
    @Order(3)
    @DisplayName("/resources/product_data.csv 존재 하는지 체크")
    void getStreamTest(){
        InputStream inputStream = productParser.getProductsStream();
        Assertions.assertNotNull(inputStream);
    }

    @Test
    @Order(4)
    @DisplayName("parsing from /test/resources/product_data.csv")
    void parse() {

        List<Product> excepted = new ArrayList<>(5);
        excepted.add(new Product(1l,"주방세제","LG","(750㎖) 자연퐁 스팀워시 레몬","개",9900,100));
        excepted.add(new Product(2l,"주방세제","헨켈","(750㎖) 프릴 베이킹소다 퓨어레몬","개",8900,100));
        excepted.add(new Product(3l,"주방세제","LG","(490㎖) 자연퐁POP 솔잎","개",5300,100));
        excepted.add(new Product(4l,"키친타올","유한","크리넥스 150매×6","개",8600,100));
        excepted.add(new Product(5l,"행주","유한","향균 블루 행주 타올 45매×4","개",10400,100));

        List<Product> actual = productParser.parse();
        for(Product product : actual){
            log.debug("product:{}",product);
        }

        //actual 과 excepted 일치 하는지 검증 합니다.
        Assertions.assertEquals(excepted, actual);
    }
}