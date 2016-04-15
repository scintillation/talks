package at.scintillation.talks.meimarie;

import at.scintillation.talks.meimarie.dto.IntervalType;
import at.scintillation.talks.meimarie.dto.SumPerInterval;
import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.repository.TransactionRepository;
import at.scintillation.talks.meimarie.service.TransactionSearchService;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.facet.result.StatisticalResult;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main class: Spring boot application and MVC controller.
 *
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "at.scintillation.talks.meimarie.repository")
@ComponentScan(basePackages = "at.scintillation.talks.meimarie")
@RestController
@RequestMapping(path = "/api")
public class MeiMarieServer {

    @Autowired
    private TransactionRepository repo;

    @Autowired
    private TransactionSearchService service;

    /**
     * Main entry point for Spring Boot.
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(MeiMarieServer.class)
                .run(args);
    }

    /**
     * Helper for setting up the Mock test. Do NOT use this anywhere else than in the test cases.
     */
    protected void setRepo(TransactionRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns all transactions for the front end.
     */
    @RequestMapping(path = "/transaction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Iterable<Transaction> list() throws ParseException {
        return repo.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "date")));
    }

    /**
     * Adds a new transaction through the front end
     */
    @RequestMapping(path = "/transaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody Transaction dto) {
        repo.save(dto);
    }

    /**
     * @return the statistical descriptive analysis for the data
     */
    @RequestMapping(path = "/stats/descriptive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public StatisticalResult descriptiveAnalysis() {
        return service.getAmountDescriptiveAnalysis();
    }

    /**
     * @return the sums for the given interval
     */
    @RequestMapping(path = "/stats/aggregation/sums_per_interval/{interval}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<SumPerInterval> sumPerInterval(@PathVariable IntervalType interval) {
        final DateHistogram.Interval dateInterval = new DateHistogram.Interval(interval.getType());
        DateHistogram histogram = service.getTransactionSumsPerInterval(dateInterval);
        return histogram.getBuckets().stream().map(b -> {
            InternalSum internalSum = b.getAggregations().get("sum");
            final org.elasticsearch.common.joda.time.DateTime date = b.getKeyAsDate();
            return new SumPerInterval(date.getMillis(), internalSum.getValue());
        }).collect(Collectors.toList());
    }

}
