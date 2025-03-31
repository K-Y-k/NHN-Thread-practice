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

import com.nhnacademy.thread.CounterThread;

public class App
{
    public static void main( String[] args )
    {
        //TODO#5 CounterThread 객체를 생성 합니다.
        // thread-name :  my-counter, countMaxSize :10
        CounterThread counterThread = new CounterThread("my-counter", 10);

        //TODO#6 counterThread를 시작 합니다.
        counterThread.run();
    }
}
