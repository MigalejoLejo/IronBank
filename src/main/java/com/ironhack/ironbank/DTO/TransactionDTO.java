package com.ironhack.ironbank.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    String originID;
    String destinationID;
    String secretKey;
    String reason;
    String amount;

    public TransactionDTO(String originID, String destinationID, String reason, String amount) {
        this.originID = originID;
        this.destinationID = destinationID;
        this.reason = reason;
        this.amount = amount;
        this.secretKey = null;
    }


}
