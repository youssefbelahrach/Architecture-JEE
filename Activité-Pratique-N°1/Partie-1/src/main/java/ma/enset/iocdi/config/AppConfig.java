package ma.enset.iocdi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de Spring par annotations.
 */
@Configuration
@ComponentScan(basePackages = "ma.enset.iocdi")
public class AppConfig {
}
