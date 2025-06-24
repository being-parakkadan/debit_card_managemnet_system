package com.bank.debit_card.Service;

import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;


    //card usage

    public DebitCardEntity getCardUsage(String cardId){
        return debitCardRepository.findById(cardId)
                .orElseThrow(()->new RuntimeException("Card not found with ID"));
    }

    public DebitCardEntity updateCardUsage(String cardId, DebitCardEntity updated) {
        DebitCardEntity card = debitCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with ID: " + cardId));

        // Update only usage-related fields
        card.setDomesticUsage(updated.isDomesticUsage());
        card.setInternationalUsage(updated.isInternationalUsage());
        card.setAtmEnabled(updated.isAtmEnabled());
        card.setPosEnabled(updated.isPosEnabled());
        card.setEcomEnabled(updated.isEcomEnabled());
        card.setNfcEnabled(updated.isNfcEnabled());

        card.setAtmTransactionLimit(updated.getAtmTransactionLimit());
        card.setPosEcomTransactionLimit(updated.getPosEcomTransactionLimit());

        return debitCardRepository.save(card);
    }
}


