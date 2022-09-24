package com.ironhack.ironbank.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.Money;
import com.ironhack.ironbank.helpclasses.MovementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    UUID id;

    @Enumerated(EnumType.STRING)
    MovementType movementType;

    String originID;
    String destinationID;
    Money amount;
    String reason;

    LocalDate creationDate;


    public static Transaction createOutgoingTransactionFromDTO (TransactionDTO transactionDTO){
        var result = new Transaction();

        result.setId(UUID.randomUUID());
        result.setMovementType(MovementType.OUTGOING);
        result.setOriginID(transactionDTO.getOriginID());
        result.setDestinationID(transactionDTO.getDestinationID());
        result.setAmount(new Money (new BigDecimal(transactionDTO.getAmount()).multiply(new BigDecimal("-1"))));
        result.setReason(transactionDTO.getReason());
        result.setCreationDate(LocalDate.now());
       return result;
    }

    public static Transaction createIncomingTransactionFromDTO (TransactionDTO transactionDTO){
        var result = new Transaction();

        result.setId(UUID.randomUUID());
        result.setMovementType(MovementType.INCOMING);
        result.setOriginID(transactionDTO.getOriginID());
        result.setDestinationID(transactionDTO.getDestinationID());
        result.setAmount(new Money (new BigDecimal(transactionDTO.getAmount())));
        result.setReason(transactionDTO.getReason());
        result.setCreationDate(LocalDate.now());
        return result;
    }
}
