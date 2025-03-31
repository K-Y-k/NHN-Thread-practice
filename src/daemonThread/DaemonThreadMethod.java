package com.nhnacademy;

public class DaemonThreadMethod {
    public static void main(String[] args) {
        // 사용자 쓰레드 생성
        Thread thread = new Thread(() -> System.out.println("쓰레드 실행"));

        // 해당 쓰레드가 데몬 쓰레드인지 판별 메소드
        System.out.println("데몬 스레드 여부 (기본값): " + thread.isDaemon());

        // 데몬 쓰레드로 설정 메소드
        thread.setDaemon(true);
        System.out.println("데몬 스레드 여부 (설정 후): " + thread.isDaemon());

        thread.start();
    }
}
