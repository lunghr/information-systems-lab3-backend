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
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask

@Service
@Transactional(rollbackFor = [Exception::class])
class FileService(
    private val magicCityService: MagicCityService,
    private val ringService: RingService,
    private val bookCreatureService: BookCreatureService,
    private val forkJoinPool: ForkJoinPool,
    private val transactionManager: PlatformTransactionManager
) {
    @Transactional(rollbackFor = [Exception::class])
    fun importObjectsFromFiles(files: List<MultipartFile>, token: String) {
        val futures = files.map { file ->
            forkJoinPool.submit {
                try {
                    prepareFile(file, token)
                } catch (ex: Exception) {
                    println("Error processing file ${file.originalFilename}")
                }
            }
        }
        futures.forEach { it.join() }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun prepareFile(
        file: MultipartFile,
        token: String
    ) {
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

            val transactionTemplate = TransactionTemplate(transactionManager)
            var stopFlag = false;

            val stopCallback = {
                stopFlag = true
                throw InterruptedException("Interrupting due to error")
            }
            val checkCallback: () -> Boolean = {
                if (stopFlag) {
                    println("Interrupting due to error")
                    throw InterruptedException("Interrupting due to error")
                }
                false
            }

            transactionTemplate.execute {
                processSheet(
                    sheet,
                    headerMap,
                    startIndex,
                    endIndex,
                    5,
                    token,
                    transactionTemplate,
                    stopCallback,
                    checkCallback
                )
                checkCallback()
            }
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun processSheet(
        sheet: XSSFSheet,
        headerMap: Map<String, Int>,
        startIndex: Int,
        endIndex: Int,
        depth: Int,
        token: String,
        transactionTemplate: TransactionTemplate,
        stopCallback: () -> Unit,
        checkCallback: () -> Boolean
    ) {
        val spawnTimestamp = System.currentTimeMillis()
        val timeout = 3000
        val forkJoinThreshold = 32

        for (i in startIndex until endIndex) {
            checkCallback()
            if (System.currentTimeMillis() - spawnTimestamp > timeout && endIndex - i > forkJoinThreshold && depth > 0) {
                val half = (i + endIndex) / 2

                println("Forking workers for rows $startIndex-$endIndex")

                val firstWorker = forkJoinPool.submit {
                    transactionTemplate.execute {
                        processSheet(
                            sheet,
                            headerMap,
                            i,
                            half,
                            depth - 1,
                            token,
                            transactionTemplate,
                            stopCallback,
                            checkCallback
                        )
                    }
                }
                val secondWorker = forkJoinPool.submit {
                    transactionTemplate.execute {
                        processSheet(
                            sheet,
                            headerMap,
                            half,
                            endIndex,
                            depth - 1,
                            token,
                            transactionTemplate,
                            stopCallback,
                            checkCallback
                        )
                    }
                }

                try {
                    firstWorker.join()
                    secondWorker.join()
                } catch (ex: Exception) {
                    println("Exception in worker: $startIndex - $endIndex")
                    stopCallback()
                }

                return
            }

            processRow(sheet.getRow(i), headerMap, token)
        }

    }

    @Transactional(rollbackFor = [Exception::class])
    fun processRow(row: XSSFRow, headerMap: Map<String, Int>, token: String) {
        val ring = extractRingData(row, headerMap)
        val city = extractCityData(row, headerMap)
        val ringId = ringService.createRing(ring, token).id
        val cityId = magicCityService.createMagicCity(city, token).id
        val bookCreature = extractBookCreatureData(row, headerMap, ringId, cityId)
        bookCreatureService.createBookCreature(bookCreature, token)
    }

    fun extractRingData(row: XSSFRow, headerMap: Map<String, Int>): RingDto {
        val name = row.getCell(headerMap["Ring name"]!!).stringCellValue
        val weight = row.getCell(headerMap["Ring weight"]!!).numericCellValue
        return RingDto(name, weight.toInt())
    }

    fun extractCityData(row: XSSFRow, headerMap: Map<String, Int>): MagicCityDto {
        val name = row.getCell(headerMap["City name"]!!).stringCellValue
        val governor =
            row.getCell(headerMap["City governor"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).stringCellValue
        val established = row.getCell(headerMap["City established"]!!).localDateTimeCellValue
        val population =
            row.getCell(headerMap["City population"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).numericCellValue
        val area = row.getCell(headerMap["City area"]!!).numericCellValue
        val populationDensity = row.getCell(
            headerMap["City population density"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
        ).numericCellValue
        var isCapital: Boolean

        try {
            val isCapitalValue = row.getCell(headerMap["Is capital"]!!).stringCellValue
            if (isCapitalValue != "true" && isCapitalValue != "false") {
                throw IllegalArgumentException("City is capital must be either true or false")
            }
            isCapital = isCapitalValue == "true"
        } catch (e: Exception) {
            isCapital =
                row.getCell(headerMap["Is capital"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).booleanCellValue
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

    fun extractBookCreatureData(
        row: XSSFRow, headerMap: Map<String, Int>, ringId: Long, cityId: Long
    ): BookCreatureDto {
        val name = row.getCell(headerMap["Creature name"]!!).stringCellValue
        val age = row.getCell(headerMap["Creature age"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).numericCellValue
        val creatureType = row.getCell(headerMap["Creature type"]!!).stringCellValue
        val attackLevel = row.getCell(
            headerMap["Creature attack level"]!!, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
        ).numericCellValue

        return BookCreatureDto(
            name = name,
            age = age.toInt(),
            creatureType = creatureType,
            creationDate = LocalDateTime.now(),
            attackLevel = attackLevel.toFloat(),
            coordinates = extractCoordinatesData(row, headerMap),
            creatureLocationId = cityId,
            ringId = ringId

        )
    }
}
