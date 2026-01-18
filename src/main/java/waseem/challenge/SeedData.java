package waseem.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import waseem.challenge.orders.dto.OrderRequest;
import waseem.challenge.orders.service.OrderService;

import java.util.UUID;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        OrderRequest firstOrder = new OrderRequest("First Order", UUID.fromString("803dd66c-9b72-4769-9e81-6f6f0e04598b"));
        OrderRequest secondOrder = new OrderRequest("Second Order", UUID.fromString("53371cf6-ec09-42e8-af45-20ce86cea266"));
        orderService.createOrder(firstOrder);
        orderService.createOrder(secondOrder);

    }
}
