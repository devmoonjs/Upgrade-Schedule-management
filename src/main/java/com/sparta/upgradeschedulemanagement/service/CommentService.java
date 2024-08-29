package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.CommentRequestDto;
import com.sparta.upgradeschedulemanagement.dto.CommentResponseDto;
import com.sparta.upgradeschedulemanagement.entity.Comment;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.exception.EntityNotFoundException;
import com.sparta.upgradeschedulemanagement.jwt.JwtUtil;
import com.sparta.upgradeschedulemanagement.repository.CommentRepository;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import com.sparta.upgradeschedulemanagement.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final TodoService todoService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto, HttpServletRequest httpServletRequest) {
        Todo todo = todoService.findTodoById(todoId);

        String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);
        String token = jwtUtil.subStringToken(tokenValue);
        Claims info = jwtUtil.getUserInfoFromToken(token);

        User user = userRepository.findByName(info.getSubject()).orElseThrow();

        Comment comment = new Comment(requestDto, user, todo);
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
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = findCommentById(commentId);

        if (requestDto.getContent() != null) comment.changeContent(requestDto.getContent());

        return CommentResponseDto.of(commentRepository.save(comment));
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    // 댓글 존재 유무 검사
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 댓글입니다.")
        );
    }
}
