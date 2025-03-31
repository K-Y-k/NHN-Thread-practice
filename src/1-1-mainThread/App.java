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

import com.nhnacademy.thread.Counter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException{

        //TODO#6 현재 실행되고 있는 main thread의 이름을 my-thread로 설정 합니다.
        //참고.main thread : Thread.currentThread()
        Thread main = Thread.currentThread();
        main.setName("my-thread");

        //TODO#7 Counter 생성, countMaxSize는 10으로 설정 후 run method를 호출 합니다.
        Counter counter = new Counter(10);
        counter.run();
    }
}