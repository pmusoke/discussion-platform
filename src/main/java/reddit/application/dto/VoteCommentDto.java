package reddit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reddit.application.models.Comment;
import reddit.application.models.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteCommentDto {
    private Long id;
    private UserEntity user;
    private Comment comment;
    private int vote;

}
