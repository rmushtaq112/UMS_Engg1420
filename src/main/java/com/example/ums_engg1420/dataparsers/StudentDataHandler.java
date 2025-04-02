package com.example.ums_engg1420.dataparsers;

import com.example.ums_engg1420.dataclasses.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StudentDataHandler extends DataHandler {
    private static Sheet studentSheet;
    private static Map<String, Integer> columnIndexMap;

    // Loads the student sheet from the Excel file
    private static void loadStudentSheet() {
        try (FileInputStream file = new FileInputStream(new File("path_to_excel_file"))) {
            Workbook workbook = new XSSFWorkbook(file);
            studentSheet = workbook.getSheet("Students");
            // Initialize columnIndexMap only after the sheet is loaded
            columnIndexMap = getColumnIndexMap(studentSheet.getRow(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Converts a row to a Student object
    private static Student rowToStudentObj(Row row) {
        if (studentSheet == null) {
            loadStudentSheet();  // Ensure the sheet is loaded before processing rows
        }

        Student newStudent = new Student("PLACEHOLDER", null);

        // Converts a row from the sheet into a Student object
        for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
            String columnName = entry.getKey();
            Integer columnIndex = entry.getValue();
            Cell cell = row.getCell(columnIndex);

            if (cell == null) continue;

            switch (columnName) {
                case "Student ID":
                    if (cell.getCellType() == CellType.STRING) newStudent.setStudentId(cell.getStringCellValue());
                    break;
                case "Name":
                    if (cell.getCellType() == CellType.STRING) newStudent.setName(cell.getStringCellValue());
                    break;
                case "Email Address":
                    if (cell.getCellType() == CellType.STRING) newStudent.setEmail(cell.getStringCellValue());
                    break;
                case "Profile Photo":
                    if (cell.getCellType() == CellType.STRING) newStudent.setProfilePhoto(cell.getStringCellValue());
                    break;
                case "Address":
                    if (cell.getCellType() == CellType.STRING) newStudent.setAddress(cell.getStringCellValue());
                    break;
                case "Telephone":
                    if (cell.getCellType() == CellType.STRING) newStudent.setTelephone(cell.getStringCellValue());
                    break;
                case "Tuition":
                    if (cell.getCellType() == CellType.STRING) newStudent.setTuitionStatus(cell.getStringCellValue());
                    break;
                case "Current Semester":
                    if (cell.getCellType() == CellType.NUMERIC) newStudent.setCurrentSemester((int) cell.getNumericCellValue());
                    break;
                case "Academic Level":
                    if (cell.getCellType() == CellType.STRING) newStudent.setAcademicLevel(cell.getStringCellValue());
                    break;
                case "Thesis Title":
                    if (cell.getCellType() == CellType.STRING) newStudent.setThesisTitle(cell.getStringCellValue());
                    break;
                case "Progress":
                    if (cell.getCellType() == CellType.NUMERIC) newStudent.setProgress(cell.getNumericCellValue());
                    break;
                default:
                    break;
            }
        }
        return newStudent;
    }

    // Adds or updates student data in the sheet at the specified row index
    private static void addToData(Student student, int rowIndex) {
        if (studentSheet == null) {
            loadStudentSheet();  // Ensure the sheet is loaded before adding data
        }

        Row row = studentSheet.getRow(rowIndex);

        if (row == null) {
            row = studentSheet.createRow(rowIndex);
        }

        for (String key : columnIndexMap.keySet()) {
            Cell cell = row.createCell(columnIndexMap.get(key));
            switch (key) {
                case "Student ID":
                    cell.setCellValue(student.getStudentId());
                    break;
                case "Name":
                    cell.setCellValue(student.getName());
                    break;
                case "Email Address":
                    cell.setCellValue(student.getEmail());
                    break;
                case "Profile Photo":
                    cell.setCellValue(student.getProfilePhoto());
                    break;
                case "Address":
                    cell.setCellValue(student.getAddress());
                    break;
                case "Telephone":
                    cell.setCellValue(student.getTelephone());
                    break;
                case "Tuition":
                    cell.setCellValue(student.getTuitionStatus());
                    break;
                case "Current Semester":
                    cell.setCellValue(student.getCurrentSemester());
                    break;
                case "Academic Level":
                    cell.setCellValue(student.getAcademicLevel());
                    break;
                case "Thesis Title":
                    cell.setCellValue(student.getThesisTitle());
                    break;
                case "Progress":
                    cell.setCellValue(student.getProgress());
                    break;
                default:
                    System.out.println("Unexpected column: " + key);
                    break;
            }
        }

        saveData();
    }

    // Updates the student's profile by calling addToData
    public static void updateProfile(Student student, int rowIndex) {
        addToData(student, rowIndex);
        saveData();
    }

    // Uploads a profile photo by setting the appropriate cell value
    public static void uploadProfilePhoto(int rowIndex, String photoPath) {
        if (studentSheet == null) {
            loadStudentSheet();  // Ensure the sheet is loaded before updating profile photo
        }

        Row row = studentSheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.createCell(columnIndexMap.get("Profile Photo"));
            cell.setCellValue(photoPath);
        }
        saveData();
    }

    // Retrieves the tuition status of a student at a given row index
    public static String getTuitionStatus(int rowIndex) {
        if (studentSheet == null) {
            loadStudentSheet();  // Ensure the sheet is loaded before retrieving tuition status
        }

        Row row = studentSheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(columnIndexMap.get("Tuition"));
            return cell != null ? cell.getStringCellValue() : "Unknown";
        }
        return "Unknown";
    }

    // Sends an email notification (Placeholder for actual email sending logic)
    public static void sendEmailNotification(String email, String message) {
        System.out.println("Sending email to: " + email + " with message: " + message);
        // Email sending logic to be implemented
    }

    private static Student loggedInStudent = null; // Store the logged-in student

    public static void setLoggedInStudent(Student student) {
        loggedInStudent = student;
    }

    public static Student getLoggedInStudent() {
        return loggedInStudent; // Returns the logged-in student
    }

    // Retrieves the row index of a student by their ID
    public static int getStudentRowIndex(String studentId) {
        if (studentSheet == null) {
            loadStudentSheet();  // Ensure the sheet is loaded before finding the student
        }

        for (int i = 1; i <= studentSheet.getLastRowNum(); i++) {
            Row row = studentSheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(columnIndexMap.get("Student ID"));
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().equals(studentId)) {
                        return i; // Return the row index of the student
                    }
                }
            }
        }
        return -1; // Return -1 if student is not found
    }

    // Initialize the class by loading the sheet
    static {
        loadStudentSheet(); // Ensure the sheet is loaded when the class is loaded
    }
}

