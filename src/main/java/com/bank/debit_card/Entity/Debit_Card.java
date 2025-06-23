package com.bank.debit_card.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "debit_cards")
public class Debit_Card {

    @Id
    private String id;

    private String accountId;
    private String cardNumber;
    private String cvv;
    private LocalDate expiryDate;
    private String pin;
    private String status;

    // Empty constructor (needed by Spring)
    public Debit_Card() {}

    // Constructor to create a new debit card quickly
    public Debit_Card(String accountId, String cardNumber, String cvv, LocalDate expiryDate) {
        this.accountId = accountId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.status = "Inactive"; // Default status when card is created
    }

    // Getters
    public String getId() { return id; }
    public String getAccountId() { return accountId; }
    public String getCardNumber() { return cardNumber; }
    public String getCvv() { return cvv; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getPin() { return pin; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public void setPin(String pin) { this.pin = pin; }
    public void setStatus(String status) { this.status = status; }
}
