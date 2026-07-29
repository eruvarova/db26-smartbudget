package com.smartbudget.repository;

import com.smartbudget.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// ============================================================
// TICKET-F051 (Day 5, Sprint 4) — Transaction Repository
// ============================================================
//
// WHAT: A Spring Data JPA Repository is an interface (NOT a class) that
//       gives you database operations for FREE — no SQL, no implementation code.
//       By extending JpaRepository<Transaction, Long>, you automatically get:
//         findAll()        → SELECT * FROM transactions
//         findById(Long)   → SELECT * WHERE txn_id = ?
//         save(Transaction)→ INSERT or UPDATE
//         deleteById(Long) → DELETE WHERE txn_id = ?
//         count()          → SELECT COUNT(*)
//       You NEVER write these methods — Spring generates them at startup.
//
// WHY:  Compare this with TransactionDAO.java (Day 4) where you wrote raw JDBC
//       with PreparedStatement, ResultSet, while(rs.next()), etc.
//       Spring Data JPA does ALL of that automatically. Same result, zero boilerplate.
//       This is why most Spring Boot apps use JPA repositories instead of raw JDBC.
//
// HOW IT WORKS:
//       JpaRepository<Transaction, Long> means:
//         Transaction = the @Entity class this repository manages
//         Long        = the type of the primary key (txnId is Long)
//       Spring scans for interfaces extending JpaRepository and creates
//       implementation classes at runtime using proxies.
//
// ============================================================

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /** 1) All transactions for a user, newest first. */
    List<Transaction> findByUser_UserIdOrderByTxnDateDesc(Long userId);

    /** 2) Filter by 'INCOME' or 'EXPENSE'. */
    List<Transaction> findByType(String type);

    /** 3) Inclusive date range. */
    List<Transaction> findByTxnDateBetween(LocalDate from, LocalDate to);

    /** 4) Sum amounts for a user + type. COALESCE keeps result non-null. */
    @Query("""
           SELECT COALESCE(SUM(t.amount), 0)
           FROM   Transaction t
           WHERE  t.user.userId = :userId
             AND  t.type        = :type
           """)
    BigDecimal sumByUserAndType(@Param("userId") Long userId,
                                @Param("type")   String type);    

    // -------------------------------------------------------
    // TODO TICKET-F051: Step 1 — Add findByUser_UserIdOrderByTxnDateDesc()
    // -------------------------------------------------------
    // WHAT: A custom query method that finds all transactions for a specific user,
    //       sorted by date (newest first). Spring Data JPA reads the method name
    //       and generates the SQL query automatically.
    //
    // HOW:  Declare a method (just the signature, no body — this is an interface).
    //       The method name follows a convention:
    //         findBy       → "SELECT ... WHERE"
    //         User_UserId  → "user.userId = ?" (navigates the @ManyToOne relationship)
    //         OrderBy      → "ORDER BY"
    //         TxnDateDesc  → "txn_date DESC" (descending = newest first)
    //       The method accepts a Long parameter and returns a List of Transactions.
    //
    // WHY:  The TransactionController's GET /api/transactions/user/{userId} endpoint
    //       needs this query. Without it, you'd have to get ALL transactions and
    //       filter in Java — much less efficient than letting the database do it.
    //
    // OBSERVE: After adding, restart the app and call GET /api/transactions/user/1.
    //          You should see only user 1's transactions, sorted newest first.
    //          Check the Spring console — you'll see the generated SQL query in the logs.

    // -------------------------------------------------------
    // TODO TICKET-F051: Step 2 — Add findByType()
    // -------------------------------------------------------
    // WHAT: Finds all transactions of a specific type (INCOME or EXPENSE).
    //       Spring reads "findByType" and generates: WHERE type = ?
    //
    // HOW:  Declare a method that accepts a String parameter (the type)
    //       and returns a List of Transactions.
    //       The method name must be exactly: findByType
    //       (matches the "type" field in the Transaction entity).
    //
    // WHY:  Useful for filtering — the dashboard might show "all expenses this month."
    //       Also demonstrates how simple Spring Data query derivation is.
    //
    // OBSERVE: Test with a method call: repo.findByType("INCOME")
    //          Should return only income transactions from the seed data.

    // -------------------------------------------------------
    // TODO TICKET-F051: Step 3 — Add findByTxnDateBetween()
    // -------------------------------------------------------
    // WHAT: Finds transactions within a date range.
    //       "Between" is a keyword Spring recognizes: WHERE txn_date BETWEEN ? AND ?
    //
    // HOW:  Declare a method that accepts two LocalDate parameters (from, to)
    //       and returns a List of Transactions.
    //       Method name: findByTxnDateBetween
    //       You will need to import java.time.LocalDate.
    //
    // WHY:  Date range filtering is essential for financial reports.
    //       "Show me all transactions from January to March 2026."
    //
    // OBSERVE: Test with dates that span your seed data.
    //          Narrow the range and verify fewer results come back.

    // -------------------------------------------------------
    // TODO TICKET-F051: Step 4 — Add @Query for sumByUserAndType
    // -------------------------------------------------------
    // WHAT: Some queries are too complex for method-name derivation.
    //       For these, you use @Query with JPQL (Java Persistence Query Language).
    //       JPQL looks like SQL but uses entity/field names instead of table/column names.
    //       This query calculates the total amount for a user's income or expenses.
    //
    // HOW:  Declare a method called sumByUserAndType that accepts userId and type.
    //       Annotate it with @Query and write a JPQL SELECT that uses:
    //         SUM(t.amount)   → adds up all amounts
    //         COALESCE(...,0) → returns 0 instead of null when there are no matching records
    //         WHERE t.user.userId = :userId AND t.type = :type
    //       Mark each parameter with @Param("paramName") to bind to :paramName in the query.
    //       The return type should be BigDecimal.
    //
    // WHY:  The Dashboard needs "Total Income: £X" and "Total Expenses: £Y."
    //       Doing this in SQL is much faster than fetching all transactions
    //       and summing in Java — especially with thousands of records.
    //
    // OBSERVE: Call sumByUserAndType(1L, "INCOME") — should return the sum of user 1's income.
    //          Call sumByUserAndType(999L, "INCOME") — should return 0 (not null), thanks to COALESCE.
}
