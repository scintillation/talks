package at.scintillation.talks.meimarie.repository;

import at.scintillation.talks.meimarie.dto.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 27.12.2015
 */
public interface TransactionRepository extends ElasticsearchRepository<Transaction, String> {

}
