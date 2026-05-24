package com.hotelreservation.entity;

/**
 * Booking Status Enum
 * Defines the possible states of a booking
 */
public enum BookingStatus {
    ACTIVE("Active"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");
    
    private final String displayName;
    
    BookingStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

