package com.cloud;

/**
 * @Author zhouTao
 * @Date ${DATE}
 */
public class Main {
    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(200000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        thread.stop();

    }
}