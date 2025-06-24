package com.bank.debit_card.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;

@Document(collection = "debit_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardEntity {

    @Id
    private String id;

    private String accountId;
    private String cardNumber;
    private String accountHolderName;
    private String accountNumber;
    private String cvv;
    private LocalDate expiryDate;
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


    // ====Card Activation Section====
    private LocalDate activationDate;

    // =====Card Block Section =====
    private boolean isBlocked;
    private String blockReason;
    private LocalDate blockDate;


    // You can still add custom constructors or methods manually if needed
    public DebitCardEntity(String accountId, String accountNumber,String cardNumber, String accountHolderName, String cvv, LocalDate expiryDate, String pin) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.accountHolderName = accountHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.status = "Inactive";
        this.pin= null;
    }
}
