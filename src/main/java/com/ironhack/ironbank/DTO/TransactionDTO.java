package com.ironhack.ironbank.DTO;

import java.time.Instant;
import java.util.UUID;

public class TransactionDTO {

    UUID id;
    String originID;
    String destinationID;
    String secretKey;
    String reason;
    String amount;
    Instant creationDate;

}
