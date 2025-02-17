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

import com.nhnacademy.nhnmart.product.exception.CsvParsingException;
import com.nhnacademy.nhnmart.product.parser.ProductParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class CsvProductParserFailTest {

    @Test
    @DisplayName("product_data_fail.csv 형식이 잘못됨")
    void parcingFailTest() throws IOException {
        /*/test/resources/product_data_fail.csv 파일을 기준으로 parcing 합니다.
           - 제품의 가격이 99000000000000000000000000000000 파싱에 실패 합니다.
           - CsvParsingException.class 예외가 발생할 수 있도록 검증 합니다.
           - ProductParser의 getProductsStream()를 참고 합니다.

            default InputStream getProductsStream(){
                return this.getClass()
                        .getClassLoader()
                        .getResourceAsStream(PRODUCTS_DATA);
            }
        */
        Assertions.assertThrows(CsvParsingException.class,()->{
            try(
                InputStream inputStream = this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("product_data_fail.csv");
                ProductParser productParser = new CsvProductParser(inputStream);
            ){
                productParser.parse();
            }
        });
    }
}
