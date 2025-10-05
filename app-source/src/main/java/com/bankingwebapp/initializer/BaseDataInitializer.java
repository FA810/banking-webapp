package com.bankingwebapp.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseDataInitializer<T> {

    @Autowired
    private JpaRepository<T, Long> repository; // Injected repository for each entity

    // Define a limit for rows in the database
    private static final int MAX_ROWS = 20;

    // Check if the current number of rows exceeds the limit
    public boolean isBelowLimit() {
        long count = repository.count();
        return count < MAX_ROWS;
    }

    // Abstract method to initialize data, to be implemented by subclasses
    protected abstract void initializeData();

    // Method to add data, ensuring the limit is respected
    public void addDataIfBelowLimit() {
        if (isBelowLimit()) {
            initializeData();  // Initialize data only if limit is not exceeded
        } else {
            System.out.println("Row limit reached. Data not inserted.");
        }
    }
}
