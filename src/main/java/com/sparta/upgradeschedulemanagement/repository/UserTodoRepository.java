package com.sparta.upgradeschedulemanagement.repository;

import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {
}
