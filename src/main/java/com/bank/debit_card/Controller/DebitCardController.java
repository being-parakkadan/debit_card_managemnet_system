package com.bank.debit_card.Controller;

import com.bank.debit_card.DTO.DebitCardDto;
import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;


@RestController
@RequestMapping("/debit_card")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @GetMapping("/{cardNumber}")
    public DebitCardEntity getCardUsage(@PathVariable String cardNumber){
        return debitCardService.getCardUsage(cardNumber);
    }

    @PutMapping("/{cardNumber}")
    public DebitCardEntity updateCardUsage(@PathVariable String cardNumber,@RequestBody DebitCardEntity updatedUsage){
        return debitCardService.updateCardUsage(cardNumber,updatedUsage);
    }

    @PutMapping("/block/{cardNumber}")
    public String blockCard(@PathVariable String cardNumber, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        return debitCardService.blockCardByCardNumber(cardNumber, reason);
    }

    @PutMapping("/activate/{cardNumber}")
    public String activateCard(@PathVariable String cardNumber, @RequestBody Map<String, String> body) {
        String pin = body.get("pin");
        return debitCardService.activateCard(cardNumber, pin);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateCard(@RequestParam String customerId,
                                               @RequestParam String accountId,
                                               @RequestParam String accountType) {
        debitCardService.generateDebitCard(customerId, accountId, accountType);
        return ResponseEntity.ok("Debit card generated and saved successfully.");
    }

    @PutMapping("/reset-pin/{cardNumber}")
    public String resetPin(@PathVariable String cardNumber, @RequestBody Map<String, String> body) {
        String newPin = body.get("newPin");
        return debitCardService.resetPin(cardNumber, newPin);
    }

    @GetMapping("/cards/{customerId}")
    public List<DebitCardDto> getCardsByCustomerId(@RequestParam String customerId) {
        return debitCardService.getCardsByCustomerId(customerId);
    }
}
