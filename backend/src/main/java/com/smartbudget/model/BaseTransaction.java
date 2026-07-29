package com.smartbudget.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// ============================================================
// TICKET-F021 (Day 3, Sprint 2) — Abstract Base Class for Transactions
// ============================================================
//
// WHAT: An abstract class is a class you CANNOT create objects from directly.
//       It serves as a blueprint for child classes (IncomeTransaction, ExpenseTransaction).
//       The "abstract" keyword on a method means: "every child MUST provide its own version."
//       This is a core OOP concept called POLYMORPHISM.
//
// WHY:  Income and Expense transactions share common fields (amount, date, description)
//       but differ in behavior (getType returns different values).
//       Instead of duplicating fields in both classes, we put shared logic HERE
//       and let each child add its own unique behavior.
//       This follows the DRY principle — Don't Repeat Yourself.
//
// ============================================================
public abstract class BaseTransaction {

    // -------------------------------------------------------
    // TODO TICKET-F021: Step 1 — Declare fields
    // -------------------------------------------------------
    // WHAT: Fields store data about each transaction.
    //       Use "protected" access so child classes can access them directly.
    //
    // HOW:  Declare 4 protected fields:
    //         - An integer for the transaction ID
    //         - A BigDecimal for the amount (never use double for money — precision issues)
    //         - A LocalDate for the transaction date
    //         - A String for the description
    //
    // WHY:  "protected" means: accessible in this class AND its children.
    //       "private" would hide them from IncomeTransaction/ExpenseTransaction.
    //       BigDecimal is used for money because double can produce errors like 0.1 + 0.2 = 0.30000000000000004.
    //
    // OBSERVE: After adding fields, your IDE should show them as unused (yellow underline).
    //          That's expected — they'll be used once you add the constructor.
    protected int txnId;
    protected BigDecimal amount;
    protected LocalDate txnDate;
    protected String description;

    public BaseTransaction(int txnId, BigDecimal amount,
                           LocalDate txnDate, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException(
                "Amount must be greater than zero, got: " + amount);
        }
        if (txnDate == null || txnDate.isAfter(LocalDate.now())) {
            throw new InvalidTransactionException(
                "Transaction date cannot be in the future: " + txnDate);
        }
        this.txnId       = txnId;
        this.amount      = amount;
        this.txnDate     = txnDate;
        this.description = description;
    }

    public abstract String getType();

    public int getTxnId()              { return txnId; }
    public BigDecimal getAmount()      { return amount; }
    public LocalDate getTxnDate()      { return txnDate; }
    public String getDescription()     { return description; }

    @Override
    public String toString() {
        return String.format("[%s] id=%d | %s | %s | %s",
                getType(), txnId, amount, txnDate, description);
    }



    // -------------------------------------------------------
    // TODO TICKET-F021: Step 2 — Constructor with validation
    // -------------------------------------------------------
    // WHAT: A constructor is a special method that runs when you create a new object.
    //       Validation in the constructor means: if the data is invalid, the object
    //       is NEVER created — it fails immediately with an exception.
    //
    // HOW:  Create a public constructor that accepts all 4 fields as parameters.
    //       Before assigning values, check two business rules:
    //         Rule 1: Amount must be greater than zero.
    //                 Compare using BigDecimal's compareTo method (not > operator).
    //                 If violated, throw your InvalidTransactionException with a clear message.
    //         Rule 2: Date must not be in the future.
    //                 Use LocalDate.now() and the isAfter() method to check.
    //                 If violated, throw InvalidTransactionException.
    //       After validation passes, assign each parameter to its field using "this."
    //
    // WHY:  Validating in the constructor guarantees that every BaseTransaction object
    //       in your program has valid data. This is called "maintaining invariants."
    //       If you validate later (in a separate method), someone might forget to call it.
    //
    // OBSERVE: After implementing, try creating a transaction with amount = -50.
    //          Your program should crash with InvalidTransactionException.
    //          Try with a future date — same result. Try with valid data — it should work.

    // -------------------------------------------------------
    // TODO TICKET-F021: Step 3 — Abstract method getType()
    // -------------------------------------------------------
    // WHAT: An abstract method has NO body — just a signature.
    //       Every child class MUST override it and provide the actual implementation.
    //       This is how polymorphism works: same method name, different behavior per class.
    //
    // HOW:  Declare: public abstract String getType();
    //       Note: no curly braces, just a semicolon. The method has no body here.
    //       IncomeTransaction will return "INCOME", ExpenseTransaction will return "EXPENSE".
    //
    // WHY:  This lets you write code like: transaction.getType() and get the correct answer
    //       regardless of whether it's an Income or Expense transaction.
    //       The calling code doesn't need to know which subclass it is.
    //
    // OBSERVE: After adding this, IncomeTransaction and ExpenseTransaction will show
    //          compile errors because they don't override it yet. That's expected.

    // -------------------------------------------------------
    // TODO TICKET-F021: Step 4 — Getters for all fields
    // -------------------------------------------------------
    // WHAT: Getters are public methods that return field values.
    //       Since fields are protected, external code uses getters to read them.
    //
    // HOW:  Add a public getter for each of the 4 fields.
    //       Convention: getFieldName() — e.g., getAmount() returns the amount field.
    //
    // WHY:  This follows the Encapsulation principle: fields are hidden,
    //       access is controlled through methods. If you later need to add
    //       logic (e.g., rounding), you change the getter without affecting callers.

    // -------------------------------------------------------
    // TODO TICKET-F021: Step 5 — Override toString()
    // -------------------------------------------------------
    // WHAT: toString() is called automatically when you print an object.
    //       Without it, printing shows something useless like "BaseTransaction@4a574b5".
    //       With it, you see meaningful data like "[INCOME] id=1 | 3500.00 | 2026-05-01 | May salary"
    //
    // HOW:  Override toString() using the @Override annotation.
    //       Use String.format() to combine getType(), id, amount, date, and description.
    //       Call getType() instead of hardcoding — this way each subclass shows its own type.
    //
    // WHY:  toString() is essential for debugging. When something goes wrong,
    //       printing the object should tell you everything about it.
    //
    // OBSERVE: After implementing, create a transaction and print it with System.out.println().
    //          You should see a formatted string, not a memory address.
}
