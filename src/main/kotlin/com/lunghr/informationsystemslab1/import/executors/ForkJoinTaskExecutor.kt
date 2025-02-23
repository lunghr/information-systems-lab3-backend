package com.lunghr.informationsystemslab1.import.executors

import org.springframework.core.task.TaskExecutor
import java.util.concurrent.ForkJoinPool

class ForkJoinTaskExecutor(private val forkJoinPool: ForkJoinPool) : TaskExecutor {
    override fun execute(task: Runnable) {
        forkJoinPool.submit(task)
    }
}
