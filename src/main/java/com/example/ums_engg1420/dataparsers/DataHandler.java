package com.example.ums_engg1420.dataparsers;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

// DataHandler contains constants which are used for retrieving data across different sheets,
// Also contains methods which can be universally used across different sheets, such as saving or indexing a row
public class DataHandler {
    protected static final String FILE_PATH = "src/main/resources/com/example/ums_engg1420/UMS_Data.xlsx";
    protected static Workbook workbook;

    static { // Creates a workbook from the Excel data file
        try {
            workbook = WorkbookFactory.create(new FileInputStream(FILE_PATH)); // Excel file loaded to memory
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Gets every header category in a sheet, and assigns it to a index using a map
    protected static Map<String, Integer> getColumnIndexMap(Row headerRow) { // Row that contains all headers
        Map<String, Integer> columnIndexMap = new HashMap<>(); // Hashmap stores name of header and index
        if (headerRow != null) { // Checks if row argument is empty, if not proceed
            for (Cell cell : headerRow) { // For every cell in the header row
                if (cell != null && cell.getCellType() == CellType.STRING) { // Checks if cell is not empty and is a string
                    columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex()); // Adds values into map
                }
            }
        }
        return columnIndexMap;
    }

    protected static void saveData() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) { // Outputs changed workbook data in memory to file
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
