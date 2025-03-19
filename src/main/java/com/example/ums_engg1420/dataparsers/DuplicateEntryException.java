package com.example.ums_engg1420.dataparsers;


// Error yields of trying to add an already existing piece of data in the tables
public class DuplicateEntryException extends RuntimeException {
    private final Object duplicateEntry;

    public DuplicateEntryException(String message) {
        super(message);
        this.duplicateEntry = null; // No entry provided
    }

    public DuplicateEntryException(String message, Object duplicateEntry) {
        super(message);
        this.duplicateEntry = duplicateEntry;
    }

    public Object getDuplicateEntry() {
        return duplicateEntry;
    }
}