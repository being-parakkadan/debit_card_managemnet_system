package com.bank.debit_card.Controller;

import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.Map;

@RestController
@RequestMapping("/debit_card")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @GetMapping("/{cardId}")
    public DebitCardEntity getCardUsage(@PathVariable String cardId){
        return debitCardService.getCardUsage(cardId);
    }

    @PutMapping("/{cardId}")
    public DebitCardEntity updateCardUsage(@PathVariable String cardId,@RequestBody DebitCardEntity updatedUsage){
        return debitCardService.updateCardUsage(cardId,updatedUsage);
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


}
