package com.cbfacademy.apiassessment.Repository;

import com.cbfacademy.apiassessment.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<List<Budget>> findByUser(User user);
    Optional<Budget> findByUserAndId(User user, Long budgetId);
    //@Query("SELECT * FROM Budget b WHERE b.user_id = :user_id AND b.budgetCategory = :budgetCategory")
    List<Budget> findByUserIdAndBudgetCategory(Long user_id, SubCategories.Category budgetCategory);
    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.budgetAmount BETWEEN :minPrice AND :maxPrice")
    List<Budget> findBudgetsInPriceRange(User user, double minPrice, double maxPrice);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.createdAt BETWEEN :startDate AND :endDate")
    List<Budget> findBudgetsByDateRange(User user, Date startDate, Date endDate);


}
