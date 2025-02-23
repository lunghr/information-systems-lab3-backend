package com.lunghr.informationsystemslab1.import.controllers

import com.lunghr.informationsystemslab1.import.dto.FileStatsDTO
import com.lunghr.informationsystemslab1.import.model.FileStats
import com.lunghr.informationsystemslab1.import.model.repos.FileStatsRepository
import com.lunghr.informationsystemslab1.import.service.FileService
import io.minio.GetObjectArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.io.InputStream
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/file")
@Tag(name = "File Import")
class FileController<ByteArrayResource : Any?>(
    private val fileService: FileService,
    private val minioClient: MinioClient
) {
    @Value("\${minio.bucket}")
    lateinit var bucketName: String

    @PostMapping("/import")
    @Tag(name = "Import")
    @Transactional(rollbackFor = [Exception::class])
    fun importObjectsFromFile(
        @RequestHeader("Authorization") token: String,
        @RequestParam("file") files: List<MultipartFile>
    ) {
        require(files.size <= 4) { "Only 4 files in one request allowed" }
        fileService.importObjectsFromFiles(files, token)
    }

    @GetMapping("/stats")
    @Tag(name = "Get File Stats")
    @Transactional(rollbackFor = [Exception::class])
    fun getFileStats(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<FileStatsDTO>> {
        return ResponseEntity.ok(
            fileService.getFileStats(token).map {
                FileStatsDTO(
                    it.id,
                    it.user.username,
                    it.originalFilename,
                    it.storedFilename,
                    it.additions,
                    it.finished,
                    it.timestamp
                )
            }
        )
    }

    @GetMapping("/download/{fileName}")
    fun downloadFile(@PathVariable fileName: String): ResponseEntity<InputStreamResource> {
        return try {
            val stream: InputStream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .build()
            )

            val headers = HttpHeaders()
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName")

            ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(InputStreamResource(stream))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }
}



