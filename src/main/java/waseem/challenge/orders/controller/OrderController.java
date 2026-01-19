package waseem.challenge.orders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import waseem.challenge.orders.dto.OrderDTO;
import waseem.challenge.orders.dto.OrderRequest;
import waseem.challenge.orders.dto.Status;
import waseem.challenge.orders.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        List<OrderDTO> orders = orderService.getAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable UUID id) {
        OrderDTO order = orderService.getById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam Status status
    ) {
        OrderDTO updated = orderService.updateStatus(id, status);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody OrderRequest request) {
        OrderDTO created = orderService.createOrder(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
