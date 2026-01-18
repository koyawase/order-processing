package waseem.challenge.orders.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderRequest(
    @NotNull(message = "Name must be provided")
    String name,
    @NotNull(message = "ID must be provided")
    UUID id
) {}
