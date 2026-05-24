package com.hotelreservation.controller;

import com.hotelreservation.entity.Hotel;
import com.hotelreservation.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Hotel Controller - Implements Spring MVC (BO3 - Spring Core)
 * Handles hotel-related operations for both users and admins
 */
@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "http://localhost:3000")
public class HotelController {
    
    private final HotelService hotelService;
    
    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }
    
    /**
     * Get all active hotels (for users)
     * @return list of active hotels
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllActiveHotels() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Hotel> hotels = hotelService.getActiveHotels();
            response.put("success", true);
            response.put("hotels", hotels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch hotels: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all hotels (for admin)
     * @return list of all hotels
     */
    @GetMapping("/admin/all")
    public ResponseEntity<Map<String, Object>> getAllHotels() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Hotel> hotels = hotelService.getAllHotels();
            response.put("success", true);
            response.put("hotels", hotels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch hotels: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get hotel by ID
     * @param id the hotel ID
     * @return the hotel if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHotelById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Hotel> hotel = hotelService.findById(id);
            if (hotel.isPresent()) {
                response.put("success", true);
                response.put("hotel", hotel.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Hotel not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch hotel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search hotels by city
     * @param city the city to search for
     * @return list of hotels in the city
     */
    @GetMapping("/search/city")
    public ResponseEntity<Map<String, Object>> searchByCity(@RequestParam String city) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Hotel> hotels = hotelService.findByCity(city);
            response.put("success", true);
            response.put("hotels", hotels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to search hotels: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search hotels by state
     * @param state the state to search for
     * @return list of hotels in the state
     */
    @GetMapping("/search/state")
    public ResponseEntity<Map<String, Object>> searchByState(@RequestParam String state) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Hotel> hotels = hotelService.findByState(state);
            response.put("success", true);
            response.put("hotels", hotels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to search hotels: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search hotels by name
     * @param keyword the keyword to search for
     * @return list of hotels matching the keyword
     */
    @GetMapping("/search/name")
    public ResponseEntity<Map<String, Object>> searchByName(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Hotel> hotels = hotelService.searchByName(keyword);
            response.put("success", true);
            response.put("hotels", hotels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to search hotels: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Add new hotel (admin only)
     * @param hotel the hotel to add
     * @return the created hotel
     */
    @PostMapping("/admin/add")
    public ResponseEntity<Map<String, Object>> addHotel(@RequestBody Hotel hotel) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Hotel savedHotel = hotelService.addHotel(hotel);
            response.put("success", true);
            response.put("message", "Hotel added successfully");
            response.put("hotel", savedHotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to add hotel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update hotel (admin only)
     * @param id the hotel ID
     * @param hotel the updated hotel data
     * @return the updated hotel
     */
    @PutMapping("/admin/{id}")
    public ResponseEntity<Map<String, Object>> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            hotel.setId(id);
            Hotel updatedHotel = hotelService.updateHotel(hotel);
            response.put("success", true);
            response.put("message", "Hotel updated successfully");
            response.put("hotel", updatedHotel);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update hotel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Delete hotel (admin only)
     * @param id the hotel ID to delete
     * @return success message
     */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, Object>> deleteHotel(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            hotelService.deleteHotel(id);
            response.put("success", true);
            response.put("message", "Hotel deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete hotel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Permanently delete hotel (admin only)
     * @param id the hotel ID to permanently delete
     * @return success message
     */
    @DeleteMapping("/admin/{id}/permanent")
    public ResponseEntity<Map<String, Object>> permanentlyDeleteHotel(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            hotelService.permanentlyDeleteHotel(id);
            response.put("success", true);
            response.put("message", "Hotel permanently deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to permanently delete hotel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

