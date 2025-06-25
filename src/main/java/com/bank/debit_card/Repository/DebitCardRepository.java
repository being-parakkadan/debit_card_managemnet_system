package com.bank.debit_card.Repository;

import com.bank.debit_card.Entity.DebitCardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DebitCardRepository extends MongoRepository<DebitCardEntity, String> {
    Optional<DebitCardEntity> findByCardNumber(String cardNumber);

    List<DebitCardEntity> findByCustomerId(String customerId);

}

