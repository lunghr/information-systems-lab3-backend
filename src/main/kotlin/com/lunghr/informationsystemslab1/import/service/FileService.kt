package com.lunghr.informationsystemslab1.import.service

import com.lunghr.informationsystemslab1.dto.BookCreatureDto
import com.lunghr.informationsystemslab1.dto.CoordinatesDto
import com.lunghr.informationsystemslab1.dto.MagicCityDto
import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.import.exceptions.InvalidFileDataException
import com.lunghr.informationsystemslab1.service.BookCreatureService
import com.lunghr.informationsystemslab1.service.MagicCityService
import com.lunghr.informationsystemslab1.service.RingService
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Service
@Transactional(rollbackFor = [Exception::class])
class FileService(
    private val magicCityService: MagicCityService,
    private val ringService: RingService,
    private val bookCreatureService: BookCreatureService,
//    private val taskExecutor: ThreadPoolTaskExecutor
) {
    @Transactional(rollbackFor = [Exception::class])
    fun importObjectsFromFiles(files: List<MultipartFile>, token: String) {
        val executor = Executors.newFixedThreadPool(4)
        val futures = files.map { file ->
            executor.submit(Callable { processFileWithTimeout(file, token) })
        }

        try {
            futures.forEach { it.get() }
        } catch (e: Exception) {
            futures.forEach { it.cancel(true) }
            throw e
        } finally {
            executor.shutdown()
        }
    }

    private fun processFileWithTimeout(file: MultipartFile, token: String) {
        val executor = Executors.newCachedThreadPool()
        val future = executor.submit(Callable { processFile(file, token) })

        try {
            future.get(30, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
            future.cancel(true)
            println("Файл обрабатывается слишком долго, увеличиваем число потоков")
            processFileInParallel(file, token)
        } finally {
            executor.shutdown()
        }
    }

    private fun processFile(file: MultipartFile, token: String) {
        require(file.contentType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") { "Invalid file type" }

        file.inputStream.use { inputStream ->
            val sheet = XSSFWorkbook(inputStream).getSheetAt(0)

            val (rings, magicCities, bookCreatures) = sheet.groupBy { it.getCell(0).stringCellValue.trim() }
                .let {
                    Triple(it["Ring"].orEmpty(), it["MagicCity"].orEmpty(), it["BookCreature"].orEmpty())
                }

            rings.forEach { ringService.createRing(parseRingDto(it), token) }
            magicCities.forEach { magicCityService.createMagicCity(parseMagicCityDto(it), token) }
            bookCreatures.forEach { bookCreatureService.createBookCreature(parseBookCreatureDto(it), token) }
        }
    }

    private fun processFileInParallel(file: MultipartFile, token: String) {
        val executor = Executors.newFixedThreadPool(2) // Два потока на задачу
        val future1 = executor.submit(Callable { processHalfFile(file, token, true) })
        val future2 = executor.submit(Callable { processHalfFile(file, token, false) })

        try {
            future1.get()
            future2.get()
        } finally {
            executor.shutdown()
        }
    }

    private fun processHalfFile(file: MultipartFile, token: String, firstHalf: Boolean) {
        file.inputStream.use { inputStream ->
            val sheet = XSSFWorkbook(inputStream).getSheetAt(0)
            val rows = sheet.iterator().asSequence().toList()
            val half = rows.size / 2

            val targetRows = if (firstHalf) rows.take(half) else rows.drop(half)

            targetRows.forEach {
                when (it.getCell(0).stringCellValue.trim()) {
                    "Ring" -> ringService.createRing(parseRingDto(it), token)
                    "MagicCity" -> magicCityService.createMagicCity(parseMagicCityDto(it), token)
                    "BookCreature" -> bookCreatureService.createBookCreature(parseBookCreatureDto(it), token)
                }
            }
        }
    }

    private fun parseRingDto(row: Row): RingDto {
        return runCatching {
            RingDto(
                name = row.getCell(1).stringCellValue,
                weight = row.getCell(2).numericCellValue.toInt()
            )
        }.getOrElse { throw InvalidFileDataException("Invalid data in row ${row.rowNum}") }
    }

    private fun parseMagicCityDto(row: Row): MagicCityDto {
        return runCatching {
            MagicCityDto(
                name = row.getCell(1).stringCellValue,
                area = row.getCell(2).numericCellValue,
                population = row.getCell(3).numericCellValue.toInt(),
                establishedData = java.time.LocalDateTime.now(),
//                establishedData = row.getCell(4).dateCellValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                governor = row.getCell(5).stringCellValue,
                capital = row.getCell(6).booleanCellValue,
                populationDensity = row.getCell(7).numericCellValue
            )
        }.getOrElse { throw InvalidFileDataException("Invalid data in row ${row.rowNum}") }
    }

    private fun parseBookCreatureDto(row: Row): BookCreatureDto {
        return runCatching {
            BookCreatureDto(
                name = row.getCell(1).stringCellValue,
                coordinates = CoordinatesDto(
                    x = row.getCell(2).numericCellValue.toInt(),
                    y = row.getCell(3).numericCellValue
                ),
                age = row.getCell(4).numericCellValue.toInt(),
                creatureType = row.getCell(5).stringCellValue,
                ringId = row.getCell(6).numericCellValue.toLong(),
                creatureLocationId = row.getCell(7).numericCellValue.toLong(),
                attackLevel = row.getCell(8).numericCellValue.toFloat()
            )
        }.getOrElse { throw InvalidFileDataException("Invalid data in row ${row.rowNum}") }
    }
}
