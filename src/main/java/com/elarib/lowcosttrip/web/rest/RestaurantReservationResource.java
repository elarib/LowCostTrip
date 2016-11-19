package com.elarib.lowcosttrip.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.elarib.lowcosttrip.domain.RestaurantReservation;

import com.elarib.lowcosttrip.repository.RestaurantReservationRepository;
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
 * REST controller for managing RestaurantReservation.
 */
@RestController
@RequestMapping("/api")
public class RestaurantReservationResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantReservationResource.class);
        
    @Inject
    private RestaurantReservationRepository restaurantReservationRepository;

    /**
     * POST  /restaurant-reservations : Create a new restaurantReservation.
     *
     * @param restaurantReservation the restaurantReservation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new restaurantReservation, or with status 400 (Bad Request) if the restaurantReservation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/restaurant-reservations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RestaurantReservation> createRestaurantReservation(@Valid @RequestBody RestaurantReservation restaurantReservation) throws URISyntaxException {
        log.debug("REST request to save RestaurantReservation : {}", restaurantReservation);
        if (restaurantReservation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("restaurantReservation", "idexists", "A new restaurantReservation cannot already have an ID")).body(null);
        }
        RestaurantReservation result = restaurantReservationRepository.save(restaurantReservation);
        return ResponseEntity.created(new URI("/api/restaurant-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("restaurantReservation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /restaurant-reservations : Updates an existing restaurantReservation.
     *
     * @param restaurantReservation the restaurantReservation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated restaurantReservation,
     * or with status 400 (Bad Request) if the restaurantReservation is not valid,
     * or with status 500 (Internal Server Error) if the restaurantReservation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/restaurant-reservations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RestaurantReservation> updateRestaurantReservation(@Valid @RequestBody RestaurantReservation restaurantReservation) throws URISyntaxException {
        log.debug("REST request to update RestaurantReservation : {}", restaurantReservation);
        if (restaurantReservation.getId() == null) {
            return createRestaurantReservation(restaurantReservation);
        }
        RestaurantReservation result = restaurantReservationRepository.save(restaurantReservation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("restaurantReservation", restaurantReservation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /restaurant-reservations : get all the restaurantReservations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of restaurantReservations in body
     */
    @RequestMapping(value = "/restaurant-reservations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RestaurantReservation> getAllRestaurantReservations() {
        log.debug("REST request to get all RestaurantReservations");
        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findAll();
        return restaurantReservations;
    }

    /**
     * GET  /restaurant-reservations/:id : get the "id" restaurantReservation.
     *
     * @param id the id of the restaurantReservation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the restaurantReservation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/restaurant-reservations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RestaurantReservation> getRestaurantReservation(@PathVariable Long id) {
        log.debug("REST request to get RestaurantReservation : {}", id);
        RestaurantReservation restaurantReservation = restaurantReservationRepository.findOne(id);
        return Optional.ofNullable(restaurantReservation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /restaurant-reservations/:id : delete the "id" restaurantReservation.
     *
     * @param id the id of the restaurantReservation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/restaurant-reservations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRestaurantReservation(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantReservation : {}", id);
        restaurantReservationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("restaurantReservation", id.toString())).build();
    }

}
