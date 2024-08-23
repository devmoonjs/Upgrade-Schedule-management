package com.sparta.upgradeschedulemanagement.repository;

import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {

    @EntityGraph(attributePaths = {"user", "todo"})
    @Query("SELECT ut FROM UserTodo ut WHERE ut.todo.todoId = :todoId")
    List<UserTodo> findUserTodosByTodoId(@Param("todoId") Long todoId);
}
