package com.cbfacademy.apiassessment.Repository;

import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long>{
   Expenses findByUserAndID(User user, Long Id);
   List<Expenses> findByUserAndBudget(User user, Budget budget);
   List<Expenses> findByUser(User user);
   List<Expenses> findByUserAndExpenseCategory(User user, SubCategories.Category category );
   @Query("SELECT e FROM Expenses e WHERE e.user = :user AND e.Amount BETWEEN :minPrice AND :maxPrice")
   List<Expenses> findExpensesInPriceRange(User user, double minPrice, double maxPrice);

   @Query("SELECT e FROM Expenses e WHERE e.user = :user AND e.createdAt BETWEEN :startDate AND :endDate")
   List<Expenses> findExpensesByDateRange(User user, Date startDate, Date endDate);

}
