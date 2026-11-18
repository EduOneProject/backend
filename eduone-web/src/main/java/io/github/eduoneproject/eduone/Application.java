package io.github.eduoneproject.eduone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用启动类
 *
 * @author summerain0
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EntityScan({
        "org.axonframework.eventsourcing.eventstore.jpa",
        "org.axonframework.messaging.eventhandling.processing.streaming.token.store.jpa"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
