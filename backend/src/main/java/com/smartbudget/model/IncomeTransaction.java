package com.smartbudget.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// ============================================================
// TICKET-F022 (Day 3, Sprint 2) — Income Transaction Subclass
// ============================================================
//
// WHAT: This class EXTENDS BaseTransaction, which means it inherits all of
//       BaseTransaction's fields (txnId, amount, txnDate, description) and methods.
//       "extends" creates a parent-child (IS-A) relationship:
//       "An IncomeTransaction IS-A BaseTransaction."
//
// WHY:  Income transactions have an extra field: "source" (where the money came from).
//       Instead of duplicating all the common fields, we inherit them from the parent
//       and just add what's unique to income.
//
// ============================================================
public class IncomeTransaction extends BaseTransaction {

    // -------------------------------------------------------
    // TODO TICKET-F022: Step 1 — Add a "source" field
    // -------------------------------------------------------
    // WHAT: The "source" field describes where the income came from.
    //       Examples: "Salary", "Freelance", "Investment Dividends", "Gift"
    //
    // HOW:  Declare a private String field named "source".
    //       Use private (not protected) because no class extends IncomeTransaction.
    //
    // WHY:  This field makes IncomeTransaction different from ExpenseTransaction.
    //       Each subclass adds its own unique data on top of the shared parent fields.

    public IncomeTransaction(int txnId, BigDecimal amount,
                             LocalDate txnDate, String description) {
        super(txnId, amount, txnDate, description);
    }

    @Override
    public String getType() {
        return "INCOME";
    }

    // -------------------------------------------------------
    // TODO TICKET-F022: Step 2 — Constructor
    // -------------------------------------------------------
    // WHAT: The constructor must call the parent's constructor using super().
    //       super() passes the shared fields UP to BaseTransaction's constructor,
    //       which handles validation (amount > 0, date not future).
    //
    // HOW:  Create a constructor that accepts 5 parameters:
    //         txnId, amount, txnDate, description (same as parent) + source (new).
    //       First line: call super(txnId, amount, txnDate, description)
    //       Second line: assign this.source = source
    //       super() MUST be the very first line — Java enforces this.
    //
    // WHY:  The parent constructor handles validation. By calling super() first,
    //       you reuse that logic without writing it again.
    //       If amount is negative, super() throws InvalidTransactionException
    //       BEFORE your source field is even set — the object is never created.
    //
    // OBSERVE: Try creating: new IncomeTransaction(1, new BigDecimal("3500"), LocalDate.now(), "Salary", "Company")
    //          It should work. Try with negative amount — it should throw an exception
    //          (the parent's validation kicks in).

    // -------------------------------------------------------
    // TODO TICKET-F022: Step 3 — Implement getType()
    // -------------------------------------------------------
    // WHAT: This overrides the abstract method from BaseTransaction.
    //       The @Override annotation tells the compiler: "I'm intentionally
    //       replacing the parent's method." If you misspell the method name,
    //       @Override causes a compile error (catching your typo).
    //
    // HOW:  Add a public method getType() that returns the String "INCOME".
    //       Add the @Override annotation above it.
    //
    // WHY:  When code calls transaction.getType(), Java automatically calls
    //       the correct version based on the actual object type.
    //       This is runtime polymorphism — the method that runs is determined
    //       by the object, not the variable type.
    //
    // OBSERVE: Create an IncomeTransaction and call getType(). It returns "INCOME".
    //          Create an ExpenseTransaction and call getType(). It returns "EXPENSE".
    //          Same method name, different results — that's polymorphism.

    // -------------------------------------------------------
    // TODO TICKET-F022: Step 4 — Getter/setter for source + toString()
    // -------------------------------------------------------
    // WHAT: Getter lets other code read the source field.
    //       toString() should include the parent's toString() plus the source.
    //
    // HOW:  Add getSource() and setSource() methods.
    //       Override toString() — call super.toString() to get the parent's output,
    //       then append " | source=" + source to it.
    //
    // WHY:  Calling super.toString() reuses the parent's formatting.
    //       The child only adds its unique field, keeping the output consistent.
    //
    // OBSERVE: Print an IncomeTransaction — you should see the parent's fields
    //          PLUS the source field at the end.
}
