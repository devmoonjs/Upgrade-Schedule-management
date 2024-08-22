package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.CommentRequestDto;
import com.sparta.upgradeschedulemanagement.dto.CommentResponseDto;
import com.sparta.upgradeschedulemanagement.entity.Comment;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.repository.CommentRepository;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final TodoService todoService;

    // 댓글 생성
    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto) {
        Todo todo = todoService.findTodoById(todoId);
        Comment comment = new Comment(requestDto);
        comment.setTodo(todo);
        return CommentResponseDto.of(commentRepository.save(comment));
    }

    // 댓글 단건 조회
    public CommentResponseDto getComment(Long commentId) {
        return CommentResponseDto.of(findCommentById(commentId));
    }

    // 댓글 전체 조회
    public List<CommentResponseDto> getCommentList(Long todoId) {
        Todo todo = todoService.findTodoById(todoId);

        List<Comment> commentValue = todo.getCommentList();
        List<CommentResponseDto> commentList = new ArrayList<>();

        for (Comment comment : commentValue) {
            commentList.add(CommentResponseDto.of(comment));
        }
        return commentList;
    }

    // 댓글 수정
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = findCommentById(commentId);

        if (requestDto.getUserId() != null) comment.changeUser(requestDto.getUserId());
        if (requestDto.getContent() != null) comment.changeContent(requestDto.getContent());
        comment.changeUpdatedAt();

        return CommentResponseDto.of(commentRepository.save(comment));
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    // 댓글 존재 유무 검사
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
    }
}
