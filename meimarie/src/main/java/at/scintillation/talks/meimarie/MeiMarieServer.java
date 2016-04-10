package at.scintillation.talks.meimarie;

import at.scintillation.talks.meimarie.dto.Stats;
import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.dto.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
@SpringBootApplication
@RestController
@RequestMapping(path = "/api")
public class MeiMarieServer {

    private static final Logger logger = LoggerFactory.getLogger(MeiMarieServer.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(MeiMarieServer.class)
                .run(args);
    }

    @RequestMapping(path = "/transaction/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Transaction list(@PathParam("uuid") UUID uuid) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return new Transaction(uuid.toString(), format.parse("12.04.2016"), -24.95,
                TransactionType.CASH, Arrays.asList("#groceries", "merkur", "diner"));
    }

    @RequestMapping(path = "/transaction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Transaction> list() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return  Arrays.asList(
                new Transaction(UUID.randomUUID().toString(), format.parse("12.04.2016"), -24.95,
                        TransactionType.CASH, Arrays.asList("#groceries", "#merkur", "#diner")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.01.2016"), -4.95,
                        TransactionType.CASH, Arrays.asList("#obi", "#lightbulb", "#livingroom")),
                new Transaction(UUID.randomUUID().toString(), format.parse("01.03.2016"), 100.00,
                        TransactionType.CASH, Arrays.asList("#bday", "#present", "#grandparents"))
        );
    }

    @RequestMapping(path = "/transaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody Transaction dto) {
        logger.info(dto.toString());
    }

    @RequestMapping(path = "/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Stats stats() {
        return new Stats();
    }

}
