package com.bankingwebapp.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
@Component
public class DataInitializer {

    @Value("${data.initializer.enabled}")
    private boolean dataInitializerEnabled;

    @Autowired
    private UserDataInitializer userDataInitializer;

    @Autowired
    private BankAccountDataInitializer bankAccountDataInitializer;

    @Autowired
    private TransactionDataInitializer transactionDataInitializer;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
    	
    	if (dataInitializerEnabled) {
            // Only initialize data if the flag is true
            System.out.println("Initializing data...");
            userDataInitializer.addDataIfBelowLimit();
            bankAccountDataInitializer.addDataIfBelowLimit();
            transactionDataInitializer.addDataIfBelowLimit();
        } else {
            System.out.println("Data initialization is disabled.");
        }
    }
}
