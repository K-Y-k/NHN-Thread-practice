package com.nhnacademy;

public class DaemonThreadEx {
    public static void main(String[] args) {
        // 프로그램은 메인 쓰레드와 사용자 쓰레드들이 모두 종료되면 종료되는데
        // 데몬 쓰레드만 종료되지 않아도 프로그램이 종료된다.

        // 사용자 쓰레드 생성
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("데몬 쓰레드 실행 중...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 데몬 스레드로 설정
        daemonThread.setDaemon(true);
        daemonThread.start();

        
        System.out.println("메인 쓰레드 종료");
    }
}
