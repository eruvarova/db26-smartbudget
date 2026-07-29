package com.smartbudget.service;

import com.smartbudget.model.BaseTransaction;
import com.smartbudget.model.ExpenseTransaction;
import com.smartbudget.model.IncomeTransaction;
import com.smartbudget.model.InvalidTransactionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionServiceTest {

    private TransactionService svc;
    private IncomeTransaction income;
    private ExpenseTransaction expense;

    @BeforeEach
    void setUp() {
        svc = new TransactionService();

        income = new IncomeTransaction(
                1,
                new BigDecimal("3500"),
                LocalDate.of(2026, 1, 1),
                "Salary"
        );

        expense = new ExpenseTransaction(
                2,
                new BigDecimal("45"),
                LocalDate.of(2026, 1, 5),
                "Groceries"
        );
    }

    @Test
    void initiallyEmpty() {
        assertEquals(0, svc.size());
    }

    @Test
    void addTransaction_singleItem_isReturnedByGetAll() {
        // Arrange
        IncomeTransaction transaction = new IncomeTransaction(
                1,
                new BigDecimal("100"),
                LocalDate.now(),
                "Test"
        );

        // Act
        svc.addTransaction(transaction);

        // Assert
        List<BaseTransaction> all = svc.getAll();

        assertEquals(
                1,
                all.size(),
                "should contain exactly one transaction"
        );

        assertEquals(1, all.get(0).getTxnId());
        assertEquals(
                new BigDecimal("100"),
                all.get(0).getAmount()
        );
        assertEquals("INCOME", all.get(0).getType());
    }

    @Test
    void addTransaction_multipleItems_allReturned() {
        svc.addTransaction(income);
        svc.addTransaction(expense);

        List<BaseTransaction> all = svc.getAll();

        assertEquals(2, all.size());
    }

    @Test
    void getAll_returnsDefensiveCopy() {
        svc.addTransaction(income);

        List<BaseTransaction> returnedList = svc.getAll();
        returnedList.clear();

        assertEquals(
                1,
                svc.size(),
                "getAll() must return a defensive copy"
        );
    }


    @Test
    void delete_existingItem_removesIt() {
        // Arrange
        svc.addTransaction(income);
        assertEquals(1, svc.size());

        // Act
        boolean removed = svc.delete(String.valueOf(income.getTxnId()));

        // Assert
        assertTrue(removed, "delete should return true when item existed");
        assertEquals(0, svc.size());
        assertNull(svc.findById(String.valueOf(income.getTxnId())));
    }

    @Test
    void delete_missingItem_returnsFalseAndChangesNothing() {
        svc.addTransaction(income);

        boolean removed = svc.delete("999");

        assertFalse(removed);
        assertEquals(1, svc.size(), "delete of missing id must not affect state");
    }

    @Test
    void negativeAmount_throwsInvalidTransactionException() {
        InvalidTransactionException ex = assertThrows(
                InvalidTransactionException.class,
                () -> new IncomeTransaction(1, new BigDecimal("-10"),
                        LocalDate.now(), "bad"));

        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("amount") || msg.contains("greater than zero"),
                "Message should mention amount or 'greater than zero', got: " + ex.getMessage());
    }

    @Test
    void zeroAmount_throwsInvalidTransactionException() {
        assertThrows(InvalidTransactionException.class,
                () -> new ExpenseTransaction(2, BigDecimal.ZERO,
                        LocalDate.now(), "zero"));
    }

    @Test
    void futureDate_throwsInvalidTransactionException() {
        InvalidTransactionException ex = assertThrows(
                InvalidTransactionException.class,
                () -> new IncomeTransaction(3, new BigDecimal("100"),
                        LocalDate.now().plusDays(1), "future"));
        assertTrue(ex.getMessage().toLowerCase().contains("date"));
    }

    @Test
    void addNullTransaction_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> svc.addTransaction(null));
    }

}
