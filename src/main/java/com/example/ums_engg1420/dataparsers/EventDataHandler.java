package com.example.ums_engg1420.dataparsers;

import com.example.ums_engg1420.dataclasses.Event;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventDataHandler extends DataHandler {

    private static final Sheet eventSheet = workbook.getSheet("Events ");  // Correct sheet name
    private static final Map<String, Integer> columnIndexMap = getColumnIndexMap(eventSheet.getRow(0)); // Map column headers

    // Converts a row from Excel to an Event object
    private static Event rowToEventObj(Row row) {
        Event newEvent = new Event("PLACEHOLDER", null, null, null, null, 0, null, null, new ArrayList<>());

        for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
            String columnName = entry.getKey();
            Integer columnIndex = entry.getValue();
            Cell cell = row.getCell(columnIndex);

            if (cell == null) continue;

            switch (columnName) {
                case "Event Code":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setEventCode(cell.getStringCellValue());
                    break;
                case "Event Name":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setEventName(cell.getStringCellValue());
                    break;
                case "Description":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setDescription(cell.getStringCellValue());
                    break;
                case "Location":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setLocation(cell.getStringCellValue());
                    break;
                case "Date and Time":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setDateTime(cell.getStringCellValue());
                    break;
                case "Capacity":
                    if (cell.getCellType() == CellType.NUMERIC)
                        newEvent.setCapacity((int) cell.getNumericCellValue());
                    break;
                case "Cost":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setCost(cell.getStringCellValue());
                    break;
                case "Header Image":
                    if (cell.getCellType() == CellType.STRING)
                        newEvent.setHeaderImage(cell.getStringCellValue());
                    break;
                case "Registered Students":
                    if (cell.getCellType() == CellType.STRING) {
                        List<String> registeredStudents = Arrays.asList(cell.getStringCellValue().split(", "));
                        newEvent.setRegisteredStudents(registeredStudents);
                    }
                    break;
                default:
                    break;
            }
        }
        return newEvent;
    }

    // Reads all events from Excel and returns a list
    public static List<Event> readEvents() {
        List<Event> eventArray = new ArrayList<>();
        if (eventSheet == null) return eventArray; // Handle missing sheet

        for (int i = 1; i <= eventSheet.getLastRowNum(); i++) {
            Row row = eventSheet.getRow(i);
            if (row != null) {
                Event event = rowToEventObj(row);
                if (event != null && !event.getEventCode().equals("PLACEHOLDER")) {
                    eventArray.add(event);
                }
            }
        }
        return eventArray;
    }

    // Writes a new event to the sheet
    public static void writeEvent(Event event) {
        int rowIndex = eventSheet.getLastRowNum() + 1;
        Row row = eventSheet.createRow(rowIndex);

        for (String key : columnIndexMap.keySet()) {
            Cell cell = row.createCell(columnIndexMap.get(key));

            switch (key) {
                case "Event Code":
                    cell.setCellValue(event.getEventCode());
                    break;
                case "Event Name":
                    cell.setCellValue(event.getEventName());
                    break;
                case "Description":
                    cell.setCellValue(event.getDescription());
                    break;
                case "Location":
                    cell.setCellValue(event.getLocation());
                    break;
                case "Date and Time":
                    cell.setCellValue(event.getDateTime());
                    break;
                case "Capacity":
                    cell.setCellValue(event.getCapacity());
                    break;
                case "Cost":
                    cell.setCellValue(event.getCost());
                    break;
                case "Header Image":
                    cell.setCellValue(event.getHeaderImage());
                    break;
                case "Registered Students":
                    cell.setCellValue(String.join(", ", event.getRegisteredStudents()));
                    break;
                default:
                    break;
            }
        }
        saveData();
    }

    // Updates an existing event in the sheet
    public static void updateEvent(Event event, int rowIndex) {
        Row row = eventSheet.getRow(rowIndex);
        if (row == null) row = eventSheet.createRow(rowIndex);

        for (String key : columnIndexMap.keySet()) {
            Cell cell = row.createCell(columnIndexMap.get(key));

            switch (key) {
                case "Event Code":
                    cell.setCellValue(event.getEventCode());
                    break;
                case "Event Name":
                    cell.setCellValue(event.getEventName());
                    break;
                case "Description":
                    cell.setCellValue(event.getDescription());
                    break;
                case "Location":
                    cell.setCellValue(event.getLocation());
                    break;
                case "Date and Time":
                    cell.setCellValue(event.getDateTime());
                    break;
                case "Capacity":
                    cell.setCellValue(event.getCapacity());
                    break;
                case "Cost":
                    cell.setCellValue(event.getCost());
                    break;
                case "Header Image":
                    cell.setCellValue(event.getHeaderImage());
                    break;
                case "Registered Students":
                    cell.setCellValue(String.join(", ", event.getRegisteredStudents()));
                    break;
                default:
                    break;
            }
        }
        saveData();
    }

    // Removes an event from the sheet
    public static void deleteEvent(int rowIndex) {
        int lastRowNum = eventSheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex <= lastRowNum) {
            Row rowToRemove = eventSheet.getRow(rowIndex);
            if (rowToRemove != null) {
                eventSheet.removeRow(rowToRemove);
                // Shift remaining rows up to fill the gap
                if (rowIndex < lastRowNum) {
                    eventSheet.shiftRows(rowIndex + 1, lastRowNum, -1);
                }
            }
            saveData();  // Save after deletion
        }
    }
}
