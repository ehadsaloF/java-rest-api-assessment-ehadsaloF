package com.cbfacademy.apiassessment.Repository;

import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long>{
   Optional<Expenses> findByUserAndId(User user, Long expensesId);
   List<Expenses> findByUserAndBudget(User user, Budget budget);
   List<Expenses> findByUser(User user);
   List<Expenses> findByUserAndExpenseCategory(User user, SubCategories.Category category );
   @Query("SELECT b FROM Expenses b WHERE b.user = :user AND b.expenseAmount > :minPrice")
   List<Expenses> findExpensesGreaterThan(User user, double minPrice);

   @Query("SELECT b FROM Expenses b WHERE b.user = :user AND b.expenseAmount < :maxPrice")
   List<Expenses> findExpensesLessThan(User user,double maxPrice);

   @Query("SELECT b FROM Expenses b WHERE b.user = :user AND b.expenseAmount BETWEEN :minPrice AND :maxPrice")
   List<Expenses> findExpensesInPriceRange(User user, double minPrice, double maxPrice);

   @Query("SELECT b FROM Expenses b WHERE b.user = :user AND b.createdAt BETWEEN :startDate AND :endDate")
   List<Expenses> findExpensesByDateRange(User user, Date startDate, Date endDate);

   @Query("SELECT b FROM Expenses b WHERE b.user = :user AND b.createdAt < :endDate")
   List<Expenses> findExpensesBefore(User user, Date endDate);

   @Query("SELECT b FROM Expenses b WHERE b.user = :user AND b.createdAt > :startDate")
   List<Expenses> findExpensesAfter(User user, Date startDate);

}
