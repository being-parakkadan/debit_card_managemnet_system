package com.bank.debit_card.DTO;


import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardDto {
    private String cardNumber;
    private String cvv;
    private LocalDate expiryDate;
}
