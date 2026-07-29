package com.smartbudget.repository;

import com.smartbudget.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// ============================================================
// TICKET-F052 (Day 5, Sprint 4) — Category Repository
// ============================================================
//
// WHAT: A JPA Repository for the Category entity.
//       This is the simplest repository in the project.
//       CategoryController (the Day 1 proof-of-life endpoint) already uses
//       the inherited findAll() method to return all categories.
//
// WHY:  Categories classify transactions (e.g., "Salary" = INCOME, "Groceries" = EXPENSE).
//       The AddTransactionForm needs a dropdown of categories, which comes from
//       GET /api/categories → CategoryController → this repository's findAll().
//
// HOW IT WORKS:
//       JpaRepository<Category, Long> gives you findAll(), findById(), save(), etc.
//       Your task is to add ONE custom query method for filtering by type.
//
// ============================================================

public interface CategoryRepository extends JpaRepository<Category, Long> {
    /** All categories of a given type ('INCOME' or 'EXPENSE'). */
    List<Category> findByType(String type);

    // -------------------------------------------------------
    // TODO TICKET-F052: Add findByType()
    // -------------------------------------------------------
    // WHAT: Finds categories filtered by type (INCOME or EXPENSE).
    //       Spring reads "findByType" → generates: SELECT * FROM categories WHERE type = ?
    //
    // HOW:  Declare a method that accepts a String parameter (the type)
    //       and returns a List of Category entities.
    //       Method name: findByType
    //       You will need to import java.util.List.
    //
    // WHY:  The AddTransactionForm might want to show only EXPENSE categories
    //       when the user is adding an expense (not salary, dividends, etc.).
    //       Without this filter, all categories appear in every dropdown.
    //
    // OBSERVE: After adding, you can test by calling:
    //          repo.findByType("INCOME") → should return Salary, Freelance
    //          repo.findByType("EXPENSE") → should return Groceries, Rent, Transport
    //          (based on the seed data in data.sql)
}
