package com.sparta.upgradeschedulemanagement.controller;

import com.sparta.upgradeschedulemanagement.dto.CommentRequestDto;
import com.sparta.upgradeschedulemanagement.dto.CommentResponseDto;
import com.sparta.upgradeschedulemanagement.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/comment/{todoId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long todoId, @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.createComment(todoId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 댓글 단건 조회
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentId) {
        CommentResponseDto responseDto = commentService.getComment(commentId);
        return ResponseEntity.ok().body(responseDto);
    }

    // 댓글 전체 조회
    @GetMapping("/comments/{todoId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable Long todoId) {
        return ResponseEntity.ok().body(commentService.getCommentList(todoId));
    }

    // 댓글 수정
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok().body(commentService.updateComment(commentId, requestDto));
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
