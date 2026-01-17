package waseem.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import waseem.challenge.orders.service.OrderService;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        orderService.createOrder("First Order");
        orderService.createOrder("Second Order");

    }
}
