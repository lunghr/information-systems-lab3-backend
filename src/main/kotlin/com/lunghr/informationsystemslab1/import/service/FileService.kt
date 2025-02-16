package com.lunghr.informationsystemslab1.import.service

import com.lunghr.informationsystemslab1.dto.BookCreatureDto
import com.lunghr.informationsystemslab1.dto.CoordinatesDto
import com.lunghr.informationsystemslab1.dto.MagicCityDto
import com.lunghr.informationsystemslab1.dto.RingDto
import com.lunghr.informationsystemslab1.service.BookCreatureService
import com.lunghr.informationsystemslab1.service.MagicCityService
import com.lunghr.informationsystemslab1.service.RingService
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

            processRow(sheet.getRow(i), headerMap)
        }

    }

    @Transactional(rollbackFor = [Exception::class])
    fun processRow(row: XSSFRow, headerMap: Map<String, Int>) {
        val ring = extractRingData(row, headerMap)
        val city = extractCityData(row, headerMap)
        val bookCreature = extractBookCreatureData(row, headerMap)
    }

    fun extractRingData(row: XSSFRow, headerMap: Map<String, Int>): RingDto {
        val name = row.getCell(headerMap["Ring name"]!!).stringCellValue
        val weight = row.getCell(headerMap["Ring weight"]!!).numericCellValue
        return RingDto(name, weight.toInt())
    }

    fun extractCityData(row: XSSFRow, headerMap: Map<String, Int>): MagicCityDto {
        val name = row.getCell(headerMap["City name"]!!).stringCellValue
        val governor = row.getCell(headerMap["City governor"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).stringCellValue
        val established = row.getCell(headerMap["City established"]!!).localDateTimeCellValue
        val population = row.getCell(headerMap["City population"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).numericCellValue
        val area = row.getCell(headerMap["City area"]!!).numericCellValue
        val populationDensity = row.getCell(headerMap["City population density"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).numericCellValue
        var isCapital: Boolean

        try {
            val isCapitalValue = row.getCell(headerMap["Is capital"]!!).stringCellValue
            if (isCapitalValue != "true" && isCapitalValue != "false") {
                throw IllegalArgumentException("City is capital must be either true or false")
            }
            isCapital = isCapitalValue == "true"
        } catch (e: Exception) {
            isCapital = row.getCell(headerMap["Is capital"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).booleanCellValue
        }

        return MagicCityDto(
            name = name,
            governor = governor,
            establishedData = established,
            population = population.toInt(),
            area = area,
            populationDensity = populationDensity,
            capital = isCapital
        )
    }

    fun extractCoordinatesData(row: XSSFRow, headerMap: Map<String, Int>): CoordinatesDto {
        val xCoordinate = row.getCell(headerMap["Coordinates.X"]!!).numericCellValue
        val yCoordinate = row.getCell(headerMap["Coordinates.Y"]!!).numericCellValue
        return CoordinatesDto(xCoordinate.toInt(), yCoordinate)
    }

    fun extractBookCreatureData(row: XSSFRow, headerMap: Map<String, Int>): BookCreatureDto {
        val name = row.getCell(headerMap["Creature name"]!!).stringCellValue
        val age = row.getCell(headerMap["Creature age"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).numericCellValue
        val creatureType = row.getCell(headerMap["Creature type"]!!).stringCellValue
        val attackLevel = row.getCell(headerMap["Creature attack level"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).numericCellValue

        return BookCreatureDto(
            name = name,
            age = age.toInt(),
            creatureType = creatureType,
            creationDate = LocalDateTime.now(),
            attackLevel = attackLevel.toFloat(),
            coordinates = extractCoordinatesData(row, headerMap),
            creatureLocationId = 0L,
            ringId = 0L

        )
    }
}
