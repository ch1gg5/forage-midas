package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(Transaction transaction) {
        // record transaction
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), transaction.getIncentive());
        transactionRecordRepository.save(transactionRecord);

        // update user balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        save(sender);
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + transaction.getIncentive());
        save(recipient);
    }

    //check if
//    The senderId is valid
//    The recipientId is valid
//    The sender has a balance greater than or equal to the transaction amount

    public boolean isValid(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        if (sender == null) {
            return false;
        }

        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);
        if (recipient == null) {
            return false;
        }

        return sender.getBalance() >= transaction.getAmount();
    }

    public float queryUserBalance(Long userId) {
        UserRecord userRecord = userRepository.findById(userId).orElse(null);
        if (userRecord == null) {
            return 0;
        } else {
            return userRecord.getBalance();
        }
    }

    public String toString(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);
        return "Transaction {sender=" + sender + ", recipient=" + recipient + "}";
    }

}
