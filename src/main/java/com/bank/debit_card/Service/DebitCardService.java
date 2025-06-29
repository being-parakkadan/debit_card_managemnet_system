package com.bank.debit_card.Service;

import com.bank.debit_card.DTO.DebitCardDto;
import com.bank.debit_card.Entity.DebitCardEntity;
import com.bank.debit_card.Repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;


    //=====card usage========

    public DebitCardEntity getCardUsage(String cardNumber) {
        return debitCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found with Number: " + cardNumber));
    }

    public DebitCardEntity updateCardUsage(String cardNumber, DebitCardEntity updated) {
        DebitCardEntity card = debitCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found with Number: " + cardNumber));

        if (!"Active".equalsIgnoreCase(card.getStatus())) {
            throw new RuntimeException("Card is not active. Usage settings cannot be updated.");
        }


        // Update only the usage-related fields
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


        // Check if card is already blocked
        if (card.isBlocked()) {
            return "Card is already blocked.";
        }

        // Auto-block if expired
        if (card.getExpiryDate() != null && card.getExpiryDate().isBefore(Instant.now())){
                card.setBlocked(true);
                card.setBlockReason("Expired");
                card.setBlockDate(now);
                card.setStatus("Blocked");
                debitCardRepository.save(card);
                return "Card is expired and has been automatically blocked.";
            }

        //Manual Block With reason
        if (!"Active".equalsIgnoreCase(card.getStatus())) {
            return "Cannot block card manually. Card is not active.";
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

        //========================Card Generation Section ======================

    public void generateDebitCard(String customerId, String accountId, String accountType){
        // Check if card already exists
        Optional<DebitCardEntity> existingCard =
                debitCardRepository.findByCustomerIdAndAccountType(customerId, accountType);

        if (existingCard.isPresent()){
            throw new IllegalStateException("A Debit Card Already Exists for this Customer and Account type");
        }


        String cardNumber = generateUniqueCardNumber();
        String cvv = generateRandomCVV();
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneOffset.UTC);
        ZonedDateTime futureDate = zonedDateTime.plusYears(5);
        Instant expiryDate = futureDate.toInstant();



        DebitCardEntity newCard = new DebitCardEntity(customerId,accountId, accountType, cardNumber, cvv, expiryDate, null);
        newCard.setPin(null);
        newCard.setStatus("Inactive");

        debitCardRepository.save(newCard);
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
        int cvv = 100 + random.nextInt(900);
        return String.valueOf(cvv);
    }

    //===================Pin Reset Section=================================
    public String resetPin(String cardNumber, String newPin) {
        Optional<DebitCardEntity> optionalCard = debitCardRepository.findByCardNumber(cardNumber);

        if (optionalCard.isEmpty()) {
            return "Card not found.";
        }

        DebitCardEntity card = optionalCard.get();

        if (!"Active".equalsIgnoreCase(card.getStatus())) {
            return "Cannot reset PIN. Card is not active.";
        }

        if (!newPin.matches("\\d{4}")) {
            return "New PIN must be exactly 4 digits.";
        }

        card.setPin(newPin); // update pin
        debitCardRepository.save(card);

        return "PIN reset successfully.";
    }

    //====================Card Details Section================================

    public List<DebitCardDto> getCardsByCustomerId(String customerId) {
        List<DebitCardEntity> cards = debitCardRepository.findByCustomerId(customerId);

        if(cards.isEmpty()){
            DebitCardDto messageDto = new DebitCardDto("N/A","N/A",null, "N/A");
            return List.of(messageDto);
        }
        return cards.stream()
                .map(card -> new DebitCardDto(
                        card.getCardNumber(),
                        card.getCvv(),
                        card.getExpiryDate(),
                        card.getAccountType()
                ))
                .collect(Collectors.toList());
    }

}




