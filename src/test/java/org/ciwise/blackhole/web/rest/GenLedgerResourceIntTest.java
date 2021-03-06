package org.ciwise.blackhole.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.ciwise.blackhole.BlackholeApp;
import org.ciwise.blackhole.domain.GenLedger;
import org.ciwise.blackhole.service.GenLedgerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


/**
 * Test class for the AccountEntryResource REST controller.
 *
 * @see GenLedgerResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = BlackholeApp.class)
//@WebAppConfiguration
//@IntegrationTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlackholeApp.class)
public class GenLedgerResourceIntTest {


//    private static final LocalDate DEFAULT_ENTRYDATE = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_ENTRYDATE = LocalDate.now(ZoneId.systemDefault());
    private static final ZonedDateTime DEFAULT_ENTRYDATE = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
    private static final ZonedDateTime UPDATED_ENTRYDATE = ZonedDateTime.now();


    private static final String DEFAULT_TRANSACTION = "AAAAA";
    private static final String UPDATED_TRANSACTION = "BBBBB";

    private static final Boolean DEFAULT_RECONCILE = false;
    private static final Boolean UPDATED_RECONCILE = true;

    private static final Long DEFAULT_POSTINGREF = 1L;
    private static final Long UPDATED_POSTINGREF = 2L;
    private static final String DEFAULT_DEBIT = "AAAAA";
    private static final String UPDATED_DEBIT = "BBBBB";
    private static final String DEFAULT_CREDIT = "AAAAA";
    private static final String UPDATED_CREDIT = "BBBBB";
    private static final String DEFAULT_DEBITBALANCE = "AAAAA";
    private static final String UPDATED_DEBITBALANCE = "BBBBB";
    private static final String DEFAULT_CREDITBALANCE = "AAAAA";
    private static final String UPDATED_CREDITBALANCE = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";
    private static final String DEFAULT_CNO = "AAAAA";
    private static final String UPDATED_CNO = "BBBBB";

    @Inject
    private GenLedgerService accountEntryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAccountEntryMockMvc;

    private GenLedger accountEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenLedgerResource accountEntryResource = new GenLedgerResource();
        ReflectionTestUtils.setField(accountEntryResource, "accountEntryService", accountEntryService);

        this.restAccountEntryMockMvc = MockMvcBuilders.standaloneSetup(accountEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        //accountEntrySearchRepository.deleteAll();
        accountEntry = new GenLedger();
        accountEntry.setEntrydate(DEFAULT_ENTRYDATE);
        accountEntry.setTransaction(DEFAULT_TRANSACTION);
        accountEntry.setReconcile(DEFAULT_RECONCILE);
        accountEntry.setPostingref(DEFAULT_POSTINGREF);
        accountEntry.setDebit(DEFAULT_DEBIT);
        accountEntry.setCredit(DEFAULT_CREDIT);
        accountEntry.setDebitbalance(DEFAULT_DEBITBALANCE);
        accountEntry.setCreditbalance(DEFAULT_CREDITBALANCE);
        accountEntry.setNotes(DEFAULT_NOTES);
        accountEntry.setCno(DEFAULT_CNO);
    }

    @Test
    @Transactional
    public void createAccountEntry() throws Exception {
        int databaseSizeBeforeCreate = accountEntryService.findAll(new PageRequest(0,1000)).getContent().size();

        // Create the AccountEntry

        restAccountEntryMockMvc.perform(post("/api/account-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountEntry)))
                .andExpect(status().isCreated());

        // Validate the AccountEntry in the database
        Page<GenLedger> accountPageEntries = accountEntryService.findAll(new PageRequest(0,1000));
        List<GenLedger> accountEntries = accountPageEntries.getContent();

        assertThat(accountEntries).hasSize(databaseSizeBeforeCreate + 1);
        GenLedger testAccountEntry = accountEntries.get(accountEntries.size() - 1);
        assertThat(testAccountEntry.getEntrydate()).isEqualTo(DEFAULT_ENTRYDATE);
        assertThat(testAccountEntry.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testAccountEntry.isReconcile()).isEqualTo(DEFAULT_RECONCILE);
        assertThat(testAccountEntry.getPostingref()).isEqualTo(DEFAULT_POSTINGREF);
        assertThat(testAccountEntry.getDebit()).isEqualTo(DEFAULT_DEBIT);
        assertThat(testAccountEntry.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testAccountEntry.getDebitbalance()).isEqualTo(DEFAULT_DEBITBALANCE);
        assertThat(testAccountEntry.getCreditbalance()).isEqualTo(DEFAULT_CREDITBALANCE);
        assertThat(testAccountEntry.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAccountEntry.getCno()).isEqualTo(DEFAULT_CNO);

    }

    @Test
    @Transactional
    public void getAllAccountEntries() throws Exception {
        // Initialize the database
        accountEntryService.save(accountEntry);

        // Get all the accountEntries
        restAccountEntryMockMvc.perform(get("/api/account-entries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accountEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
                .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.toString())))
                .andExpect(jsonPath("$.[*].reconcile").value(hasItem(DEFAULT_RECONCILE.booleanValue())))
                .andExpect(jsonPath("$.[*].postingref").value(hasItem(DEFAULT_POSTINGREF.intValue())))
                .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.toString())))
                .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.toString())))
                .andExpect(jsonPath("$.[*].debitbalance").value(hasItem(DEFAULT_DEBITBALANCE.toString())))
                .andExpect(jsonPath("$.[*].creditbalance").value(hasItem(DEFAULT_CREDITBALANCE.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].cno").value(hasItem(DEFAULT_CNO.toString())));
    }

    @Test
    @Transactional
    public void getAccountEntry() throws Exception {
        // Initialize the database
        accountEntryService.save(accountEntry);

        // Get the accountEntry
        restAccountEntryMockMvc.perform(get("/api/account-entries/{id}", accountEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(accountEntry.getId().intValue()))
            .andExpect(jsonPath("$.entrydate").value(DEFAULT_ENTRYDATE.toString()))
            .andExpect(jsonPath("$.transaction").value(DEFAULT_TRANSACTION.toString()))
            .andExpect(jsonPath("$.reconcile").value(DEFAULT_RECONCILE.booleanValue()))
            .andExpect(jsonPath("$.postingref").value(DEFAULT_POSTINGREF.intValue()))
            .andExpect(jsonPath("$.debit").value(DEFAULT_DEBIT.toString()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.toString()))
            .andExpect(jsonPath("$.debitbalance").value(DEFAULT_DEBITBALANCE.toString()))
            .andExpect(jsonPath("$.creditbalance").value(DEFAULT_CREDITBALANCE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.cno").value(DEFAULT_CNO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountEntry() throws Exception {
        // Get the accountEntry
        restAccountEntryMockMvc.perform(get("/api/account-entries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountEntry() throws Exception {
        // Initialize the database
        accountEntryService.save(accountEntry);

        int databaseSizeBeforeUpdate = accountEntryService.findAll(new PageRequest(0,1000)).getContent().size();

        // Update the accountEntry
        GenLedger updatedAccountEntry = new GenLedger();
        updatedAccountEntry.setId(accountEntry.getId());
        updatedAccountEntry.setEntrydate(UPDATED_ENTRYDATE);
        updatedAccountEntry.setTransaction(UPDATED_TRANSACTION);
        updatedAccountEntry.setReconcile(UPDATED_RECONCILE);
        updatedAccountEntry.setPostingref(UPDATED_POSTINGREF);
        updatedAccountEntry.setDebit(UPDATED_DEBIT);
        updatedAccountEntry.setCredit(UPDATED_CREDIT);
        updatedAccountEntry.setDebitbalance(UPDATED_DEBITBALANCE);
        updatedAccountEntry.setCreditbalance(UPDATED_CREDITBALANCE);
        updatedAccountEntry.setNotes(UPDATED_NOTES);
        updatedAccountEntry.setCno(UPDATED_CNO);

        restAccountEntryMockMvc.perform(put("/api/account-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAccountEntry)))
                .andExpect(status().isOk());

        // Validate the AccountEntry in the database
        Page<GenLedger> accountPageEntries = accountEntryService.findAll(new PageRequest(0,1000));
        List<GenLedger> accountEntries = accountPageEntries.getContent();

        assertThat(accountEntries).hasSize(databaseSizeBeforeUpdate);
        GenLedger testAccountEntry = accountEntries.get(accountEntries.size() - 1);
        assertThat(testAccountEntry.getEntrydate()).isEqualTo(UPDATED_ENTRYDATE);
        assertThat(testAccountEntry.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testAccountEntry.isReconcile()).isEqualTo(UPDATED_RECONCILE);
        assertThat(testAccountEntry.getPostingref()).isEqualTo(UPDATED_POSTINGREF);
        assertThat(testAccountEntry.getDebit()).isEqualTo(UPDATED_DEBIT);
        assertThat(testAccountEntry.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testAccountEntry.getDebitbalance()).isEqualTo(UPDATED_DEBITBALANCE);
        assertThat(testAccountEntry.getCreditbalance()).isEqualTo(UPDATED_CREDITBALANCE);
        assertThat(testAccountEntry.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAccountEntry.getCno()).isEqualTo(UPDATED_CNO);

    }

    @Test
    @Transactional
    public void deleteAccountEntry() throws Exception {
        // Initialize the database
        accountEntryService.save(accountEntry);

        int databaseSizeBeforeDelete = accountEntryService.findAll(new PageRequest(0,1000)).getContent().size();

        // Get the accountEntry
        restAccountEntryMockMvc.perform(delete("/api/account-entries/{id}", accountEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        Page<GenLedger> accountPageEntries = accountEntryService.findAll(new PageRequest(0,1000));
        List<GenLedger> accountEntries = accountPageEntries.getContent();

        assertThat(accountEntries).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccountEntry() throws Exception {
        // Initialize the database
        accountEntryService.save(accountEntry);

        // Search the accountEntry
        restAccountEntryMockMvc.perform(get("/api/_search/account-entries?query=id:" + accountEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
            .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.toString())))
            .andExpect(jsonPath("$.[*].reconcile").value(hasItem(DEFAULT_RECONCILE.booleanValue())))
            .andExpect(jsonPath("$.[*].postingref").value(hasItem(DEFAULT_POSTINGREF.intValue())))
            .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.toString())))
            .andExpect(jsonPath("$.[*].debitbalance").value(hasItem(DEFAULT_DEBITBALANCE.toString())))
            .andExpect(jsonPath("$.[*].creditbalance").value(hasItem(DEFAULT_CREDITBALANCE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].cno").value(hasItem(DEFAULT_CNO.toString())));
    }
}
