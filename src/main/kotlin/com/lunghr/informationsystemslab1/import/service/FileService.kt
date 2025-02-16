package com.lunghr.informationsystemslab1.import.service

import com.lunghr.informationsystemslab1.service.BookCreatureService
import com.lunghr.informationsystemslab1.service.MagicCityService
import com.lunghr.informationsystemslab1.service.RingService
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.ForkJoinPool

@Service
@Transactional(rollbackFor = [Exception::class])
class FileService(
    private val magicCityService: MagicCityService,
    private val ringService: RingService,
    private val bookCreatureService: BookCreatureService,
    private val forkJoinPool: ForkJoinPool
) {
    @Transactional(rollbackFor = [Exception::class])
    fun importObjectsFromFiles(files: List<MultipartFile>, token: String) {
        val futures = files.map { file ->
            forkJoinPool.submit {
                prepareFile(file)
            }
        }
        futures.forEach { it.get() }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun prepareFile(file: MultipartFile) {
        println(file.originalFilename)
        file.inputStream.use { inputStream ->
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)
            val rowCount = sheet.physicalNumberOfRows
            if (rowCount == 0) {
                return
            }
            val header = sheet.getRow(0)
            val headerMap = mutableMapOf<String, Int>()
            for (i in 0 until header.physicalNumberOfCells) {
                headerMap[header.getCell(i).stringCellValue] = i
            }
            val startIndex = 1;
            val endIndex = rowCount

            println(headerMap)
            println(startIndex)
            println(endIndex)

            processSheet(sheet, headerMap, startIndex, endIndex, 5)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun processSheet(sheet: XSSFSheet, headerMap: Map<String, Int>, startIndex: Int, endIndex: Int, depth: Int) {
        val spawnTimestamp = System.currentTimeMillis()
        val timeout = 3000
        val forkJoinThreshold = 32

        for (i in startIndex until endIndex) {
            if (System.currentTimeMillis() - spawnTimestamp > timeout
                && endIndex - i > forkJoinThreshold
                && depth > 0
            ) {
                val half = (i + endIndex) / 2

                println()
                println()
                val firstWorker = forkJoinPool.submit {
                    processSheet(sheet, headerMap, i, half, depth - 1)
                }
                val secondWorker = forkJoinPool.submit {
                    processSheet(sheet, headerMap, half, endIndex, depth - 1)
                }

                firstWorker.join()
                secondWorker.join()

                return
            }

            Thread.sleep(100)
        }

    }
}
