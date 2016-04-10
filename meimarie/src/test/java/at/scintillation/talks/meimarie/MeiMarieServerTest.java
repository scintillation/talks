package at.scintillation.talks.meimarie;

import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.dto.TransactionType;
import at.scintillation.talks.meimarie.repository.TransactionRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
public class MeiMarieServerTest {

    @Mock
    private TransactionRepository repo;
    private MeiMarieServer ctrl;
    private MockMvc mvc;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ctrl = new MeiMarieServer();
        ctrl.setRepo(repo);
        this.mvc = MockMvcBuilders.standaloneSetup(ctrl).build();
    }

    @Test
    public void testList() throws Exception {
        when(repo.findAll(any(Sort.class))).thenReturn(Arrays.asList(
                new Transaction(UUID.randomUUID().toString(), sdf.parse("07.04.2016"), 970.00,
                        TransactionType.WIRE_TRANSFER, Arrays.asList("#haus", "#möbel", "#garten")),
                new Transaction(UUID.randomUUID().toString(), sdf.parse("03.04.2016"), -10.00,
                        TransactionType.CREDIT_CARD, Arrays.asList("#mousepad", "#edv", "#gaming")),
                new Transaction(UUID.randomUUID().toString(), sdf.parse("01.04.2016"), -23.56,
                        TransactionType.CASH, Arrays.asList("#essen", "#inder", "#linz"))
        ));
        mvc.perform(get("/api/transaction")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].date").value(hasItems(1459461600000L, 1459634400000l, 1459980000000l)))
                .andExpect(jsonPath("$[0].tags").value(hasItems("#haus", "#möbel", "#garten")));

        verify(repo, times(1)).findAll(any(Sort.class));
    }

}