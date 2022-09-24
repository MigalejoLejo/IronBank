package com.ironhack.ironbank.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.DTO.TransactionDTO;
import com.ironhack.ironbank.helpclasses.MovementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
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
    UUID id;

    @Enumerated(EnumType.STRING)
    MovementType movementType;

    String originID;
    String destinationID;
    String amount;
    String reason;

    LocalDate creationDate;

//    @ManyToOne
//    @JoinTable(
//            name = "checking_transaction_list",
//            joinColumns = @JoinColumn(name="checking_account_id"),
//            inverseJoinColumns = @JoinColumn(name= "transaction_list_id")
//    )
//    @JsonIgnore
//    Checking checking;

    public Transaction(MovementType movementType, String originID, String destinationID, String amount, String reason, LocalDate creationDate) {
        this.movementType = movementType;
        this.originID = originID;
        this.destinationID = destinationID;
        this.reason = reason;
        this.amount = amount;
        this.creationDate = creationDate;
    }

    public static Transaction createOutgoingTransactionFromDTO (TransactionDTO transactionDTO){
       return new Transaction(
                MovementType.OUTGOING,
                transactionDTO.getOriginID(),
                transactionDTO.getDestinationID(),
                "-"+transactionDTO.getAmount()+"€",
                transactionDTO.getReason()==null?"": transactionDTO.getReason(),
                LocalDate.now()
        );
    }

    public static Transaction createIncomingTransactionFromDTO (TransactionDTO transactionDTO){
        return new Transaction(
                MovementType.INCOMING,
                transactionDTO.getOriginID(),
                transactionDTO.getDestinationID(),
                transactionDTO.getAmount()+"€",
                transactionDTO.getReason(),
                LocalDate.now()
        );
    }
}
