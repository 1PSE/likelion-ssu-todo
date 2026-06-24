package likelion.todo.domain.todo.repository;

import likelion.todo.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByMemberId(Long memberId);
    List<Todo> findAllByMemberIdAndDateBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}
