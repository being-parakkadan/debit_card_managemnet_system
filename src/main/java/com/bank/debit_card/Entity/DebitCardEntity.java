package com.bank.debit_card.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "debit_cards")
@Data // Generates Getters, Setters, toString, equals, hashCode
@NoArgsConstructor // No-args constructor
@AllArgsConstructor // All-args constructor
public class DebitCardEntity {

    @Id
    private String id;

    private String accountId;
    private String cardNumber;
    private String cvv;
    private Instant expiryDate;
    private String pin;
    private String status;

    // ===== Card Usage Section (Jerald) =====
    private boolean domesticUsage;
    private boolean internationalUsage;
    private boolean atmEnabled;
    private boolean posEnabled;
    private boolean ecomEnabled;
    private boolean nfcEnabled;

    private int atmTransactionLimit;
    private int posEcomTransactionLimit;
    private int annaliya ;


    // ====Card Activation Section====
    private Instant activationDate;     // Timestamp when the card was activated

    // =====Card Block Section =====
    private boolean isBlocked;         // true if blocked (manually or due to expiry)
    private String blockReason;        // e.g. "Lost", "Stolen", "Expired", etc.
    private Instant blockDate;         // Timestamp when the card was blocked


    // You can still add custom constructors or methods manually if needed
    public DebitCardEntity(String accountId, String cardNumber, String cvv, Instant expiryDate) {
        this.accountId = accountId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.status = "Inactive";
    }
}
