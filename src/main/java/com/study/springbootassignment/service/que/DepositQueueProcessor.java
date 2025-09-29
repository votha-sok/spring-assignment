package com.study.springbootassignment.service.que;

import com.study.springbootassignment.dto.transaction.CreateDeposit;
import com.study.springbootassignment.dto.transaction.CreateTransfer;
import com.study.springbootassignment.dto.transaction.CreateWithdraw;
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
public class DepositQueueProcessor {

    private final TransactionProcessorService processorService;

    private final BlockingQueue<DepositTask> depositQueue = new LinkedBlockingQueue<>();

    public DepositQueueProcessor(TransactionProcessorService processorService) {
        this.processorService = processorService;
        startWorker();
    }


    // Wrapper: request + future
    private record DepositTask(CreateDeposit request, CompletableFuture<TransactionEntity> future) {
    }

    public CompletableFuture<TransactionEntity> enqueue(CreateDeposit request) {
        request.setUserId(UserContext.getUserId());
        CompletableFuture<TransactionEntity> future = new CompletableFuture<>();
        try {
            depositQueue.put(new DepositQueueProcessor.DepositTask(request, future));
        } catch (InterruptedException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    private void startWorker() {
        Thread worker = new Thread(() -> {
            DepositTask task = null;
            while (true) {
                try {
                    task = depositQueue.take(); // blocks if empty
                    CreateDeposit req = task.request;

                    log.info("⚡ Processing deposit: {}", req.getAccountNumber());

                    // Delay before processing (if desired)
//                    TimeUnit.SECONDS.sleep(0);

                    TransactionEntity result = processorService.handleDeposit(req);

                    task.future.complete(result); // ✅ success
                    log.info("✅ Processed deposit: {}", req.getAccountNumber());

                } catch (Exception e) {
                    log.error("❌ Failed to process deposit", e);
                    // ✅ complete the future with exception
                    if (e instanceof RuntimeException re) {
                        // If you want the same exception type propagated
                        assert task != null;
                        task.future.completeExceptionally(re);
                    } else {
                        assert task != null;
                        task.future.completeExceptionally(
                                new RuntimeException("Deposit failed", e));
                    }
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
