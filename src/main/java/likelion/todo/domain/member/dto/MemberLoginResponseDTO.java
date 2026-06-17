package likelion.todo.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

public record MemberLoginResponseDTO(
        @JsonProperty("member_id")
        Long memberId
) {
}
