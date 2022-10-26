package com.example.concurrencytest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
//@Transactional
class ConControllerTest {

    Logger log = (Logger) LoggerFactory.getLogger(ConControllerTest.class);

    @Autowired
    private ConService conService;

    @Autowired
    private WebApplicationContext ctx;

    @Test
    public void genTest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        mockMvc.perform(post("/con")
            .param("age", "23")
            .param("name", "생성")).andDo(print());
    }

    @Test
    @DisplayName("수정 요청 순서가 보장되지 않음을 증명하는 테스트")
    @RepeatedTest(10)
    public void multiModifyTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(7);

        Queue<Long> threadResultQueue = new ConcurrentLinkedQueue<>();
        Queue<Long> expectResultQueue = new ConcurrentLinkedQueue<>();

        final int MAX_TRY_SIZE = 10;

        for (long i = 0; i < MAX_TRY_SIZE; i++) {
            Long finalI = i;
            expectResultQueue.offer(finalI);   // 0 ~ MAX_TRY_SIZE
            executorService.submit(() -> {
                try {
                    log.info("{}", finalI);
                    ConDto conDto = conService.modifyAge(18L, finalI.intValue());
                    threadResultQueue.offer(finalI);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            int sameCnt = 0;
            for (int i = 0; i < MAX_TRY_SIZE; i++) {
                long input = threadResultQueue.poll();
                long expect = expectResultQueue.poll();
                log.info("expect: {} -> result: {}", expect, input);
                if (input == expect) {
                    sameCnt++;
                }
            }
            assertThat(sameCnt).isNotEqualTo(MAX_TRY_SIZE);
        }
    }
}