package at.scintillation.talks.meimarie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
        logger.info("Launching pa-web.");

        new SpringApplicationBuilder()
                .sources(MeiMarieServer.class)
                .run(args);
    }

    @RequestMapping(path = "/greeting", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> greeting() {
        Map<String, String> greetings = new HashMap<>();
        greetings.put("de", "Hallo");
        greetings.put("en", "Hello");
        greetings.put("es", "Hola");
        return greetings;
    }

}
