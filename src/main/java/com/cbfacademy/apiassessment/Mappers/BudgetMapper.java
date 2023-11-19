package com.cbfacademy.apiassessment.Mappers;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mapper(componentModel = "spring", uses = { UserRepository.class })
public interface BudgetMapper {


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
    BudgetMapper INSTANCE = Mappers.getMapper( BudgetMapper.class );
    @Mappings({
            @Mapping(source = "budgetAmount", target = "amount"),
            @Mapping(source = "budgetCategory", target = "category"),
            @Mapping(source = "budgetSubcategory", target = "subcategory"),
            @Mapping(source = "user.id", target = "user_id"),
            @Mapping(source = "budget.id", target = "id")
    })
    BudgetDTO budgetDTO (Budget budget);

    @Mappings({
            @Mapping(target = "budgetAmount", source = "amount"),
            @Mapping(target = "budgetCategory", source = "category"),
            @Mapping(target = "budgetSubcategory", source = "subcategory"),
            @Mapping(target = "user", expression = "java(toUser(budgetDto.getUser_id(), userRepository))"),
            @Mapping(source = "budgetDto.id", target = "id")
    })
    Budget toBudget(BudgetDTO budgetDto);

    default User toUser(Long userId, UserRepository userRepository) {
        return userId != null ? userRepository.getById(userId) : null;
    }


}
