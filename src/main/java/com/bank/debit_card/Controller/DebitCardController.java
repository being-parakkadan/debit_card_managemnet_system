package com.bank.debit_card.Controller;

import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/block")
    public String blockCard(@RequestParam String cardNumber,@RequestParam String reason){
        return debitCardService.blockCardByCardNumber(cardNumber, reason);
    }
}
