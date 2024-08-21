package com.sparta.upgradeschedulemanagement.repository;

import com.sparta.upgradeschedulemanagement.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
