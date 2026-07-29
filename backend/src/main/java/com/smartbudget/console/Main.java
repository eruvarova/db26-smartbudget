package com.smartbudget.console;

import com.smartbudget.model.InvalidTransactionException;
import com.smartbudget.model.BaseTransaction;
import com.smartbudget.model.ExpenseTransaction;
import com.smartbudget.model.IncomeTransaction;
import com.smartbudget.model.Transaction;
import com.smartbudget.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final List<Transaction> TXNS = new ArrayList<>();

    

    public static void main(String[] args) {
        TransactionService svc = new TransactionService();


        


        // =========================================
        // Test IncomeTransaction
        // =========================================

        System.out.println("=== IncomeTransaction Test ===");

        IncomeTransaction salary = new IncomeTransaction(
                1,
                new BigDecimal("3500.00"),
                LocalDate.now(),
                "January salary"
        );

        System.out.println(salary.getType()); // Expected: INCOME
        System.out.println(salary);

        // Test validation inherited from parent class
        try {
            new IncomeTransaction(
                    2,
                    new BigDecimal("-100"),
                    LocalDate.now(),
                    "Bad"
            );

            System.out.println(
                    "Test failed: negative amount was accepted."
            );

        } catch (InvalidTransactionException e) {
            System.out.println(
                    "Rejected: " + e.getMessage()
            );
        }

        System.out.println("==============================");

    // =========================================
    // Test 2: Polymorphism
    // =========================================

        List<BaseTransaction> all = new ArrayList<>();

        all.add(new IncomeTransaction(
                1,
                new BigDecimal("3500"),
                LocalDate.now(),
                "Salary"
        ));

        all.add(new ExpenseTransaction(
                2,
                new BigDecimal("45"),
                LocalDate.now(),
                "Groceries",
                "Food"
        ));

        for (BaseTransaction t : all) {
            System.out.println(t.getType() + " -> " + t);
        }
    // =========================================
    // Test 3: TransactionService
    // =========================================       

        svc.addTransaction(new IncomeTransaction(1, new BigDecimal("100"),
                LocalDate.now(), "Test"));
        svc.addTransaction(new ExpenseTransaction(2, new BigDecimal("20"),
                LocalDate.now(), "Test"));

        List<BaseTransaction> view = svc.getAll();
        view.clear();                         // does NOT touch internal list
        System.out.println(svc.size());       // still 2 — defensive copy worked

    // =========================================
    // Test 4: smoke test
    // =========================================  
    
        svc.addTransaction(new IncomeTransaction(1, new BigDecimal("100"),
                LocalDate.of(2026, 1,  5), "Jan"));
        svc.addTransaction(new IncomeTransaction(2, new BigDecimal("100"),
                LocalDate.of(2026, 2, 15), "Feb"));
        svc.addTransaction(new IncomeTransaction(3, new BigDecimal("100"),
                LocalDate.of(2026, 3, 25), "Mar"));

        List<BaseTransaction> jan = svc.filterByDateRange(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));
        System.out.println(jan.size());     // 1 — only the Jan row

        List<BaseTransaction> q1 = svc.filterByDateRange(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 31));
        System.out.println(q1.size());      // 3 — all three rows

        List<BaseTransaction> reversed = svc.filterByDateRange(
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 1, 1));
        System.out.println(reversed.size()); // 0 — empty range, not an error
        

        seed();

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== SmartBudget Console ===");
            System.out.println("1) List Transactions");
            System.out.println("2) Add Transaction");
            System.out.println("3) Summary");
            System.out.println("4) Exit");
            System.out.print("Choice: ");

            int choice;

            try {
                choice = sc.nextInt();
                sc.nextLine(); // discard trailing newline
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number from 1 to 4.");
                sc.nextLine(); // discard invalid input
                continue;
            }

            switch (choice) {
                case 1 -> listTransactions();
                case 2 -> addTransaction(sc);
                case 3 -> showSummary();
                case 4 -> running = false;
                default -> System.out.println("Unknown option: " + choice);
            }
        }

        sc.close();
        System.out.println("Goodbye!");
    }

    private static void listTransactions() {
        if (TXNS.isEmpty()) {
            System.out.println("(no transactions)");
            return;
        }

        String headerFormat = "%-5s %-25s %-8s %10s %-12s%n";

        System.out.printf(
                headerFormat,
                "ID",
                "Description",
                "Type",
                "Amount",
                "Date"
        );

        System.out.println("-".repeat(65));

        String rowFormat = "%-5d %-25s %-8s %10.2f %-12s%n";

        for (Transaction transaction : TXNS) {
            System.out.printf(
                    rowFormat,
                    transaction.getTxnId(),
                    transaction.getDescription(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getTxnDate()
            );
        }

        System.out.println("-".repeat(65));
        System.out.println("Total rows: " + TXNS.size());
    }

    private static void addTransaction(Scanner sc) {
        System.out.print("Amount: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Transaction not added.");
            return;
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be positive. Transaction not added.");
            return;
        }

        System.out.print("Date (yyyy-mm-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(sc.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Transaction not added.");
            return;
        }
        if (date.isAfter(LocalDate.now())) {
            System.out.println("Date cannot be in the future. Transaction not added.");
            return;
        }

        System.out.print("Description: ");
        String desc = sc.nextLine().trim();
        if (desc.isEmpty()) {
            System.out.println("Description required. Transaction not added.");
            return;
        }

        System.out.print("Type (INCOME/EXPENSE): ");
        String type = sc.nextLine().trim().toUpperCase();
        if (!"INCOME".equals(type) && !"EXPENSE".equals(type)) {
            System.out.println("Type must be INCOME or EXPENSE. Transaction not added.");
            return;
        }

        int nextId = TXNS.size() + 1;
        TXNS.add(new Transaction(nextId, 1, 1, amount, date, desc, type));
        System.out.println("Added transaction #" + nextId);
    }

    private static void showSummary() {
        BigDecimal income  = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Transaction t : TXNS) {
            if ("INCOME".equals(t.getType())) {
                income = income.add(t.getAmount());
            } else if ("EXPENSE".equals(t.getType())) {
                expense = expense.add(t.getAmount());
            }
        }
        BigDecimal net = income.subtract(expense);

        System.out.println();
        System.out.println("=== Summary ===");
        System.out.printf("Total Income:    %12.2f%n", income);
        System.out.printf("Total Expenses:  %12.2f%n", expense);
        System.out.println("-".repeat(28));
        System.out.printf("Net Balance:     %12.2f%n", net);
    }

    private static void seed() {
        TXNS.add(t(
                1,
                1,
                1,
                "3500.00",
                "2026-01-01",
                "January salary",
                "INCOME"
        ));

        TXNS.add(t(
                2,
                1,
                3,
                "45.20",
                "2026-01-08",
                "Groceries",
                "EXPENSE"
        ));

        TXNS.add(t(
                3,
                1,
                4,
                "25.00",
                "2026-01-15",
                "Bus pass",
                "EXPENSE"
        ));

        TXNS.add(t(
                4,
                2,
                1,
                "4200.00",
                "2026-01-01",
                "January salary",
                "INCOME"
        ));

        TXNS.add(t(
                5,
                2,
                5,
                "120.00",
                "2026-01-20",
                "Electricity bill",
                "EXPENSE"
        ));

        TXNS.add(t(
                6,
                3,
                2,
                "800.00",
                "2026-02-05",
                "Freelance gig",
                "INCOME"
        ));

        TXNS.add(t(
                7,
                3,
                3,
                "60.00",
                "2026-02-10",
                "Restaurant",
                "EXPENSE"
        ));

        TXNS.add(t(
                8,
                1,
                1,
                "3500.00",
                "2026-02-01",
                "February salary",
                "INCOME"
        ));

        TXNS.add(t(
                9,
                4,
                1,
                "2800.00",
                "2026-02-01",
                "February salary",
                "INCOME"
        ));

        TXNS.add(t(
                10,
                5,
                3,
                "52.00",
                "2026-03-05",
                "Groceries",
                "EXPENSE"
        ));

        System.out.println(
                "Seeded " + TXNS.size() + " transactions."
        );
    }

    private static Transaction t(
            int transactionId,
            int userId,
            int categoryId,
            String amount,
            String date,
            String description,
            String type
    ) {
        return new Transaction(
                transactionId,
                userId,
                categoryId,
                new BigDecimal(amount),
                LocalDate.parse(date),
                description,
                type
        );
    }

    
}