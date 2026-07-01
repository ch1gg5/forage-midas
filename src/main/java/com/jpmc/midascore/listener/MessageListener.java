package com.jpmc.midascore.listener;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.component.IncentiveQuerier;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageListener {

    private final DatabaseConduit databaseConduit;
    private final IncentiveQuerier incentiveQuerier;

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        handleTransaction(transaction);
    }

    public void handleTransaction(Transaction transaction) {
        if (databaseConduit.isValid(transaction)) {
            Incentive incentive = incentiveQuerier.query(transaction);
            transaction.setIncentive(incentive.getAmount());
            databaseConduit.save(transaction);
            //System.out.println(databaseConduit.toString(transaction)); //to check for name
        }
    }

}
