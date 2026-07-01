package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRecord {

    @Id
    @GeneratedValue()
    private long id;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;


    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionRecord {sender=" + sender.toString() + ", recipient=" + recipient.toString() + ", amount=" + amount +
                 "}";
    }
}