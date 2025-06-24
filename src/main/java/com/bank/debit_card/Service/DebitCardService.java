package com.bank.debit_card.Service;

import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;


    //=====card usage========

    public DebitCardEntity getCardUsage(String cardId){
        return debitCardRepository.findById(cardId)
                .orElseThrow(()->new RuntimeException("Card not found with ID"));
    }

    public DebitCardEntity updateCardUsage(String cardId, DebitCardEntity updated) {
        DebitCardEntity card = debitCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with ID: " + cardId));

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

    // ==================== Card Blocking Section ====================

    public String blockCardByCardNumber(String cardNumber, String reason) {
        DebitCardEntity card = debitCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardNumber));

        Instant now = Instant.now();

        // Auto-block if expired
        if (card.getExpiryDate() != null && card.getExpiryDate().isBefore(now)) {
            if (!card.isBlocked()) {
                card.setBlocked(true);
                card.setBlockReason("Expired");
                card.setBlockDate(now);
                card.setStatus("Blocked");
                debitCardRepository.save(card);
                return "Card is expired and has been automatically blocked.";
            } else {
                return "Card is already blocked and expired.";
            }
        }

        // Manual block with reason
        if (card.isBlocked()) {
            return "Card is already blocked.";
        }

        card.setBlocked(true);
        card.setBlockReason(reason);
        card.setBlockDate(now);
        card.setStatus("Blocked");

        debitCardRepository.save(card);

        return "Card blocked successfully for reason: " + reason;
    }

        // ==================== Card Activation Section ====================
        public String activateCard(String cardNumber, String pin) {
            DebitCardEntity card = debitCardRepository.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new RuntimeException("Card not found."));

            if (!pin.matches("\\d{4}")) {
                return "PIN must be exactly 4 digits.";
            }

            card.setPin(pin);
            card.setStatus("Active");
            card.setActivationDate(Instant.now());

            debitCardRepository.save(card);

            return "Card activated successfully.";
        }
}




