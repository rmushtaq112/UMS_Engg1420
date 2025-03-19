package com.example.ums_engg1420.dataparsers;

import com.example.ums_engg1420.dataclasses.Subject;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SubjectDataHandler extends DataHandler {
    private static final Sheet subjectSheet = workbook.getSheet("Subjects"); // Retrieves sheet containing subject data
    // Header data and row index as a map, for example "Subject Name" corresponds to index 0 vvv
    private static final Map<String, Integer> columnIndexMap = getColumnIndexMap(subjectSheet.getRow(0));

    // Responsible for turning a row of data in a sheet into a subject object, uses index of all headers in subject sheet
    private static Subject rowToSubjectObj(Row row) {
        Subject newSub = new Subject("PLACEHOLDER", null);
        for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {

            String columnName = entry.getKey();
            Integer columnIndex = entry.getValue();
            Cell cell = row.getCell(columnIndex);

            if (cell == null) continue;

            switch (columnName) {
                case "Subject Code":
                    if (cell.getCellType() == CellType.STRING) newSub.setSubjectCode(cell.getStringCellValue());
                    break;
                case "Subject Name":
                    if (cell.getCellType() == CellType.STRING) newSub.setSubjectName(cell.getStringCellValue());
                    break;
                default:
                    break;
            }
        }
        return newSub;
    }

    public static List<Subject> readSubjects() {
        if (subjectSheet == null) return new ArrayList<>(); // Handle missing sheet

        List<Subject> subjectArray = new ArrayList<>();


        for (int i = 1; i <= subjectSheet.getLastRowNum(); i++) {
            Row row = subjectSheet.getRow(i);

            boolean validRow = false;

            // Row is only read from if at least one element is not blank
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    validRow = true;
                }
            }

            if (!validRow) break; // No longer iterates on first instance of invalid row

            if (row != null && validRow == true) {
                subjectArray.add(rowToSubjectObj(row));
            }
        }
        return subjectArray;
    }

    private static void changeData(Subject subject, int rowIndex) {
        boolean validRow = false;

        for (int i = 1; i <= subjectSheet.getLastRowNum(); i++) {
            Row testrow = subjectSheet.getRow(i);

            for (Cell cell : testrow) {
                if (checkDuplicateSubject(cell, subject)) {
                    throw new DuplicateEntryException("Duplicate entry! " + cell.getStringCellValue() + " already exists in database.");
                }
                if (cell.getCellType() != CellType.BLANK) {
                    validRow = true;
                }
            }
            if (!validRow) {
                break;
            }

        }

        addToData(subject, rowIndex);
        saveData();

    }

    private static boolean checkDuplicateSubject(Cell cell, Subject subject) {
        if (cell.getStringCellValue().toLowerCase().equals(subject.getSubjectCode().toLowerCase()) || cell.getStringCellValue().toLowerCase().equals(subject.getSubjectName().toLowerCase())) {
            return true;
        }
        return false;
    }

    static void addToData(Subject subject, int rowIndex) {
        Row row = subjectSheet.getRow(rowIndex);

        if (row == null) {
            row = subjectSheet.createRow(rowIndex);
        }

        for (String key : columnIndexMap.keySet()) {
            Cell cell = row.createCell(columnIndexMap.get(key));
            switch (key) {
                case "Subject Name":
                    cell.setCellValue(subject.getSubjectName());
                    break;
                case "Subject Code":
                    cell.setCellValue(subject.getSubjectCode());
                    break;
                default:
                    System.out.println("something went wrong idk bro");
                    break;
            }
        }

        saveData();
    }

    public static void writeNewSubject(Subject subject) {
        for (int i = 1; i <= subjectSheet.getLastRowNum(); i++) {
            Row row = subjectSheet.getRow(i);

            boolean validRow = false;

            // Row is only read from if at least one element is not blank
            for (Cell cell : row) {
                if (checkDuplicateSubject(cell, subject)) {
                    throw new DuplicateEntryException("Duplicate entry! " + cell.getStringCellValue() + " already exists in database.");
                }
                if (cell.getCellType() != CellType.BLANK) {
                    validRow = true;
                }
            }

            if (!validRow) {
                addToData(subject, i);
                break;
            }
        }
    }

    public static void removeSubject(int rowIndex) {
        List<Subject> subjects = readSubjects();
        subjects.remove(rowIndex - 1);
        for (int i = 1; i <= subjectSheet.getLastRowNum(); i++) {
            Row row = subjectSheet.getRow(i);
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    cell.setBlank();
                }
            }
        }
        for (Subject subj : subjects) {
            if (subj != null){
                writeNewSubject(subj);
            }
        }
    }

}
