package com.smartbudget.dao;

import com.smartbudget.model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// TICKET-F036 to F039 (Day 4, Sprint 3) — Raw JDBC DAO
// ============================================================
//
// WHAT: A DAO (Data Access Object) is a class whose ONLY job is to talk to the database.
//       It translates between Java objects and SQL queries.
//       This DAO uses raw JDBC — later (Day 5), you'll see how Spring Data JPA
//       does the same thing with ZERO SQL code.
//
//       SECURITY RULE: ALWAYS use PreparedStatement — NEVER concatenate user input into SQL.
//       Bad:  "SELECT * FROM transactions WHERE user_id = " + userId   ← SQL INJECTION ATTACK!
//       Good: "SELECT * FROM transactions WHERE user_id = ?"           ← Safe, uses parameter binding
//
// WHY:  Understanding raw JDBC helps you appreciate what Spring Data JPA does automatically.
//       In interviews, you may be asked about JDBC even if you use JPA day-to-day.
//
// ============================================================
public class TransactionDAO {

    public void insert(Transaction t) throws SQLException {
        String sql = """
                INSERT INTO transactions
                    (user_id, category_id, amount, txn_date, description, type)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getUserId());
            ps.setInt(2, t.getCategoryId());
            ps.setBigDecimal(3, t.getAmount());
            ps.setDate(4, Date.valueOf(t.getTxnDate()));
            ps.setString(5, t.getDescription());
            ps.setString(6, t.getType());
            ps.executeUpdate();
        }
    }


    public List<Transaction> getAll() throws SQLException {
        String sql = "SELECT txn_id, user_id, category_id, amount, txn_date, description, type FROM transactions ORDER BY txn_date DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private static Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("txn_id"),
                rs.getInt("user_id"),
                rs.getInt("category_id"),
                rs.getBigDecimal("amount"),
                rs.getDate("txn_date").toLocalDate(),
                rs.getString("description"),
                rs.getString("type")
        );
    }

    // -------------------------------------------------------
    // TODO TICKET-F038: Implement getByUserId(int userId) → List<Transaction>
    // -------------------------------------------------------
    // WHAT: Retrieves transactions for ONE specific user.
    //       Uses a PreparedStatement because the userId comes from user input.
    //
    // HOW:  Same pattern as getAll(), but:
    //       - Use a WHERE clause: WHERE user_id = ?
    //       - Use PreparedStatement (not Statement) because you have a parameter
    //       - Set the parameter: ps.setInt(1, userId)
    //       - The rest is identical: loop ResultSet, build Transaction objects, return list
    //
    // WHY:  This demonstrates the difference between Statement (no parameters)
    //       and PreparedStatement (with parameters). When user input is involved,
    //       ALWAYS use PreparedStatement.
    //
    // OBSERVE: Call getByUserId(1) — you should only see transactions for user 1.
    //          Call getByUserId(999) — you should get an empty list (no crash).
    public List<Transaction> getByUserId(int userId) throws SQLException {
        String sql = """
                SELECT txn_id, user_id, category_id, amount, txn_date, description, type
                FROM transactions
                WHERE user_id = ?
                ORDER BY txn_date DESC, txn_id DESC
                """;
        List<Transaction> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // -------------------------------------------------------
    // TODO TICKET-F039: Implement delete(int txnId)
    // -------------------------------------------------------
    // WHAT: Deletes a single transaction by its ID.
    //       executeUpdate() returns the number of rows affected.
    //
    // HOW:  1. Write the SQL: DELETE FROM transactions WHERE txn_id = ?
    //       2. Use PreparedStatement, set the txnId parameter
    //       3. Call ps.executeUpdate() — it returns an int (rows deleted)
    //       4. If the return value is 0, no record was found with that ID — log a warning
    //
    // WHY:  Checking the return value of executeUpdate() is good practice.
    //       If 0 rows were affected, the ID didn't exist — your code should handle this gracefully.
    //
    // OBSERVE: Call delete() with a valid ID, then getAll() — the record should be gone.
    //          Call delete() with a non-existent ID — no crash, just a warning message.
    public int delete(int txnId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE txn_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, txnId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("Warning: No transaction record found with txn_id=" + txnId);
            }
            return rowsAffected;
        }
    }

}
