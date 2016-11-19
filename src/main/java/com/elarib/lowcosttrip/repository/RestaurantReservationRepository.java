package com.elarib.lowcosttrip.repository;

import com.elarib.lowcosttrip.domain.RestaurantReservation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RestaurantReservation entity.
 */
@SuppressWarnings("unused")
public interface RestaurantReservationRepository extends JpaRepository<RestaurantReservation,Long> {

    @Query("select restaurantReservation from RestaurantReservation restaurantReservation where restaurantReservation.user.login = ?#{principal.username}")
    List<RestaurantReservation> findByUserIsCurrentUser();

}
