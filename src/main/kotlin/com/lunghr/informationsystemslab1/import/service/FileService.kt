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

@Service
@Transactional(rollbackFor = [Exception::class])
class FileService(
    private val magicCityService: MagicCityService,
    private val ringService: RingService,
    private val bookCreatureService: BookCreatureService
) {
    fun importObjectsFromFile(file: MultipartFile, token: String) {
        require(file.contentType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") { "Invalid file type" }

        runCatching {
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
        }.onFailure { throw InvalidFileDataException("Invalid file data") }
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
