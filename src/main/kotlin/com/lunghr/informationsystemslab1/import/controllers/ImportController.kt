package com.lunghr.informationsystemslab1.import.controllers

import com.lunghr.informationsystemslab1.import.service.ImportService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.CompletableFuture

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/file")
@Tag(name = "File Import")
class ImportController(
    private val importService: ImportService,
    private val transactionManager: PlatformTransactionManager,
    private val taskExecutor: ThreadPoolTaskExecutor
) {
    private val transactionTemplate = TransactionTemplate(transactionManager)

    @PostMapping("/import")
    @Tag(name = "Import")
    @Transactional(rollbackFor = [Exception::class])
    fun importObjectsFromFile(
        @RequestHeader("Authorization") token: String,
        @RequestParam("file") files: List<MultipartFile>,

    ) {
        require(files.size <= 4) { "Only 4 files in one request allowed" }

        val futures = files.map { file ->
            CompletableFuture.runAsync({
                // Передаем транзакцию в асинхронный контекст
                transactionTemplate.execute {
                    // Импортируем файл в рамках транзакции
                    importService.importObjectsFromFile(file, token)
                }
            }, taskExecutor)
        }

        // Ждем завершения всех асинхронных задач
        CompletableFuture.allOf(*futures.toTypedArray()).join()
    }
}
