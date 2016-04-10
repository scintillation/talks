package at.scintillation.talks.meimarie;

import at.scintillation.talks.meimarie.dto.Stats;
import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.dto.TransactionType;
import at.scintillation.talks.meimarie.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "at.scintillation.talks.meimarie.repository")
@RestController
@RequestMapping(path = "/api")
public class MeiMarieServer {

    private static final Logger logger = LoggerFactory.getLogger(MeiMarieServer.class);


    @Autowired
    private TransactionRepository repo;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(MeiMarieServer.class)
                .run(args);
    }

    @RequestMapping(path = "/transaction/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Transaction get(@PathParam("uuid") UUID uuid) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return new Transaction(uuid.toString(), format.parse("12.04.2016"), -24.95,
                TransactionType.CASH, Arrays.asList("#groceries", "merkur", "diner"));
    }

    @RequestMapping(path = "/transaction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Iterable<Transaction> list() throws ParseException {
        return repo.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "date")));
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
