package likelion.todo.domain.todo.controller;

import likelion.todo.domain.todo.dto.TodoCreateRequestDTO;
import likelion.todo.domain.todo.dto.TodoCreateResponseDTO;
import likelion.todo.domain.todo.dto.TodoReviewRequestDTO;
import likelion.todo.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/{member_id}/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoCreateResponseDTO> createTodo(
            @PathVariable("member_id") Long memberId,
            @RequestBody TodoCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(todoService.createTodo(memberId, requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<TodoCreateResponseDTO>> getAllTodos(
            @PathVariable("member_id") Long memberId) {
        return ResponseEntity.ok(todoService.getAllTodos(memberId));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<TodoCreateResponseDTO>> getDailyTodos(
            @PathVariable("member_id") Long memberId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day) {
        return ResponseEntity.ok(todoService.getDailyTodos(memberId, month, day));
    }

    @PatchMapping("/{todo_id}")
    public ResponseEntity<TodoCreateResponseDTO> updateTodo(
            @PathVariable("member_id") Long memberId,
            @PathVariable("todo_id") Long todoId,
            @RequestBody TodoCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(todoService.updateTodo(memberId, todoId, requestDTO));
    }

    @DeleteMapping("/{todo_id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable("member_id") Long memberId,
            @PathVariable("todo_id") Long todoId) {
        todoService.deleteTodo(memberId, todoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content 로 수정
    }

    @PatchMapping("/{todo_id}/reviews")
    public ResponseEntity<TodoCreateResponseDTO> reviewTodo(
            @PathVariable("member_id") Long memberId,
            @PathVariable("todo_id") Long todoId,
            @RequestBody TodoReviewRequestDTO requestDTO) {
        return ResponseEntity.ok(todoService.reviewTodo(memberId, todoId, requestDTO.emoji()));
    }

    @PatchMapping("/{todo_id}/check")
    public ResponseEntity<TodoCreateResponseDTO> toggleCheck(
            @PathVariable("member_id") Long memberId,
            @PathVariable("todo_id") Long todoId) {
        return ResponseEntity.ok(todoService.toggleCheck(memberId, todoId));
    }
}