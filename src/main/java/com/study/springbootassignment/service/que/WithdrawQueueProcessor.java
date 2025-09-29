package com.study.springbootassignment.service.que;

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

@Component
@Slf4j
public class WithdrawQueueProcessor {

    private final TransactionProcessorService processorService;
    private final BlockingQueue<WithdrawTask> withdrawQueue = new LinkedBlockingQueue<>();

    public WithdrawQueueProcessor(TransactionProcessorService processorService) {
        this.processorService = processorService;
        startWorker();
    }

    // Wrapper: request + future
        private record WithdrawTask(CreateWithdraw request, CompletableFuture<TransactionEntity> future) {
    }

    public CompletableFuture<TransactionEntity> enqueue(CreateWithdraw request) {
        request.setUserId(UserContext.getUserId());
        CompletableFuture<TransactionEntity> future = new CompletableFuture<>();
        try {
            withdrawQueue.put(new WithdrawTask(request, future));
        } catch (InterruptedException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    private void startWorker() {
        Thread worker = new Thread(() -> {
            WithdrawTask task = null;
            while (true) {
                try {
                    task = withdrawQueue.take(); // blocks if empty
                    CreateWithdraw req = task.request;

                    log.info("⚡ Processing withdraw: {}", req.getAccountNumber());

                    // Delay before processing (if desired)
//                    TimeUnit.SECONDS.sleep(3);

                    TransactionEntity result = processorService.handleWithdraw(req);

                    task.future.complete(result); // ✅ success
                    log.info("✅ Processed withdraw: {}", req.getAccountNumber());

                } catch (Exception e) {
                    log.error("❌ Failed to process withdraw", e);
                    // ✅ complete the future with exception
                    if (e instanceof RuntimeException re) {
                        // If you want the same exception type propagated
                        assert task != null;
                        task.future.completeExceptionally(re);
                    } else {
                        assert task != null;
                        task.future.completeExceptionally(
                                new RuntimeException("Withdraw failed", e));
                    }
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
