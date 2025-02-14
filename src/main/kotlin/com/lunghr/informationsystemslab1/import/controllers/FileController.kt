package com.lunghr.informationsystemslab1.import.controllers

import com.lunghr.informationsystemslab1.import.service.FileService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
}
