package com.lunghr.informationsystemslab1.import.controllers

import com.lunghr.informationsystemslab1.import.dto.FileStatsDTO
import com.lunghr.informationsystemslab1.import.model.FileStats
import com.lunghr.informationsystemslab1.import.service.FileService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/file")
@Tag(name = "File Import")
class FileController(
    private val fileService: FileService
) {

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
                    it.filename,
                    it.additions,
                    it.finished,
                    it.timestamp
                )
            }
        )
    }
}
