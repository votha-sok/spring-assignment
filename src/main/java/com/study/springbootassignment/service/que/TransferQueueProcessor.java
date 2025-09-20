package com.study.springbootassignment.service.que;

import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.serviceImp.TransactionProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class TransferQueueProcessor {

    private final TransactionProcessorService processorService;

    private final BlockingQueue<CreateTransfer> transferQueue = new LinkedBlockingQueue<>();

    public TransferQueueProcessor(TransactionProcessorService processorService) {
        this.processorService = processorService;
        startWorker();
    }

    // Enqueue transfer request
    public void enqueue(CreateTransfer request) {
        request.setUserId(UserContext.getUserId());
        transferQueue.add(request);
        System.out.println("📝 Queued transfer from " + request.getFromAccountNumber() +
                " to " + request.getToAccountNumber() +
                " amount: " + request.getAmount());
    }

    // Worker thread
    private void startWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    CreateTransfer request = transferQueue.take(); // blocks
                    System.out.println("⚡ Processing transfer: " + request.getFromAccountNumber() +
                            " -> " + request.getToAccountNumber());

                    // Call actual transactional handler
                    processorService.handleTransfer(request);

                    System.out.println("✅ Processed transfer: " + request.getFromAccountNumber() +
                            " -> " + request.getToAccountNumber());
                } catch (Exception e) {
                    System.err.println("❌ Failed to process transfer: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
