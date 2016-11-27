package com.elarib.lowcosttrip.web.rest;

import com.elarib.lowcosttrip.LowCostTripApp;

import com.elarib.lowcosttrip.domain.RestaurantReservation;
import com.elarib.lowcosttrip.domain.User;
import com.elarib.lowcosttrip.repository.RestaurantReservationRepository;

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
 * Test class for the RestaurantReservationResource REST controller.
 *
 * @see RestaurantReservationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LowCostTripApp.class)
public class RestaurantReservationResourceIntTest {

    private static final String DEFAULT_ID_RESTO = "AAAAA";
    private static final String UPDATED_ID_RESTO = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private RestaurantReservationRepository restaurantReservationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRestaurantReservationMockMvc;

    private RestaurantReservation restaurantReservation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RestaurantReservationResource restaurantReservationResource = new RestaurantReservationResource();
        ReflectionTestUtils.setField(restaurantReservationResource, "restaurantReservationRepository", restaurantReservationRepository);
        this.restRestaurantReservationMockMvc = MockMvcBuilders.standaloneSetup(restaurantReservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantReservation createEntity(EntityManager em) {
        RestaurantReservation restaurantReservation = new RestaurantReservation()
                .idResto(DEFAULT_ID_RESTO)
                .date(DEFAULT_DATE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        restaurantReservation.setUser(user);
        return restaurantReservation;
    }

    @Before
    public void initTest() {
        restaurantReservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestaurantReservation() throws Exception {
        int databaseSizeBeforeCreate = restaurantReservationRepository.findAll().size();

        // Create the RestaurantReservation

        restRestaurantReservationMockMvc.perform(post("/api/restaurant-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurantReservation)))
                .andExpect(status().isCreated());

        // Validate the RestaurantReservation in the database
        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findAll();
        assertThat(restaurantReservations).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantReservation testRestaurantReservation = restaurantReservations.get(restaurantReservations.size() - 1);
        assertThat(testRestaurantReservation.getIdResto()).isEqualTo(DEFAULT_ID_RESTO);
        assertThat(testRestaurantReservation.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkIdRestoIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantReservationRepository.findAll().size();
        // set the field null
        restaurantReservation.setIdResto(null);

        // Create the RestaurantReservation, which fails.

        restRestaurantReservationMockMvc.perform(post("/api/restaurant-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurantReservation)))
                .andExpect(status().isBadRequest());

        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findAll();
        assertThat(restaurantReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantReservationRepository.findAll().size();
        // set the field null
        restaurantReservation.setDate(null);

        // Create the RestaurantReservation, which fails.

        restRestaurantReservationMockMvc.perform(post("/api/restaurant-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurantReservation)))
                .andExpect(status().isBadRequest());

        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findAll();
        assertThat(restaurantReservations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRestaurantReservations() throws Exception {
        // Initialize the database
        restaurantReservationRepository.saveAndFlush(restaurantReservation);

        // Get all the restaurantReservations
        restRestaurantReservationMockMvc.perform(get("/api/restaurant-reservations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantReservation.getId().intValue())))
                .andExpect(jsonPath("$.[*].idResto").value(hasItem(DEFAULT_ID_RESTO.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRestaurantReservation() throws Exception {
        // Initialize the database
        restaurantReservationRepository.saveAndFlush(restaurantReservation);

        // Get the restaurantReservation
        restRestaurantReservationMockMvc.perform(get("/api/restaurant-reservations/{id}", restaurantReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantReservation.getId().intValue()))
            .andExpect(jsonPath("$.idResto").value(DEFAULT_ID_RESTO.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRestaurantReservation() throws Exception {
        // Get the restaurantReservation
        restRestaurantReservationMockMvc.perform(get("/api/restaurant-reservations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurantReservation() throws Exception {
        // Initialize the database
        restaurantReservationRepository.saveAndFlush(restaurantReservation);
        int databaseSizeBeforeUpdate = restaurantReservationRepository.findAll().size();

        // Update the restaurantReservation
        RestaurantReservation updatedRestaurantReservation = restaurantReservationRepository.findOne(restaurantReservation.getId());
        updatedRestaurantReservation
                .idResto(UPDATED_ID_RESTO)
                .date(UPDATED_DATE);

        restRestaurantReservationMockMvc.perform(put("/api/restaurant-reservations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRestaurantReservation)))
                .andExpect(status().isOk());

        // Validate the RestaurantReservation in the database
        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findAll();
        assertThat(restaurantReservations).hasSize(databaseSizeBeforeUpdate);
        RestaurantReservation testRestaurantReservation = restaurantReservations.get(restaurantReservations.size() - 1);
        assertThat(testRestaurantReservation.getIdResto()).isEqualTo(UPDATED_ID_RESTO);
        assertThat(testRestaurantReservation.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteRestaurantReservation() throws Exception {
        // Initialize the database
        restaurantReservationRepository.saveAndFlush(restaurantReservation);
        int databaseSizeBeforeDelete = restaurantReservationRepository.findAll().size();

        // Get the restaurantReservation
        restRestaurantReservationMockMvc.perform(delete("/api/restaurant-reservations/{id}", restaurantReservation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findAll();
        assertThat(restaurantReservations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
