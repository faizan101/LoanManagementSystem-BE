package com.dtone.lending;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.Loans;
import com.dtone.lending.domain.SubCategory;
import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.domain.enumeration.LoanStatus;
import com.dtone.lending.repository.LoanProductsRepository;
import com.dtone.lending.repository.LoansRepository;
import com.dtone.lending.repository.UserProfileRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.hasItem;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
@SpringBootTest(classes = LendingApplication.class)
class LoansResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final LoanStatus DEFAULT_STATUS = LoanStatus.INITIATED;
    private static final LoanStatus UPDATED_STATUS = LoanStatus.INREVIEW;

    private static final BigDecimal DEFAULT_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FEE = new BigDecimal(2);

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.now();
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now();

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.now();
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now();


    private static final String ENTITY_API_URL = "/api/loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private LoanProductsRepository loanProductsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoansMockMvc;

    private Loans loans;

    private UserProfile userProfile;

    private LoanProducts loanProducts;

    private SubCategory subCategory;


    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loans createEntity(EntityManager em) {
        Loans loans = new Loans()
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .fee(DEFAULT_FEE)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return loans;
    }

    public static UserProfile createUserProfileEntity(EntityManager em) {
        UserProfile userProfile = UserProfile.builder()
                .id(1l)
                .username("user1@gmail.com")
                .password("password")
                .roles(Arrays.asList( "ROLE_USER"))
                .build();
        return userProfile;
    }

    public static LoanProducts createLoanProductsEntity(EntityManager em) {
        LoanProducts loanProducts = new LoanProducts();
        loanProducts.setId(1l);
        loanProducts.setDuration(10);
        return loanProducts;
    }


    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loans createUpdatedEntity(EntityManager em) {
        Loans loans = new Loans()
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .fee(UPDATED_FEE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return loans;
    }

    @BeforeEach
    public void initTest() {
//        Authentication authentication = Mockito.mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(userProfile);
        loans = createEntity(em);
        loanProducts = createLoanProductsEntity(em);
        userProfile = createUserProfileEntity(em);
    }

    @Test
    @Transactional
    void createLoans() throws Exception {
        // Initialize the database
        //loanProductsRepository.saveAndFlush(loanProducts);
        //userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeCreate = loansRepository.findAll().size();

        Map<Object, Object> objectObjectMap = new HashMap<>();
        objectObjectMap.put("amount",2);
        objectObjectMap.put("productId", 1);
        objectObjectMap.put("catId", 1);


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userProfile);
        Mockito.when(authentication.getPrincipal()).thenReturn(userProfile);


        // Create the Loans
        restLoansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(objectObjectMap)))
            .andExpect(status().isCreated());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeCreate + 1);
        Loans testLoans = loansList.get(loansList.size() - 1);
        assertThat(testLoans.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testLoans.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLoans.getFee()).isEqualByComparingTo(DEFAULT_FEE);
        assertThat(testLoans.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testLoans.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLoans() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        // Get all the loansList
        restLoansMockMvc
            .perform(get(ENTITY_API_URL))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loans.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getLoans() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        // Get the loans
        restLoansMockMvc
            .perform(get(ENTITY_API_URL_ID, loans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loans.getId().intValue()))
//            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
//            .andExpect(jsonPath("$.fee").value(sameNumber(DEFAULT_FEE)))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
//    void getAllLoansByPaymentIsEqualToSomething() throws Exception {
//        // Initialize the database
//        loansRepository.saveAndFlush(loans);
//        Payment payment = PaymentResourceIT.createEntity(em);
//        em.persist(payment);
//        em.flush();
//        loans.setPayment(payment);
//        loansRepository.saveAndFlush(loans);
//        Long paymentId = payment.getId();
//
//        // Get all the loansList where payment equals to paymentId
//        defaultLoansShouldBeFound("paymentId.equals=" + paymentId);
//
//        // Get all the loansList where payment equals to (paymentId + 1)
//        defaultLoansShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
//    }
//
//    @Test
//    @Transactional
//    void getAllLoansByUserProfileIsEqualToSomething() throws Exception {
//        // Initialize the database
//        loansRepository.saveAndFlush(loans);
//        UserProfile userProfile = UserProfileResourceIT.createEntity(em);
//        em.persist(userProfile);
//        em.flush();
//        loans.setUserProfile(userProfile);
//        loansRepository.saveAndFlush(loans);
//        Long userProfileId = userProfile.getId();
//
//        // Get all the loansList where userProfile equals to userProfileId
//        defaultLoansShouldBeFound("userProfileId.equals=" + userProfileId);
//
//        // Get all the loansList where userProfile equals to (userProfileId + 1)
//        defaultLoansShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
//    }
//
//    @Test
//    @Transactional
//    void getAllLoansByLoanProductsIsEqualToSomething() throws Exception {
//        // Initialize the database
//        loansRepository.saveAndFlush(loans);
//        LoanProducts loanProducts = LoanProductsResourceIT.createEntity(em);
//        em.persist(loanProducts);
//        em.flush();
//        loans.setLoanProducts(loanProducts);
//        loansRepository.saveAndFlush(loans);
//        Long loanProductsId = loanProducts.getId();
//
//        // Get all the loansList where loanProducts equals to loanProductsId
//        defaultLoansShouldBeFound("loanProductsId.equals=" + loanProductsId);
//
//        // Get all the loansList where loanProducts equals to (loanProductsId + 1)
//        defaultLoansShouldNotBeFound("loanProductsId.equals=" + (loanProductsId + 1));
//    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoansShouldBeFound(String filter) throws Exception {
        restLoansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loans.getId().intValue())))
//            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
//            .andExpect(jsonPath("$.[*].fee").value(hasItem(sameNumber(DEFAULT_FEE))))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));

        // Check, that the count call also returns 1
        restLoansMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }



    @Test
    @Transactional
    void getNonExistingLoans() throws Exception {
        // Get the loans
        restLoansMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNonExistingLoans() throws Exception {
        int databaseSizeBeforeUpdate = loansRepository.findAll().size();
        loans.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loans.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(loans))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoans() throws Exception {
        int databaseSizeBeforeUpdate = loansRepository.findAll().size();
        loans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(loans))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoans() throws Exception {
        int databaseSizeBeforeUpdate = loansRepository.findAll().size();
        loans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loans)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoansWithPatch() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        int databaseSizeBeforeUpdate = loansRepository.findAll().size();

        // Update the loans using partial update
        Loans partialUpdatedLoans = new Loans();
        partialUpdatedLoans.setId(loans.getId());

        partialUpdatedLoans.amount(UPDATED_AMOUNT).status(UPDATED_STATUS);

        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoans.getId())
                    .contentType("application/merge-patch+json")
                    .content(mapper.writeValueAsBytes(partialUpdatedLoans))
            )
            .andExpect(status().isOk());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
        Loans testLoans = loansList.get(loansList.size() - 1);
        assertThat(testLoans.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testLoans.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLoans.getFee()).isEqualByComparingTo(DEFAULT_FEE);
        assertThat(testLoans.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testLoans.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLoansWithPatch() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        int databaseSizeBeforeUpdate = loansRepository.findAll().size();

        // Update the loans using partial update
        Loans partialUpdatedLoans = new Loans();
        partialUpdatedLoans.setId(loans.getId());

        partialUpdatedLoans
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .fee(UPDATED_FEE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoans.getId())
                    .contentType("application/merge-patch+json")
                    .content(mapper.writeValueAsBytes(partialUpdatedLoans))
            )
            .andExpect(status().isOk());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
        Loans testLoans = loansList.get(loansList.size() - 1);
        assertThat(testLoans.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testLoans.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLoans.getFee()).isEqualByComparingTo(UPDATED_FEE);
        assertThat(testLoans.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLoans.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLoans() throws Exception {
        int databaseSizeBeforeUpdate = loansRepository.findAll().size();
        loans.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loans.getId())
                    .contentType("application/json")
                    .content(mapper.writeValueAsBytes(loans))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoans() throws Exception {
        int databaseSizeBeforeUpdate = loansRepository.findAll().size();
        loans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(mapper.writeValueAsBytes(loans))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoans() throws Exception {
        int databaseSizeBeforeUpdate = loansRepository.findAll().size();
        loans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(mapper.writeValueAsBytes(loans)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loans in the database
        List<Loans> loansList = loansRepository.findAll();
        assertThat(loansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoans() throws Exception {

        //******* Delete End Point Removed *********
        // Initialize the database
        //loansRepository.saveAndFlush(loans);

        //int databaseSizeBeforeDelete = loansRepository.findAll().size();

        // Delete the loans
        restLoansMockMvc
            .perform(delete(ENTITY_API_URL_ID, loans.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isMethodNotAllowed());

        // Validate the database contains one less item
        //List<Loans> loansList = loansRepository.findAll();
        //assertThat(loansList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
