package com.hotelreservation.repository;

import com.hotelreservation.entity.Booking;
import com.hotelreservation.entity.BookingStatus;
import com.hotelreservation.entity.Hotel;
import com.hotelreservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Booking Repository - Implements Spring Data JPA (BO3 - Spring Data)
 * Provides CRUD operations and custom queries for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find bookings by user
     * @param user the user to search for
     * @return List of bookings for the specified user
     */
    List<Booking> findByUser(User user);
    
    /**
     * Find bookings by hotel
     * @param hotel the hotel to search for
     * @return List of bookings for the specified hotel
     */
    List<Booking> findByHotel(Hotel hotel);
    
    /**
     * Find bookings by status
     * @param status the booking status to search for
     * @return List of bookings with the specified status
     */
    List<Booking> findByStatus(BookingStatus status);
    
    /**
     * Find active bookings by user
     * @param user the user to search for
     * @return List of active bookings for the specified user
     */
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
    
    /**
     * Find active bookings by hotel
     * @param hotel the hotel to search for
     * @return List of active bookings for the specified hotel
     */
    List<Booking> findByHotelAndStatus(Hotel hotel, BookingStatus status);
    
    /**
     * Check for overlapping bookings for a specific hotel
     * This query prevents double booking by checking date overlaps
     * @param hotelId the hotel ID to check
     * @param checkInDate the check-in date to validate
     * @param checkOutDate the check-out date to validate
     * @param status the booking status to check (usually ACTIVE)
     * @return List of overlapping bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId " +
           "AND b.status = :status " +
           "AND NOT (b.checkOutDate <= :checkInDate OR b.checkInDate >= :checkOutDate)")
    List<Booking> findOverlappingBookings(@Param("hotelId") Long hotelId, 
                                         @Param("checkInDate") LocalDate checkInDate, 
                                         @Param("checkOutDate") LocalDate checkOutDate, 
                                         @Param("status") BookingStatus status);
    
    /**
     * Find bookings by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return List of bookings within the date range
     */
    @Query("SELECT b FROM Booking b WHERE b.checkInDate >= :startDate AND b.checkOutDate <= :endDate")
    List<Booking> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Find all bookings with user and hotel details
     * @return List of all bookings with joined user and hotel data
     */
    @Query("SELECT b FROM Booking b JOIN FETCH b.user JOIN FETCH b.hotel ORDER BY b.createdAt DESC")
    List<Booking> findAllWithUserAndHotel();
    
    /**
     * Count active bookings for a hotel
     * @param hotelId the hotel ID
     * @return Number of active bookings for the hotel
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.hotel.id = :hotelId AND b.status = :status")
    long countActiveBookingsByHotel(@Param("hotelId") Long hotelId, @Param("status") BookingStatus status);
}

