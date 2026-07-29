package com.smartbudget.repository;

import com.smartbudget.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// ============================================================
// TICKET-F050 (Day 5, Sprint 4) — User Repository
// ============================================================
//
// WHAT: A JPA Repository for the User entity.
//       By extending JpaRepository<User, Long>, you get all standard
//       CRUD operations for FREE: findAll(), findById(), save(), deleteById().
//       Your task is to add custom query methods specific to users.
//
// WHY:  Users are referenced by Transactions (via @ManyToOne) and SavingsGoals.
//       The service layer needs to look up users by email (for login/validation)
//       and check if an email already exists (for registration).
//
// HOW IT WORKS:
//       JpaRepository<User, Long> means:
//         User = the @Entity class (maps to the "users" table)
//         Long = the type of the primary key (userId is Long)
//       Spring creates the implementation at startup — you only declare methods.
//
// ============================================================
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // -------------------------------------------------------
    // TODO TICKET-F050: Step 1 — Add findByEmail()
    // -------------------------------------------------------
    // WHAT: Finds a user by their email address.
    //       Returns Optional<User> — which means "might be empty."
    //       Optional forces the caller to handle the "not found" case.
    //
    // HOW:  Declare a method signature (no body — this is an interface).
    //       Method name: findByEmail
    //       Parameter: String email
    //       Return type: Optional<User>
    //       You will need to import java.util.Optional.
    //       Spring reads "findByEmail" → generates: SELECT * FROM users WHERE email = ?
    //
    // WHY:  Email is a natural lookup key for users (like a username).
    //       Returning Optional instead of User prevents NullPointerException.
    //       The caller must explicitly handle the empty case:
    //         repo.findByEmail("alice@db.com").orElseThrow(...)
    //
    // OBSERVE: After adding, test it in a service or controller.
    //          Search for "alice@db.com" → should find the seed user.
    //          Search for "nonexistent@db.com" → should return Optional.empty().

     /** WHERE email = ? */
    Optional<User> findByEmail(String email);
    
    // TODO TICKET-F050: Step 2 — Add existsByEmail()
    // -------------------------------------------------------
    // WHAT: Checks if a user with the given email already exists.
    //       Returns a simple boolean — true or false.
    //       Spring reads "existsBy" → generates: SELECT COUNT(*) > 0 WHERE email = ?
    //
    // HOW:  Declare a method that accepts a String email and returns boolean.
    //       Method name must start with "existsBy" followed by the field name.
    //
    // WHY:  Before creating a new user, you should check if the email is taken.
    //       existsByEmail is simpler and faster than findByEmail when you only
    //       need a yes/no answer — no need to load the entire User object.
    //
    // OBSERVE: existsByEmail("alice@db.com") → should return true (seed data).
    //          existsByEmail("new@db.com") → should return false.

    /** SELECT EXISTS(SELECT 1 FROM users WHERE email = ?) */
    boolean existsByEmail(String email);

    /** Bonus: WHERE LOWER(name) LIKE LOWER('%search%') */
    List<User> findByNameContainingIgnoreCase(String search);   

    // -------------------------------------------------------

}
