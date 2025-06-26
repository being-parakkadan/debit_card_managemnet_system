package com.bank.debit_card.DTO;


import lombok.*;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardDto {
    private String cardNumber;
    private String cvv;
    private Instant expiryDate;
    private String accountType;
}
