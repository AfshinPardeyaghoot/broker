package ir.broker.maktab.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

@SpringBootApplication
public class BrokerApplication {

    public static void main(String[] args) throws ParseException {
        SpringApplication.run(BrokerApplication.class, args);
    }

}
