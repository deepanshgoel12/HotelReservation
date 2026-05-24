package com.hotelreservation.controller;

import com.hotelreservation.entity.Booking;
import com.hotelreservation.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Booking Controller - Implements Spring MVC (BO3 - Spring Core)
 * Handles booking-related operations for users and admins
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    
    private final BookingService bookingService;
    
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    /**
     * Create a new booking
     * @param bookingRequest the booking request data
     * @return the created booking
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody Map<String, Object> bookingRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = Long.valueOf(bookingRequest.get("userId").toString());
            Long hotelId = Long.valueOf(bookingRequest.get("hotelId").toString());
            LocalDate checkInDate = LocalDate.parse(bookingRequest.get("checkInDate").toString());
            LocalDate checkOutDate = LocalDate.parse(bookingRequest.get("checkOutDate").toString());
            
            Booking booking = bookingService.createBooking(userId, hotelId, checkInDate, checkOutDate);
            response.put("success", true);
            response.put("message", "Booking created successfully");
            response.put("booking", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all bookings (admin only)
     * @return list of all bookings
     */
    @GetMapping("/admin/all")
    public ResponseEntity<Map<String, Object>> getAllBookings() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            response.put("success", true);
            response.put("bookings", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get bookings by user
     * @param userId the user ID
     * @return list of bookings for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getBookingsByUser(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getBookingsByUser(userId);
            response.put("success", true);
            response.put("bookings", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch user bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get active bookings by user
     * @param userId the user ID
     * @return list of active bookings for the user
     */
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<Map<String, Object>> getActiveBookingsByUser(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getActiveBookingsByUser(userId);
            response.put("success", true);
            response.put("bookings", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch active user bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get bookings by hotel
     * @param hotelId the hotel ID
     * @return list of bookings for the hotel
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<Map<String, Object>> getBookingsByHotel(@PathVariable Long hotelId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getBookingsByHotel(hotelId);
            response.put("success", true);
            response.put("bookings", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch hotel bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get booking by ID
     * @param id the booking ID
     * @return the booking if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookingById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Booking> booking = bookingService.findById(id);
            if (booking.isPresent()) {
                response.put("success", true);
                response.put("booking", booking.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Booking not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Cancel a booking
     * @param id the booking ID to cancel
     * @param cancelRequest the cancellation request with user ID
     * @return the cancelled booking
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelBooking(@PathVariable Long id, 
                                                           @RequestBody Map<String, Object> cancelRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = Long.valueOf(cancelRequest.get("userId").toString());
            Booking booking = bookingService.cancelBooking(id, userId);
            response.put("success", true);
            response.put("message", "Booking cancelled successfully");
            response.put("booking", booking);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to cancel booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Complete a booking (admin only)
     * @param id the booking ID to complete
     * @return the completed booking
     */
    @PutMapping("/admin/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeBooking(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Booking booking = bookingService.completeBooking(id);
            response.put("success", true);
            response.put("message", "Booking completed successfully");
            response.put("booking", booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to complete booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check hotel availability
     * @param hotelId the hotel ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return availability status
     */
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(@RequestParam Long hotelId,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isAvailable = bookingService.isHotelAvailable(hotelId, checkInDate, checkOutDate);
            response.put("success", true);
            response.put("available", isAvailable);
            response.put("message", isAvailable ? "Hotel is available" : "Hotel is not available for the selected dates");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to check availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

