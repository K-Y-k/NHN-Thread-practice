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

package com.nhnacademy;

import com.nhnacademy.thread.CounterHandler;

public class App
{
    public static void main( String[] args )
    {
        //TODO#1 CounterHandler 객체를 생성 합니다. countMaxSize : 10
        CounterHandler counterHandler = new CounterHandler(10);

        //TODO#2 threadA 생성시 counterHandler 객체를 paramter로 전달 합니다.
        Thread threadA = new Thread(counterHandler);

        //TODO#3 threadA의 name을 'my-counter-A' 로 설정 합니다.
        threadA.setName("my-counter-A");

        //TODO#4 threadB 생성시 counterHandler 객체를 paramter로 전달 합니다.
        Thread threadB = new Thread(counterHandler);

        //TODO#5 threadB의 name을 'my-counter-B' 로 설정 합니다.
        threadB.setName("my-counter-B");

        //TODO#7 threadA를 시작 합니다.
        threadA.start();

        //TODO#8 threadB를 시작 합니다.
        threadB.start();
    }
}