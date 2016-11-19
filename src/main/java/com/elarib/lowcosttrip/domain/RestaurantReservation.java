package com.elarib.lowcosttrip.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A RestaurantReservation.
 */
@Entity
@Table(name = "restaurant_reservation")
public class RestaurantReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "id_resto", nullable = false)
    private String idResto;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdResto() {
        return idResto;
    }

    public RestaurantReservation idResto(String idResto) {
        this.idResto = idResto;
        return this;
    }

    public void setIdResto(String idResto) {
        this.idResto = idResto;
    }

    public LocalDate getDate() {
        return date;
    }

    public RestaurantReservation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public RestaurantReservation user(User user) {
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
        RestaurantReservation restaurantReservation = (RestaurantReservation) o;
        if(restaurantReservation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, restaurantReservation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RestaurantReservation{" +
            "id=" + id +
            ", idResto='" + idResto + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
