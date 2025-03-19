package com.example.ums_engg1420.dataclasses;

public class Event {
    private int eventId;
    private String eventName;
    private String eventCode;
    private String description;
    private String headerImage;
    private String location;
    private String dateTime;
    private int capacity;
    private double cost;
    private String registeredStudents;

    public Event(int eventId, String eventName, String eventCode, String description, String headerImage,
                 String location, String dateTime, int capacity, double cost, String registeredStudents) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventCode = eventCode;
        this.description = description;
        this.headerImage = headerImage;
        this.location = location;
        this.dateTime = dateTime;
        this.capacity = capacity;
        this.cost = cost;
        this.registeredStudents = registeredStudents;
    }

    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getEventCode() { return eventCode; }
    public String getDescription() { return description; }
    public String getHeaderImage() { return headerImage; }
    public String getLocation() { return location; }
    public String getDateTime() { return dateTime; }
    public int getCapacity() { return capacity; }
    public double getCost() { return cost; }
    public String getRegisteredStudents() { return registeredStudents; }
}
