//package com.ironhack.ironbank.model.HelpClasses;
//
//import com.ironhack.ironbank.model.Accounts.Checking;
//
//import javax.persistence.*;
//
//import java.time.Instant;
//import java.util.UUID;
//
//
//@Entity
//public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    UUID transactionId;
//
//    String accountOwner;
//
//    @ManyToOne
//    @Column (name="origin_account_id")
//    Checking originAccount;
//
//    Money amount;
//
//    String destinationAccount;
//
//    String reason;
//
//    String issuedBy;
//
//    Instant issueDate;
//
//}
