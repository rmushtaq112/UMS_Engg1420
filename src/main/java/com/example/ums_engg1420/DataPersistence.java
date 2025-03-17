package com.example.ums_engg1420;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {
    private static final String FILE_PATH = "subjects.txt"; // File to store subject data

    // ✅ Save subject list to a file
    public static void saveData(List<Subject> subjects) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Subject subject : subjects) {
                writer.write(subject.getSubjectName() + "," + subject.getSubjectCode());
                writer.newLine(); // New line for each subject
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load subjects from the file
    public static List<Subject> loadData() {
        List<Subject> subjects = new ArrayList<>();
        File file = new File(FILE_PATH);

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return subjects;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
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
}
