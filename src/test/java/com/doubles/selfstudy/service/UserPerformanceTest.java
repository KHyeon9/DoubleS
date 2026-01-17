package com.doubles.selfstudy.service;

import com.doubles.selfstudy.repository.UserAccountRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Disabled
@SpringBootTest
public class UserPerformanceTest {

    @Autowired
    private ServiceUtils serviceUtils;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void testDataBaseUserAccess() throws Exception {
        int threadCount = 100;
        // 100개의 스레드를 미리 만들어둔 스레드 풀로 요청을 동시에 쏟아붓기 위한 도구
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        // 100개의 동시 요청 모두 마칠 때까지 메인 스레드(테스트 스레드)를 기다리게 만드는
        // 대기 신호기입니다.
        CountDownLatch latch = new CountDownLatch(threadCount);

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    userAccountRepository.findById("tester");
                } finally {
                    // 100개의 스레드가 위 로직을 마칠때마다 -1
                    // 0이되면 기록 종료
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 스레드가 끝날 때까지 대기(latch의 값이 0이되면 다음줄 시작)
        long end = System.currentTimeMillis();

        System.out.println("평균 응답 시간: " + (end - start) / threadCount + "ms");
    }

    @Test
    void testCacheUserAccess() throws Exception {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        // redis에 값 저장
        serviceUtils.getUserAccountOrException("tester");

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    serviceUtils.getUserAccountOrException("tester"); // 테스트 대상 메서드
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기
        long end = System.currentTimeMillis();

        System.out.println("평균 응답 시간: " + (end - start) / threadCount + "ms");
    }
}
