package com.study.springbootassignment.service.que;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.serviceImp.TransactionProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class WithdrawQueueProcessor {

    private final TransactionProcessorService processorService;

    private final BlockingQueue<CreateWithdraw> withdrawQueue = new LinkedBlockingQueue<>();

    public WithdrawQueueProcessor(TransactionProcessorService processorService) {
        this.processorService = processorService;
        startWorker();
    }

    // Enqueue transfer request
//    public void enqueue(CreateWithdraw request) {
//        request.setUserId(UserContext.getUserId());
//        withdrawQueue.add(request);
//        System.out.println("📝 Queued withdraw to " + request.getAccountNumber() +
//                " amount: " + request.getAmount());
//    }
    public CompletableFuture<CreateWithdraw> enqueue(CreateWithdraw request) {
        return CompletableFuture.supplyAsync(() -> {
            processorService.handleWithdraw(request);
            return request;
        });
    }
    // Worker thread
    private void startWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    CreateWithdraw request = withdrawQueue.take(); // blocks
                    System.out.println("⚡ Processing withdraw : " + request.getAccountNumber() );

                    // Call actual transactional handler
                    processorService.handleWithdraw(request);

                    System.out.println("✅ Processed withdraw: " + request.getAccountNumber());
                } catch (Exception e) {
                    System.err.println("❌ Failed to process withdraw: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
