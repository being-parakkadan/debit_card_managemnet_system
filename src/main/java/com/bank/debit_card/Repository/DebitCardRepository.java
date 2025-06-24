package com.bank.debit_card.Repository;

import com.bank.debit_card.Entity.DebitCardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DebitCardRepository extends MongoRepository<DebitCardEntity, String> {

}

