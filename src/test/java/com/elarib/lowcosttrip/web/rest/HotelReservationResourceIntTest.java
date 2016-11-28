package com.elarib.lowcosttrip.web.rest;

import com.elarib.lowcosttrip.LowCostTripApp;

import com.elarib.lowcosttrip.domain.HotelReservation;
import com.elarib.lowcosttrip.domain.User;
import com.elarib.lowcosttrip.repository.HotelReservationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HotelReservationResource REST controller.
 *
 * @see HotelReservationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LowCostTripApp.class)
public class HotelReservationResourceIntTest {

    private static final String DEFAULT_ID_HOTEL = "AAAAA";
    private static final String UPDATED_ID_HOTEL = "BBBBB";

    private static final LocalDate DEFAULT_CHECK_IN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECK_IN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CHECK_OUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECK_OUT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PRICE_PER_NIGHT = 1L;
    private static final Long UPDATED_PRICE_PER_NIGHT = 2L;

    private static final String DEFAULT_COORD = "AAAAA";
    private static final String UPDATED_COORD = "BBBBB";

    @Inject
    private HotelReservationRepository hotelReservationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHotelReservationMockMvc;

    private HotelReservation hotelReservation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HotelReservationResource hotelReservationResource = new HotelReservationResource();
        ReflectionTestUtils.setField(hotelReservationResource, "hotelReservationRepository", hotelReservationRepository);
        this.restHotelReservationMockMvc = MockMvcBuilders.standaloneSetup(hotelReservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HotelReservation createEntity(EntityManager em) {
        HotelReservation hotelReservation = new HotelReservation()
                .idHotel(DEFAULT_ID_HOTEL)
                .checkIn(DEFAULT_CHECK_IN)
                .checkOut(DEFAULT_CHECK_OUT)
                .pricePerNight(DEFAULT_PRICE_PER_NIGHT)
                .coord(DEFAULT_COORD);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        hotelReservation.setUser(user);
        return hotelReservation;
    }

    @Before
    public void initTest() {
        hotelReservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createHotelReservation() throws Exception {
        int databaseSizeBeforeCreate = hotelReservationRepository.findAll().size();

        // Create the HotelReservation

        restHotelReservationMockMvc.perform(post("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotelReservation)))
                .andExpect(status().isCreated());

        // Validate the HotelReservation in the database
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeCreate + 1);
        HotelReservation testHotelReservation = hotelReservations.get(hotelReservations.size() - 1);
        assertThat(testHotelReservation.getIdHotel()).isEqualTo(DEFAULT_ID_HOTEL);
        assertThat(testHotelReservation.getCheckIn()).isEqualTo(DEFAULT_CHECK_IN);
        assertThat(testHotelReservation.getCheckOut()).isEqualTo(DEFAULT_CHECK_OUT);
        assertThat(testHotelReservation.getPricePerNight()).isEqualTo(DEFAULT_PRICE_PER_NIGHT);
        assertThat(testHotelReservation.getCoord()).isEqualTo(DEFAULT_COORD);
    }

    @Test
    @Transactional
    public void checkIdHotelIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelReservationRepository.findAll().size();
        // set the field null
        hotelReservation.setIdHotel(null);

        // Create the HotelReservation, which fails.

        restHotelReservationMockMvc.perform(post("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotelReservation)))
                .andExpect(status().isBadRequest());

        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckInIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelReservationRepository.findAll().size();
        // set the field null
        hotelReservation.setCheckIn(null);

        // Create the HotelReservation, which fails.

        restHotelReservationMockMvc.perform(post("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotelReservation)))
                .andExpect(status().isBadRequest());

        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckOutIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelReservationRepository.findAll().size();
        // set the field null
        hotelReservation.setCheckOut(null);

        // Create the HotelReservation, which fails.

        restHotelReservationMockMvc.perform(post("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotelReservation)))
                .andExpect(status().isBadRequest());

        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricePerNightIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelReservationRepository.findAll().size();
        // set the field null
        hotelReservation.setPricePerNight(null);

        // Create the HotelReservation, which fails.

        restHotelReservationMockMvc.perform(post("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotelReservation)))
                .andExpect(status().isBadRequest());

        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelReservationRepository.findAll().size();
        // set the field null
        hotelReservation.setCoord(null);

        // Create the HotelReservation, which fails.

        restHotelReservationMockMvc.perform(post("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotelReservation)))
                .andExpect(status().isBadRequest());

        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHotelReservations() throws Exception {
        // Initialize the database
        hotelReservationRepository.saveAndFlush(hotelReservation);

        // Get all the hotelReservations
        restHotelReservationMockMvc.perform(get("/api/hotel-reservations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hotelReservation.getId().intValue())))
                .andExpect(jsonPath("$.[*].idHotel").value(hasItem(DEFAULT_ID_HOTEL.toString())))
                .andExpect(jsonPath("$.[*].checkIn").value(hasItem(DEFAULT_CHECK_IN.toString())))
                .andExpect(jsonPath("$.[*].checkOut").value(hasItem(DEFAULT_CHECK_OUT.toString())))
                .andExpect(jsonPath("$.[*].pricePerNight").value(hasItem(DEFAULT_PRICE_PER_NIGHT.intValue())))
                .andExpect(jsonPath("$.[*].coord").value(hasItem(DEFAULT_COORD.toString())));
    }

    @Test
    @Transactional
    public void getHotelReservation() throws Exception {
        // Initialize the database
        hotelReservationRepository.saveAndFlush(hotelReservation);

        // Get the hotelReservation
        restHotelReservationMockMvc.perform(get("/api/hotel-reservations/{id}", hotelReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hotelReservation.getId().intValue()))
            .andExpect(jsonPath("$.idHotel").value(DEFAULT_ID_HOTEL.toString()))
            .andExpect(jsonPath("$.checkIn").value(DEFAULT_CHECK_IN.toString()))
            .andExpect(jsonPath("$.checkOut").value(DEFAULT_CHECK_OUT.toString()))
            .andExpect(jsonPath("$.pricePerNight").value(DEFAULT_PRICE_PER_NIGHT.intValue()))
            .andExpect(jsonPath("$.coord").value(DEFAULT_COORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHotelReservation() throws Exception {
        // Get the hotelReservation
        restHotelReservationMockMvc.perform(get("/api/hotel-reservations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotelReservation() throws Exception {
        // Initialize the database
        hotelReservationRepository.saveAndFlush(hotelReservation);
        int databaseSizeBeforeUpdate = hotelReservationRepository.findAll().size();

        // Update the hotelReservation
        HotelReservation updatedHotelReservation = hotelReservationRepository.findOne(hotelReservation.getId());
        updatedHotelReservation
                .idHotel(UPDATED_ID_HOTEL)
                .checkIn(UPDATED_CHECK_IN)
                .checkOut(UPDATED_CHECK_OUT)
                .pricePerNight(UPDATED_PRICE_PER_NIGHT)
                .coord(UPDATED_COORD);

        restHotelReservationMockMvc.perform(put("/api/hotel-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHotelReservation)))
                .andExpect(status().isOk());

        // Validate the HotelReservation in the database
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeUpdate);
        HotelReservation testHotelReservation = hotelReservations.get(hotelReservations.size() - 1);
        assertThat(testHotelReservation.getIdHotel()).isEqualTo(UPDATED_ID_HOTEL);
        assertThat(testHotelReservation.getCheckIn()).isEqualTo(UPDATED_CHECK_IN);
        assertThat(testHotelReservation.getCheckOut()).isEqualTo(UPDATED_CHECK_OUT);
        assertThat(testHotelReservation.getPricePerNight()).isEqualTo(UPDATED_PRICE_PER_NIGHT);
        assertThat(testHotelReservation.getCoord()).isEqualTo(UPDATED_COORD);
    }

    @Test
    @Transactional
    public void deleteHotelReservation() throws Exception {
        // Initialize the database
        hotelReservationRepository.saveAndFlush(hotelReservation);
        int databaseSizeBeforeDelete = hotelReservationRepository.findAll().size();

        // Get the hotelReservation
        restHotelReservationMockMvc.perform(delete("/api/hotel-reservations/{id}", hotelReservation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll();
        assertThat(hotelReservations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
