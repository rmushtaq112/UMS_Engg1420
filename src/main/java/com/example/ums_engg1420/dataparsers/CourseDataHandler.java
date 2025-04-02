package com.example.ums_engg1420.dataparsers;

import com.example.ums_engg1420.dataclasses.Course;
import com.example.ums_engg1420.dataclasses.Subject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseDataHandler extends DataHandler {
    private static final Sheet courseSheet = workbook.getSheet("Courses"); // Retrieves sheet containing course data (placeholder name)
    // Header data and row index as a map, for example "Course Name" corresponds to index 0
    private static final Map<String, Integer> columnIndexMap = getColumnIndexMap(courseSheet.getRow(0));

    // Responsible for turning a row of data in a sheet into a Course object, uses index of all headers in course sheet
    private static Course rowToCourseObj(Row row) {
        Course newCourse = new Course("PLACEHOLDER", 0, null, null, 0, null, null, null, null);
        for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
            String columnName = entry.getKey();
            Integer columnIndex = entry.getValue();
            Cell cell = row.getCell(columnIndex);

            if (cell == null) continue;

            switch (columnName) {
                case "Course Name":
                    if (cell.getCellType() == CellType.STRING) newCourse.setCourseName(cell.getStringCellValue());
                    break;
                case "Course Code":
                    if (cell.getCellType() == CellType.NUMERIC) newCourse.setCourseCode((int) cell.getNumericCellValue());
                    break;
                case "Subject Code":
                    if (cell.getCellType() == CellType.STRING) newCourse.setSubjectCode(cell.getStringCellValue());
                    break;
                case "Section Number":
                    if (cell.getCellType() == CellType.STRING) newCourse.setSectionNumber(cell.getStringCellValue());
                    break;
                case "Capacity":
                    if (cell.getCellType() == CellType.NUMERIC) newCourse.setCapacity((int) cell.getNumericCellValue());
                    break;
                case "Lecture Time":
                    if (cell.getCellType() == CellType.STRING) newCourse.setLectureTime(cell.getStringCellValue());
                    break;
                case "Final Exam Date":
                    if (cell.getCellType() == CellType.STRING) newCourse.setFinalExamDate(cell.getStringCellValue());
                    break;
                case "Location":
                    if (cell.getCellType() == CellType.STRING) newCourse.setLocation(cell.getStringCellValue());
                    break;
                case "Teacher Name":
                    if (cell.getCellType() == CellType.STRING) newCourse.setTeacherName(cell.getStringCellValue());
                    break;
                default:
                    break;
            }
        }
        return newCourse;
    }

    public static List<Course> readCourses() {
        if (courseSheet == null) return new ArrayList<>(); // Handle missing sheet

        List<Course> courseArray = new ArrayList<>();

        for (int i = 1; i <= courseSheet.getLastRowNum(); i++) {
            Row row = courseSheet.getRow(i);

            boolean validRow = false;

            // Row is only read from if at least one element is not blank
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    validRow = true;
                }
            }

            if (!validRow) break; // No longer iterates on first instance of invalid row

            if (row != null && validRow) {
                courseArray.add(rowToCourseObj(row));
            }
        }
        return courseArray;
    }

//    private static void changeData(Course course, int rowIndex) {
//        boolean validRow = false;
//
//        for (int i = 1; i <= courseSheet.getLastRowNum(); i++) {
//            Row testRow = courseSheet.getRow(i);
//
//            for (Cell cell : testRow) {
//                if (checkDuplicateCourse(cell, course)) {
//                    throw new DuplicateEntryException("Duplicate entry! " + cell.getStringCellValue() + " already exists in database.");
//                }
//                if (cell.getCellType() != CellType.BLANK) {
//                    validRow = true;
//                }
//            }
//            if (!validRow) {
//                break;
//            }
//        }
//
//        addToData(course, rowIndex);
//        saveData();
//    }

//    private static boolean checkDuplicateCourse(Cell cell, Course course) {
//        if (cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue().toLowerCase();
//            return cellValue.equals(course.getCourseCode().toLowerCase()) ||
//                    cellValue.equals(course.getCourseName().toLowerCase());
//        } else if (cell.getCellType() == CellType.NUMERIC) {}
//        return false;
//    }

    static void addToData(Course course, int rowIndex) {
        Row row = courseSheet.getRow(rowIndex);

        if (row == null) {
            row = courseSheet.createRow(rowIndex);
        }

        for (String key : columnIndexMap.keySet()) {
            Cell cell = row.createCell(columnIndexMap.get(key));
            switch (key) {
                case "Course Name":
                    cell.setCellValue(course.getCourseName());
                    break;
                case "Course Code":
                    cell.setCellValue(course.getCourseCode());
                    break;
                case "Subject Code":
                    cell.setCellValue(course.getSubjectCode());
                    break;
                case "Section Number":
                    cell.setCellValue(course.getSectionNumber());
                    break;
                case "Capacity":
                    cell.setCellValue(course.getCapacity());
                    break;
                case "Lecture Time":
                    cell.setCellValue(course.getLectureTime());
                    break;
                case "Final Exam Date":
                    cell.setCellValue(course.getFinalExamDate());
                    break;
                case "Location":
                    cell.setCellValue(course.getLocation());
                    break;
                case "Teacher Name":
                    cell.setCellValue(course.getTeacherName());
                    break;
                default:
                    System.out.println("Something went wrong with column: " + key);
                    break;
            }
        }

        saveData();
    }

//    public static void writeNewCourse(Course course) {
//        for (int i = 1; i <= courseSheet.getLastRowNum(); i++) {
//            Row row = courseSheet.getRow(i);
//
//            boolean validRow = false;
//
//            // Row is only read from if at least one element is not blank
//            for (Cell cell : row) {
//                if (checkDuplicateCourse(cell, course)) {
//                    throw new DuplicateEntryException("Duplicate entry! " + cell.getStringCellValue() + " already exists in database.");
//                }
//                if (cell.getCellType() != CellType.BLANK) {
//                    validRow = true;
//                }
//            }
//
//            if (!validRow) {
//                addToData(course, i);
//                break;
//            }
//        }
//    }

//    public static void removeCourse(int rowIndex) {
//        List<Course> courses = readCourses();
//        if (rowIndex - 1 >= 0 && rowIndex - 1 < courses.size()) {
//            courses.remove(rowIndex - 1);
//        }
//        for (int i = 1; i <= courseSheet.getLastRowNum(); i++) {
//            Row row = courseSheet.getRow(i);
//            for (Cell cell : row) {
//                if (cell.getCellType() != CellType.BLANK) {
//                    cell.setBlank();
//                }
//            }
//        }
//        for (Course course : courses) {
//            if (course != null) {
//                writeNewCourse(course);
//            }
//        }
//    }
}