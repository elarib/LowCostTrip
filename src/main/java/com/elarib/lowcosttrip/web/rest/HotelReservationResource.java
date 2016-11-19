package com.elarib.lowcosttrip.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.elarib.lowcosttrip.domain.HotelReservation;

import com.elarib.lowcosttrip.repository.HotelReservationRepository;
import com.elarib.lowcosttrip.service.UserService;
import com.elarib.lowcosttrip.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HotelReservation.
 */
@RestController
@RequestMapping("/api")
public class HotelReservationResource {

    private final Logger log = LoggerFactory.getLogger(HotelReservationResource.class);
        
    @Inject
    private HotelReservationRepository hotelReservationRepository;

   @Inject 
   private UserService userService;
    
    /**
     * POST  /hotel-reservations : Create a new hotelReservation.
     *
     * @param hotelReservation the hotelReservation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hotelReservation, or with status 400 (Bad Request) if the hotelReservation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hotel-reservations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HotelReservation> createHotelReservation(@Valid @RequestBody HotelReservation hotelReservation) throws URISyntaxException {
        log.debug("REST request to save HotelReservation : {}", hotelReservation);
        if (hotelReservation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hotelReservation", "idexists", "A new hotelReservation cannot already have an ID")).body(null);
        }
        
        
        HotelReservation result = hotelReservationRepository.save(hotelReservation.user(userService.getUserWithAuthorities()));
        return ResponseEntity.created(new URI("/api/hotel-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hotelReservation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hotel-reservations : Updates an existing hotelReservation.
     *
     * @param hotelReservation the hotelReservation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hotelReservation,
     * or with status 400 (Bad Request) if the hotelReservation is not valid,
     * or with status 500 (Internal Server Error) if the hotelReservation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hotel-reservations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HotelReservation> updateHotelReservation(@Valid @RequestBody HotelReservation hotelReservation) throws URISyntaxException {
        log.debug("REST request to update HotelReservation : {}", hotelReservation);
        if (hotelReservation.getId() == null) {
            return createHotelReservation(hotelReservation);
        }
        HotelReservation result = hotelReservationRepository.save(hotelReservation.user(userService.getUserWithAuthorities()));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hotelReservation", hotelReservation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hotel-reservations : get all the hotelReservations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hotelReservations in body
     */
    @RequestMapping(value = "/hotel-reservations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HotelReservation> getAllHotelReservations() {
        log.debug("REST request to get all HotelReservations");
        List<HotelReservation> hotelReservations = hotelReservationRepository.findByUserIsCurrentUser();
        return hotelReservations;
    }

    /**
     * GET  /hotel-reservations/:id : get the "id" hotelReservation.
     *
     * @param id the id of the hotelReservation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hotelReservation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hotel-reservations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HotelReservation> getHotelReservation(@PathVariable Long id) {
        log.debug("REST request to get HotelReservation : {}", id);
        HotelReservation hotelReservation = hotelReservationRepository.findOne(id);
        return Optional.ofNullable(hotelReservation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hotel-reservations/:id : delete the "id" hotelReservation.
     *
     * @param id the id of the hotelReservation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hotel-reservations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHotelReservation(@PathVariable Long id) {
        log.debug("REST request to delete HotelReservation : {}", id);
        hotelReservationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hotelReservation", id.toString())).build();
    }

}
