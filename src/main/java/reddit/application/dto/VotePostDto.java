package reddit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reddit.application.models.Post;
import reddit.application.models.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotePostDto {
    private Long id;
    private UserEntity user;
    private Post post;
    private int vote;

}
