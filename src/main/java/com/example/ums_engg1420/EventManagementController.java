package com.example.ums_engg1420;

public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public void addEvent(Event event) {
        eventService.addEvent(event);
    }

    public void editEvent(String eventCode, Event updatedEvent) {
        eventService.editEvent(eventCode, updatedEvent);
    }

    public void deleteEvent(String eventCode) {
        eventService.deleteEvent(eventCode);
    }

    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    public Event getEventByCode(String eventCode) {
        return eventService.getEventByCode(eventCode);
    }

    public boolean registerStudentForEvent(String eventCode, String studentId) {
        Event event = eventService.getEventByCode(eventCode);
        if (event != null) {
            return event.registerStudent(studentId);
        }
        return false;
    }
}