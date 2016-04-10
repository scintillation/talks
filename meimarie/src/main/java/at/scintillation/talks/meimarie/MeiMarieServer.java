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
        List<Transaction> transactions = Arrays.asList(
                new Transaction(UUID.randomUUID().toString(), format.parse("12.01.2016"), -24.95,
                        TransactionType.CASH, Arrays.asList("#groceries", "#merkur", "#diner")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.02.2016"), -4.95,
                        TransactionType.WIRE_TRANSFER, Arrays.asList("#obi")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.03.2016"), -4.95,
                        TransactionType.CASH, Arrays.asList("#food", "#pasta")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.04.2016"), 20.00,
                        TransactionType.WIRE_TRANSFER, Arrays.asList("#atm")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.05.2016"), -1.13,
                        TransactionType.CREDIT_CARD, Arrays.asList("#coke", "#food")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.06.2016"), -12.95,
                        TransactionType.CASH, Arrays.asList("#obi", "#lightbulb", "#livingroom", "#test")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.07.2016"), -14.51,
                        TransactionType.CASH, Arrays.asList("#obi", "#lightbulb", "#livingroom")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.08.2016"), -0.22,
                        TransactionType.WIRE_TRANSFER, Arrays.asList("#piggy bank")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.09.2016"), -122.95,
                        TransactionType.CASH, Arrays.asList("#sth")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.10.2016"), -4.95,
                        TransactionType.CASH, Arrays.asList("#obi", "#lightbulb", "#livingroom")),
                new Transaction(UUID.randomUUID().toString(), format.parse("04.11.2016"), -4.95,
                        TransactionType.CREDIT_CARD, Arrays.asList("#obi", "#lightbulb", "#livingroom")),
                new Transaction(UUID.randomUUID().toString(), format.parse("01.12.2016"), 100.00,
                        TransactionType.CASH, Arrays.asList("#bday", "#present", "#grandparents"))
        );
        transactions.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()) * (-1));
        return transactions;
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
