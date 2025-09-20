package com.study.springbootassignment.service.que;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.serviceImp.TransactionProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class DepositQueueProcessor {

    private final TransactionProcessorService processorService;

    private final BlockingQueue<CreateDeposit> depositQueue = new LinkedBlockingQueue<>();

    public DepositQueueProcessor(TransactionProcessorService processorService) {
        this.processorService = processorService;
        startWorker();
    }

    // Enqueue transfer request
    public void enqueue(CreateDeposit request) {
        request.setUserId(UserContext.getUserId());
        depositQueue.add(request);
        System.out.println("📝 Queued deposit to " + request.getAccountNumber() +
                " amount: " + request.getAmount());
    }

    // Worker thread
    private void startWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    CreateDeposit request = depositQueue.take(); // blocks
                    System.out.println("⚡ Processing deposit : " + request.getAccountNumber() );

                    // Call actual transactional handler
                    processorService.handleDeposit(request);

                    System.out.println("✅ Processed transfer: " + request.getAccountNumber());
                } catch (Exception e) {
                    System.err.println("❌ Failed to process deposit: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
