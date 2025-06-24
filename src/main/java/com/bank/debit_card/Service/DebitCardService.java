package com.bank.debit_card.Service;

import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;

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

    // ==================== Card Blocking Section ====================

    public String blockCardByCardNumber(String cardNumber, String reason) {
        DebitCardEntity card = debitCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardNumber));

        LocalDate now = LocalDate.now();


        // Check if card is already blocked
        if (card.isBlocked()) {
            return "Card is already blocked.";
        }

        // Auto-block if expired
        if (card.getExpiryDate() != null && card.getExpiryDate().isBefore(now)) {
                card.setBlocked(true);
                card.setBlockReason("Expired");
                card.setBlockDate(now);
                card.setStatus("Blocked");
                debitCardRepository.save(card);
                return "Card is expired and has been automatically blocked.";
            }

        //Manual Block With reason
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
        card.setActivationDate(LocalDate.now());

        debitCardRepository.save(card);

        return "Card activated successfully.";
        }

        //========================Card Generation Section ======================

    public DebitCardEntity generateDebitCard(String accountId, String accountNumber, String accountHolderName){
        String cardNumber = generateUniqueCardNumber();
        String cvv = generateRandomCVV();
        LocalDate expiryDate = LocalDate.now().plusYears(8);

        DebitCardEntity newCard = new DebitCardEntity(accountId, accountNumber, cardNumber, accountHolderName, cvv, expiryDate, null);
        newCard.setPin(null);
        newCard.setStatus("Inactive");

        return debitCardRepository.save(newCard);
    }

    // To Check Card number is Unique

    private String generateUniqueCardNumber(){
        String cardNumber;
        do{
            cardNumber = generateRandomCardNumber();
        }while (debitCardRepository.findByCardNumber(cardNumber).isPresent());
        return cardNumber;
    }

    private String generateRandomCardNumber(){
        Random random =new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i =0; i < 16; i++){
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    private String generateRandomCVV() {
        Random random = new Random();
        int cvv = 100 + random.nextInt(900); // Ensures CVV is always 3 digits
        return String.valueOf(cvv);
    }


}




