package com.hotelreservation.service;

import com.hotelreservation.entity.Booking;
import com.hotelreservation.entity.BookingStatus;
import com.hotelreservation.entity.Hotel;
import com.hotelreservation.entity.User;
import com.hotelreservation.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Booking Service - Implements Spring Core IOC and Dependency Injection (BO3 - Spring Core)
 * Provides business logic for booking operations with overlap prevention
 */
@Service
@Transactional
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final HotelService hotelService;
    private final UserService userService;
    
    // Constructor injection (BO3 - Spring Core Dependency Injection)
    @Autowired
    public BookingService(BookingRepository bookingRepository, 
                         HotelService hotelService, 
                         UserService userService) {
        this.bookingRepository = bookingRepository;
        this.hotelService = hotelService;
        this.userService = userService;
    }
    
    /**
     * Create a new booking with overlap validation
     * @param userId the user ID
     * @param hotelId the hotel ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return the created booking
     * @throws IllegalArgumentException if dates are invalid or hotel is not available
     */
    public Booking createBooking(Long userId, Long hotelId, LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate dates
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Get user and hotel
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with ID: " + hotelId));
        
        // Check for overlapping bookings
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                hotelId, checkInDate, checkOutDate, BookingStatus.ACTIVE);
        
        if (!overlappingBookings.isEmpty()) {
            throw new IllegalArgumentException("Hotel is not available for the selected dates. " +
                    "There are " + overlappingBookings.size() + " overlapping booking(s).");
        }
        
        // Create booking
        Booking booking = new Booking(user, hotel, checkInDate, checkOutDate);
        booking.calculateTotalAmount();
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Get all bookings
     * @return list of all bookings
     */
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllWithUserAndHotel();
    }
    
    /**
     * Get bookings by user
     * @param userId the user ID
     * @return list of bookings for the user
     */
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUser(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return bookingRepository.findByUser(user);
    }
    
    /**
     * Get active bookings by user
     * @param userId the user ID
     * @return list of active bookings for the user
     */
    @Transactional(readOnly = true)
    public List<Booking> getActiveBookingsByUser(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return bookingRepository.findByUserAndStatus(user, BookingStatus.ACTIVE);
    }
    
    /**
     * Get bookings by hotel
     * @param hotelId the hotel ID
     * @return list of bookings for the hotel
     */
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByHotel(Long hotelId) {
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with ID: " + hotelId));
        return bookingRepository.findByHotel(hotel);
    }
    
    /**
     * Get active bookings by hotel
     * @param hotelId the hotel ID
     * @return list of active bookings for the hotel
     */
    @Transactional(readOnly = true)
    public List<Booking> getActiveBookingsByHotel(Long hotelId) {
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with ID: " + hotelId));
        return bookingRepository.findByHotelAndStatus(hotel, BookingStatus.ACTIVE);
    }
    
    /**
     * Find booking by ID
     * @param id the booking ID
     * @return the booking if found
     */
    @Transactional(readOnly = true)
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
    
    /**
     * Cancel a booking
     * @param bookingId the booking ID to cancel
     * @param userId the user ID (for authorization)
     * @return the cancelled booking
     * @throws IllegalArgumentException if booking not found or not authorized
     */
    public Booking cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        
        // Check if user owns the booking
        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to cancel this booking");
        }
        
        // Check if booking can be cancelled
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active bookings can be cancelled");
        }
        
        if (booking.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot cancel a booking that has already started");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Complete a booking (mark as completed)
     * @param bookingId the booking ID to complete
     * @return the completed booking
     */
    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active bookings can be completed");
        }
        
        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Check hotel availability for given dates
     * @param hotelId the hotel ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if hotel is available, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isHotelAvailable(Long hotelId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                hotelId, checkInDate, checkOutDate, BookingStatus.ACTIVE);
        return overlappingBookings.isEmpty();
    }
    
    /**
     * Get booking statistics for a hotel
     * @param hotelId the hotel ID
     * @return number of active bookings
     */
    @Transactional(readOnly = true)
    public long getActiveBookingCount(Long hotelId) {
        return bookingRepository.countActiveBookingsByHotel(hotelId, BookingStatus.ACTIVE);
    }
}

