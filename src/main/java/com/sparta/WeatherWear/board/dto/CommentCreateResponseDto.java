package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.WeatherWear.board.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class CommentCreateResponseDto {
    private long id;
    // 사용자 정보
    private UserSimpleDto user = new UserSimpleDto();

    private Long BoardId;
    private String contents;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public CommentCreateResponseDto(Comment comment) {
        this.id = comment.getId();
        //
        this.user.setId(comment.getUser().getId());
        this.user.setNickname(comment.getUser().getNickname());
        this.user.setImage(comment.getUser().getImage());
        //
        this.BoardId = comment.getBoard().getId();
        this.contents = comment.getContents();
        //시간
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

}
