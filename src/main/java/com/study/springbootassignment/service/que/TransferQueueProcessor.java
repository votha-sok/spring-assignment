package com.study.springbootassignment.service.que;

import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.entity.TransactionEntity;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.serviceImp.TransactionProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TransferQueueProcessor {

    private final TransactionProcessorService processorService;

    private final BlockingQueue<TransferTask> transferQueue = new LinkedBlockingQueue<>();

    public TransferQueueProcessor(TransactionProcessorService processorService) {
        this.processorService = processorService;
        startWorker();
    }


    // Wrapper: request + future
    private record TransferTask(CreateTransfer request, CompletableFuture<TransactionEntity> future) {
    }

    public CompletableFuture<TransactionEntity> enqueue(CreateTransfer request) {
        request.setUserId(UserContext.getUserId());
        CompletableFuture<TransactionEntity> future = new CompletableFuture<>();
        try {
            transferQueue.put(new TransferTask(request, future));
        } catch (InterruptedException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    private void startWorker() {
        Thread worker = new Thread(() -> {
            TransferTask task = null;
            while (true) {
                try {
                    task = transferQueue.take(); // blocks if empty
                    CreateTransfer req = task.request;

                    System.out.println("⚡ Processing transfer: " + req.getFromAccountNumber() +
                            " -> " + req.getToAccountNumber());

                    // Delay before processing (if desired)
                    TimeUnit.SECONDS.sleep(5);

                    TransactionEntity result = processorService.handleTransfer(req);

                    task.future.complete(result); // ✅ success
                    System.out.println("✅ Processed transfer: " + req.getFromAccountNumber() +
                            " -> " + req.getToAccountNumber());

                } catch (Exception e) {
                    System.err.println("❌ Failed to process transfer: " + e.getMessage());
                    // ✅ complete the future with exception
                    if (e instanceof RuntimeException re) {
                        // If you want the same exception type propagated
                        assert task != null;
                        task.future.completeExceptionally(re);
                    } else {
                        assert task != null;
                        task.future.completeExceptionally(
                                new RuntimeException("Transfer failed", e));
                    }
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
