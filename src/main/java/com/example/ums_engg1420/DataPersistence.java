package com.example.ums_engg1420;

import com.example.ums_engg1420.dataclasses.Subject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {
    private static final String SUBJECTS_FILE_PATH = "subjects.txt"; // File to store subject data
    private static final String ENROLLED_SUBJECTS_FILE_PATH = "enrolledSubjects.txt"; // File to store enrolled subjects

    // ✅ Save subject list to a file
    public static void saveSubjects(List<Subject> subjects) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUBJECTS_FILE_PATH))) {
            for (Subject subject : subjects) {
                writer.write(subject.getSubjectName() + "," + subject.getSubjectCode());
                writer.newLine(); // New line for each subject
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load subjects from the file
    public static List<Subject> loadSubjects() {
        List<Subject> subjects = new ArrayList<>();
        File file = new File(SUBJECTS_FILE_PATH);

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return subjects;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SUBJECTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    subjects.add(new Subject(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    // ✅ Save enrolled subjects list to a file
    public static void saveEnrolledSubjects(List<Subject> enrolledSubjects) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENROLLED_SUBJECTS_FILE_PATH))) {
            for (Subject subject : enrolledSubjects) {
                writer.write(subject.getSubjectName() + "," + subject.getSubjectCode());
                writer.newLine(); // New line for each enrolled subject
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load enrolled subjects from the file
    public static List<Subject> loadEnrolledSubjects() {
        List<Subject> enrolledSubjects = new ArrayList<>();
        File file = new File(ENROLLED_SUBJECTS_FILE_PATH);

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return enrolledSubjects;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ENROLLED_SUBJECTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    enrolledSubjects.add(new Subject(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return enrolledSubjects;
    }
}
