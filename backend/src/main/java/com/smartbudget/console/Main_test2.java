package com.smartbudget.console;

import java.sql.SQLException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.sql.Connection;

import com.smartbudget.dao.DatabaseConnection;
import com.smartbudget.dao.TransactionDAO;
import com.smartbudget.model.Transaction;

public class Main_test2 {
    public static void main(String[] args) {

        try (Connection c = DatabaseConnection.getConnection()) {

            System.out.println("Connected to: " + c.getMetaData().getURL());

            TransactionDAO dao = new TransactionDAO();

            // dao.getAll().forEach(System.out::println);
            // System.out.println(dao.getByUserId(1).size());       // 5 (with the F005 seed)
            // System.out.println(dao.getByUserId(999).size());     // 0 (not found)

            int countBefore = dao.getAll().size();

            dao.insert(new Transaction(0, 1, 1,
                    new BigDecimal("99.99"), LocalDate.now(),
                    "Will be deleted", "EXPENSE"));

            int countAfter = dao.getAll().size();
            System.out.println("inserted? " + (countAfter == countBefore + 1));

            // Find the txn we just made and delete it
            int lastId = dao.getAll().stream()
                    .mapToInt(Transaction::getTxnId).max().getAsInt();
            int affected = dao.delete(lastId);
            System.out.println("delete returned: " + affected);   // 1
            System.out.println("delete 9_999_999 returned: "
                    + dao.delete(9_999_999));  

        } catch (SQLException e) {

            System.err.println("Database error: " + e.getMessage());

        }
    }
}
