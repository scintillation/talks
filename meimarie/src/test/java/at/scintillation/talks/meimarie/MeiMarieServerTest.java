package at.scintillation.talks.meimarie;

import at.scintillation.talks.meimarie.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
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

//    @Mock
//    private TransactionRepository repo = new TransactionRepository();
//
//    private MeiMarieServer ctrl;
//
//    private MockMvc mvc;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        ctrl = new MeiMarieServer(repo);
//        this.mvc = MockMvcBuilders.standaloneSetup(ctrl).build();
//    }
//
//
//
//    @Test
//    public void testList() throws Exception {
//        when(repo.findAll()).thenReturn(new Integer(1));
//        mvc.perform(get("/api/transaction")
//                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$").value(equalTo(1)));
//
//        verify(repo, times(1)).findAll();
//    }
//
//    @Test
//    public void testGet() throws Exception {
//        when(repo.findById(anyString())).thenReturn(new Integer(1));
//        mvc.perform(get("/api/transaction")
//                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$").value(equalTo(1)));
//
//        verify(repo, times(1)).findAll();
//    }
//
//    @Test
//    public void testAdd() throws Exception {
//        when(repo.save(any(Transaction.class))).thenReturn(new Integer(1));
//        mvc.perform(get("/api/transaction")
//                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$").value(equalTo(1)));
//        verify(repo, times(1)).findAll();
//    }

    @Test
    public void testStats() throws Exception {

    }
}