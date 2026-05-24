package com.hotelreservation.service;

import com.hotelreservation.entity.Hotel;
import com.hotelreservation.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Hotel Service - Implements Spring Core IOC and Dependency Injection (BO3 - Spring Core)
 * Provides business logic for hotel operations
 */
@Service
@Transactional
public class HotelService {
    
    private final HotelRepository hotelRepository;
    
    // Constructor injection (BO3 - Spring Core Dependency Injection)
    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    
    /**
     * Add a new hotel
     * @param hotel the hotel to add
     * @return the saved hotel
     */
    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
    
    /**
     * Get all hotels
     * @return list of all hotels
     */
    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    
    /**
     * Get all active hotels
     * @return list of active hotels
     */
    @Transactional(readOnly = true)
    public List<Hotel> getActiveHotels() {
        return hotelRepository.findByIsActiveTrue();
    }
    
    /**
     * Find hotel by ID
     * @param id the hotel ID
     * @return the hotel if found
     */
    @Transactional(readOnly = true)
    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }
    
    /**
     * Find hotels by city
     * @param city the city to search for
     * @return list of hotels in the city
     */
    @Transactional(readOnly = true)
    public List<Hotel> findByCity(String city) {
        return hotelRepository.findByCityAndIsActiveTrue(city);
    }
    
    /**
     * Find hotels by state
     * @param state the state to search for
     * @return list of hotels in the state
     */
    @Transactional(readOnly = true)
    public List<Hotel> findByState(String state) {
        return hotelRepository.findByState(state);
    }
    
    /**
     * Find hotels by city and state
     * @param city the city to search for
     * @param state the state to search for
     * @return list of hotels in the city and state
     */
    @Transactional(readOnly = true)
    public List<Hotel> findByCityAndState(String city, String state) {
        return hotelRepository.findByCityAndState(city, state);
    }
    
    /**
     * Search hotels by name
     * @param keyword the keyword to search for
     * @return list of hotels matching the keyword
     */
    @Transactional(readOnly = true)
    public List<Hotel> searchByName(String keyword) {
        return hotelRepository.findByNameContaining(keyword);
    }
    
    /**
     * Find hotels by price range
     * @param minPrice minimum price per night
     * @param maxPrice maximum price per night
     * @return list of hotels within the price range
     */
    @Transactional(readOnly = true)
    public List<Hotel> findByPriceRange(Double minPrice, Double maxPrice) {
        return hotelRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Update hotel information
     * @param hotel the hotel to update
     * @return the updated hotel
     */
    public Hotel updateHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
    
    /**
     * Delete hotel by ID (soft delete by setting isActive to false)
     * @param id the hotel ID to delete
     */
    public void deleteHotel(Long id) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(id);
        if (hotelOpt.isPresent()) {
            Hotel hotel = hotelOpt.get();
            hotel.setIsActive(false);
            hotelRepository.save(hotel);
        }
    }
    
    /**
     * Permanently delete hotel by ID
     * @param id the hotel ID to delete
     */
    public void permanentlyDeleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
    
    /**
     * Activate hotel
     * @param id the hotel ID to activate
     */
    public void activateHotel(Long id) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(id);
        if (hotelOpt.isPresent()) {
            Hotel hotel = hotelOpt.get();
            hotel.setIsActive(true);
            hotelRepository.save(hotel);
        }
    }
}

