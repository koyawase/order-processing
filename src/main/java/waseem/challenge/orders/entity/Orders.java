package waseem.challenge.orders.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Orders {

    @Id
    private UUID id;

    private String name;
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderStatus> history = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderStatus> getHistory() {
        return history;
    }

    public void setHistory(List<OrderStatus> history) {
        this.history = history;
    }

    public void addHistory(OrderStatus entry) {
        entry.setOrder(this);
        history.add(entry);
    }
}
