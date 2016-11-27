package com.elarib.lowcosttrip.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A HotelReservation.
 */
@Entity
@Table(name = "hotel_reservation")
public class HotelReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "id_hotel", nullable = false)
    private String idHotel;

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @NotNull
    @Column(name = "price_per_night", nullable = false)
    private Long pricePerNight;

    @ManyToOne
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdHotel() {
        return idHotel;
    }

    public HotelReservation idHotel(String idHotel) {
        this.idHotel = idHotel;
        return this;
    }

    public void setIdHotel(String idHotel) {
        this.idHotel = idHotel;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public HotelReservation checkIn(LocalDate checkIn) {
        this.checkIn = checkIn;
        return this;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public HotelReservation checkOut(LocalDate checkOut) {
        this.checkOut = checkOut;
        return this;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Long getPricePerNight() {
        return pricePerNight;
    }

    public HotelReservation pricePerNight(Long pricePerNight) {
        this.pricePerNight = pricePerNight;
        return this;
    }

    public void setPricePerNight(Long pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public User getUser() {
        return user;
    }

    public HotelReservation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotelReservation hotelReservation = (HotelReservation) o;
        if(hotelReservation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hotelReservation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HotelReservation{" +
            "id=" + id +
            ", idHotel='" + idHotel + "'" +
            ", checkIn='" + checkIn + "'" +
            ", checkOut='" + checkOut + "'" +
            ", pricePerNight='" + pricePerNight + "'" +
            '}';
    }
}
