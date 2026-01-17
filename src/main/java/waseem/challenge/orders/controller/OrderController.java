package waseem.challenge.orders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import waseem.challenge.orders.dto.OrderDTO;
import waseem.challenge.orders.dto.Status;
import waseem.challenge.orders.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public List<OrderDTO> getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PutMapping("/{id}")
    public OrderDTO updateStatus(
            @PathVariable Long id,
            @RequestParam Status status
    ) {
        return orderService.updateStatus(id, status);
    }

    @PostMapping
    public OrderDTO create(@RequestParam String name) {
        return orderService.createOrder(name);
    }
}
