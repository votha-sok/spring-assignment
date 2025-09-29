package com.study.springbootassignment.service.serviceImp;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class TransferQueueManager {
    private final Map<String, BlockingQueue<Runnable>> accountQueues = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(10); // configurable

//    public void submit(String accountNumber, Runnable task) {
//        accountQueues.computeIfAbsent(accountNumber, k -> new LinkedBlockingQueue<>()).add(task);
//        processNext(accountNumber);
//    }
//
//    private void processNext(String accountNumber) {
//        BlockingQueue<Runnable> queue = accountQueues.get(accountNumber);
//        if (queue == null) return;
//
//        Runnable task = queue.poll();
//        if (task != null) {
//            executor.submit(() -> {
//                try {
//                    task.run();
//                } finally {
//                    processNext(accountNumber); // continue with next task
//                }
//            });
//        }
//    }
    public void submit(String accountNumber, Runnable task) {
        accountQueues.computeIfAbsent(accountNumber, k -> new LinkedBlockingQueue<>()).add(task);

        executor.submit(() -> {
            Runnable t;
            while ((t = accountQueues.get(accountNumber).poll()) != null) {
                try {
                    System.out.println("Running task for account " + accountNumber);
                    t.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
