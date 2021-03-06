package at.scintillation.talks.meimarie;

import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 10.04.2016
 */
@Configuration
@EnableAutoConfiguration
@EnableElasticsearchRepositories(basePackages = "at.scintillation.manawa.web.repository")
@ComponentScan(basePackages = "at.scintillation.talks.meimarie.service")
public class TestConfig {

    @Bean
    public Client nodeClient() {
        return nodeBuilder().local(true).node().client();
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeClient());
    }

}