package com.smartbudget.console;

import com.smartbudget.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Main_test {
    public static void main(String[] args) {
        List<BaseTransaction> mixed = new ArrayList<>();
        mixed.add(new IncomeTransaction (1, new BigDecimal("3500"),
                LocalDate.now(), "Salary"));
        mixed.add(new ExpenseTransaction(2, new BigDecimal("45"),
                LocalDate.now(), "Groceries", "Food"));
        mixed.add(new IncomeTransaction (3, new BigDecimal("800"),
                LocalDate.now(), "Freelance"));
        mixed.add(new ExpenseTransaction(4, new BigDecimal("120"),
                LocalDate.now(), "Bills", "Utilities"));

        System.out.println("Total rows: " + mixed.size());
        for (BaseTransaction t : mixed) {
            System.out.println(t);    // toString() dispatches to subclass's getType()
        }

        // Sum income with no instanceof:
        BigDecimal income = BigDecimal.ZERO;
        for (BaseTransaction t : mixed) {
            if ("INCOME".equals(t.getType())) income = income.add(t.getAmount());
        }
        System.out.println("Total income: " + income);
    }
}
