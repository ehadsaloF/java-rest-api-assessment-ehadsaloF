package com.cbfacademy.apiassessment.Mappers;

import com.cbfacademy.apiassessment.DTO.ExpensesDTO;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Repository.BudgetRepository;
import com.cbfacademy.apiassessment.Repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mapper
public interface ExpensesMapper {
    ExpensesMapper INSTANCE = Mappers.getMapper( ExpensesMapper.class );

    UserRepository userRepository = new UserRepository() {
        @Override
        public Optional<User> findByUsername(String username) {
            return Optional.empty();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends User> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<User> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public User getOne(Long aLong) {
            return null;
        }

        @Override
        public User getById(Long aLong) {
            return null;
        }

        @Override
        public User getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends User> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public List<User> findAll() {
            return null;
        }

        @Override
        public List<User> findAllById(Iterable<Long> longs) {
            return null;
        }

        @Override
        public <S extends User> S save(S entity) {
            return null;
        }

        @Override
        public Optional<User> findById(Long aLong) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Long aLong) {
            return false;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {

        }

        @Override
        public void delete(User entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {

        }

        @Override
        public void deleteAll(Iterable<? extends User> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public List<User> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<User> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends User> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends User> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends User> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }
    };
    BudgetRepository budgetRepository = new BudgetRepository() {
        @Override
        public Optional<List<Budget>> findByUser(User user) {
            return Optional.empty();
        }

        @Override
        public Optional<Budget> findByUserAndId(User user, Long budgetId) {
            return Optional.empty();
        }

        @Override
        public List<Budget> findByUserIdAndBudgetCategory(Long user_id, SubCategories.Category budgetCategory) {
            return null;
        }

        @Override
        public List<Budget> findBudgetsInPriceRange(User user, double minPrice, double maxPrice) {
            return null;
        }

        @Override
        public List<Budget> findBudgetsGreaterThan(User user, double minPrice) {
            return null;
        }

        @Override
        public List<Budget> findBudgetsLessThan(User user, double maxPrice) {
            return null;
        }

        @Override
        public List<Budget> findBudgetsByDateRange(User user, Date startDate, Date endDate) {
            return null;
        }

        @Override
        public List<Budget> findBudgetsBefore(User user, Date endDate) {
            return null;
        }

        @Override
        public List<Budget> findBudgetsAfter(User user, Date startDate) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends Budget> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends Budget> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<Budget> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public Budget getOne(Long aLong) {
            return null;
        }

        @Override
        public Budget getById(Long aLong) {
            return null;
        }

        @Override
        public Budget getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends Budget> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends Budget> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends Budget> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public List<Budget> findAll() {
            return null;
        }

        @Override
        public List<Budget> findAllById(Iterable<Long> longs) {
            return null;
        }

        @Override
        public <S extends Budget> S save(S entity) {
            return null;
        }

        @Override
        public Optional<Budget> findById(Long aLong) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Long aLong) {
            return false;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {

        }

        @Override
        public void delete(Budget entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {

        }

        @Override
        public void deleteAll(Iterable<? extends Budget> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public List<Budget> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<Budget> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Budget> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Budget> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Budget> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Budget> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Budget, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }
    };

    @Mappings({
            @Mapping(source = "expenseAmount", target = "amount"),
            @Mapping(source = "expenseCategory", target = "category"),
            @Mapping(source = "expenseSubcategory", target = "subcategory"),
            @Mapping(source = "user.id", target = "user_id"),
            @Mapping(source = "budget.id", target = "budget_id"),
            @Mapping(source = "expenses.id", target = "id")
    })
    ExpensesDTO expensesDTO(Expenses expenses);

    @Mappings({
            @Mapping(target = "expenseAmount", source = "amount"),
            @Mapping(target = "expenseCategory", source = "category"),
            @Mapping(target = "expenseSubcategory", source = "subcategory"),
            @Mapping(target = "user", expression = "java(toUser(expensesDto.getUser_id(), userRepository))"),
            @Mapping(target = "budget", expression = "java(toBudget(expensesDto.getBudget_id(), budgetRepository))"),
            @Mapping(source = "expensesDto.id", target = "id")
    })
    Expenses toExpenses(ExpensesDTO expensesDto);

    default User toUser(Long userId, UserRepository userRepository) {
        return userId != null ? userRepository.getById(userId) : null;
    }

    default Budget toBudget(Long budgetId, BudgetRepository budgetRepository) {
        return budgetId != null ? budgetRepository.getById(budgetId) : null;
    }
}
