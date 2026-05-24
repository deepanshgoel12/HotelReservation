package com.hotelreservation.repository;

import com.hotelreservation.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Hotel Repository - Implements Spring Data JPA (BO3 - Spring Data)
 * Provides CRUD operations and custom queries for Hotel entity
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    /**
     * Find hotels by city
     * @param city the city to search for
     * @return List of hotels in the specified city
     */
    List<Hotel> findByCity(String city);
    
    /**
     * Find hotels by state
     * @param state the state to search for
     * @return List of hotels in the specified state
     */
    List<Hotel> findByState(String state);
    
    /**
     * Find hotels by city and state
     * @param city the city to search for
     * @param state the state to search for
     * @return List of hotels in the specified city and state
     */
    List<Hotel> findByCityAndState(String city, String state);
    
    /**
     * Find active hotels
     * @return List of active hotels
     */
    List<Hotel> findByIsActiveTrue();
    
    /**
     * Find hotels by city and active status
     * @param city the city to search for
     * @return List of active hotels in the specified city
     */
    List<Hotel> findByCityAndIsActiveTrue(String city);
    
    /**
     * Search hotels by name containing keyword
     * @param keyword the keyword to search for in hotel name
     * @return List of hotels with names containing the keyword
     */
    @Query("SELECT h FROM Hotel h WHERE h.hotelName LIKE %:keyword% AND h.isActive = true")
    List<Hotel> findByNameContaining(@Param("keyword") String keyword);
    
    /**
     * Find hotels by price range
     * @param minPrice minimum price per night
     * @param maxPrice maximum price per night
     * @return List of hotels within the price range
     */
    @Query("SELECT h FROM Hotel h WHERE h.pricePerNight BETWEEN :minPrice AND :maxPrice AND h.isActive = true")
    List<Hotel> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}

