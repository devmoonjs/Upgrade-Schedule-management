package com.sparta.upgradeschedulemanagement.repository;

import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {

    List<UserTodo> findUserTodosByTodo(Todo todo);
}
