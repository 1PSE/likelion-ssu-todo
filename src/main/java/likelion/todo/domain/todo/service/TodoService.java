package likelion.todo.domain.todo.service;

import likelion.todo.domain.member.entity.Member;
import likelion.todo.domain.member.repository.MemberRepository;
import likelion.todo.domain.todo.dto.TodoCreateRequestDTO;
import likelion.todo.domain.todo.dto.TodoCreateResponseDTO;
import likelion.todo.domain.todo.entity.Todo;
import likelion.todo.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TodoCreateResponseDTO createTodo(Long memberId, TodoCreateRequestDTO requestDTO) {
        Member member = getMember(memberId);
        Todo todo = Todo.builder()
                .member(member)
                .content(requestDTO.content())
                .date(requestDTO.date())
                .isChecked(false)
                .emoji("")
                .build();
        return TodoCreateResponseDTO.from(todoRepository.save(todo));
    }

    public List<TodoCreateResponseDTO> getAllTodos(Long memberId) {
        getMember(memberId);
        return todoRepository.findAllByMemberId(memberId).stream()
                .map(TodoCreateResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<TodoCreateResponseDTO> getDailyTodos(Long memberId, Integer month, Integer day) {
        getMember(memberId);
        LocalDate targetDate = LocalDate.now();

        if (month != null && day != null) {
            targetDate = LocalDate.of(targetDate.getYear(), month, day);
        } else if (month != null || day != null) { // 둘 중 하나만 오면 400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다.");
        }

        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        return todoRepository.findAllByMemberIdAndDateBetween(memberId, startOfDay, endOfDay).stream()
                .map(TodoCreateResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoCreateResponseDTO updateTodo(Long memberId, Long todoId, TodoCreateRequestDTO requestDTO) {
        Todo todo = getValidTodo(memberId, todoId);
        todo.update(requestDTO.content(), requestDTO.date());
        return TodoCreateResponseDTO.from(todo);
    }

    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        Todo todo = getValidTodo(memberId, todoId);
        todoRepository.delete(todo);
    }

    @Transactional
    public TodoCreateResponseDTO reviewTodo(Long memberId, Long todoId, String emoji) {
        Todo todo = getValidTodo(memberId, todoId);
        todo.updateEmoji(emoji);
        return TodoCreateResponseDTO.from(todo);
    }

    @Transactional
    public TodoCreateResponseDTO toggleCheck(Long memberId, Long todoId) {
        Todo todo = getValidTodo(memberId, todoId);
        todo.toggleCheck();
        return TodoCreateResponseDTO.from(todo);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."));
    }

    private Todo getValidTodo(Long memberId, Long todoId) {
        getMember(memberId);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "투두를 찾을 수 없습니다."));
        if (!todo.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 멤버의 투두가 아닙니다.");
        }
        return todo;
    }
}