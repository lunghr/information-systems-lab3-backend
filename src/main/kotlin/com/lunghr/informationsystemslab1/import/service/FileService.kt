package com.lunghr.informationsystemslab1.import.service
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Service
class FileService {
    fun splitFile(file: MultipartFile): Pair<MultipartFile, MultipartFile> {
        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(0)

        val totalRows = sheet.physicalNumberOfRows
        val midpoint = totalRows / 2

        val firstPart = createNewExcelFile(sheet, 0, midpoint)
        val secondPart = createNewExcelFile(sheet, midpoint, totalRows)

        return Pair(firstPart, secondPart)
    }

    private fun createNewExcelFile(sheet: Sheet, startRow: Int, endRow: Int): MultipartFile {
        val newWorkbook = XSSFWorkbook()
        val newSheet = newWorkbook.createSheet(sheet.sheetName)

        for (i in startRow until endRow) {
            val sourceRow = sheet.getRow(i) ?: continue
            val newRow = newSheet.createRow(i - startRow)

            for (j in 0 until sourceRow.physicalNumberOfCells) {
                val sourceCell = sourceRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                val newCell = newRow.createCell(j, sourceCell.cellType)

                when (sourceCell.cellType) {
                    CellType.STRING -> newCell.setCellValue(sourceCell.stringCellValue)
                    CellType.NUMERIC -> newCell.setCellValue(sourceCell.numericCellValue)
                    CellType.BOOLEAN -> newCell.setCellValue(sourceCell.booleanCellValue)
                    else -> newCell.setCellValue(sourceCell.toString())
                }
            }
        }

        val outputStream = ByteArrayOutputStream()
        newWorkbook.write(outputStream)
        newWorkbook.close()

        return object : MultipartFile {
            override fun getName(): String = sheet.sheetName
            override fun getOriginalFilename(): String = "${sheet.sheetName}.xlsx"
            override fun getContentType(): String = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            override fun isEmpty(): Boolean = outputStream.size() == 0
            override fun getSize(): Long = outputStream.size().toLong()
            override fun getBytes(): ByteArray = outputStream.toByteArray()
            override fun getInputStream(): ByteArrayInputStream = ByteArrayInputStream(outputStream.toByteArray())
            override fun transferTo(dest: java.io.File) = dest.writeBytes(outputStream.toByteArray())
        }
    }
}
