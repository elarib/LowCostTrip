package com.elarib.lowcosttrip.repository;

import com.elarib.lowcosttrip.domain.HotelReservation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HotelReservation entity.
 */
@SuppressWarnings("unused")
public interface HotelReservationRepository extends JpaRepository<HotelReservation,Long> {

    @Query("select hotelReservation from HotelReservation hotelReservation where hotelReservation.user.login = ?#{principal.username}")
    List<HotelReservation> findByUserIsCurrentUser();

}
