package com.ironhack.ironbank.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movement {

    @Id
    UUID id;

    String originID;
    String destinationID;
    String reason;
    String amount;
    Instant creationDate;

    public Movement(String originID, String destinationID, String reason, String amount, Instant creationDate) {
        this.originID = originID;
        this.destinationID = destinationID;
        this.reason = reason;
        this.amount = amount;
        this.creationDate = creationDate;
    }


}
