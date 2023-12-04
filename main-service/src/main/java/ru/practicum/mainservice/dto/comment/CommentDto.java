package ru.practicum.mainservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.dto.user.UserShortDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class CommentDto {
    protected Long id;
    protected UserShortDto author;
    protected Boolean isEventAuthor;
    protected String text;
    protected Collection<CommentDto> childComments;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime commentDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateDate;
}
