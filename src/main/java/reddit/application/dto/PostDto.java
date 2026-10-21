package reddit.application.dto;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reddit.application.models.Community;
import reddit.application.models.Membership;
import reddit.application.models.UserEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
        private Long id;
        @NotEmpty(message = "Post title should not be empty")
        private String title;
        @NotEmpty(message = "Post description should not be empty")
        private String description;
        private String photoUrl;
        private int votes;
        private LocalDateTime createdOn;
        private LocalDateTime updatedOn;
        private Community community;
        private UserEntity createdBy;
        private List<CommentDto> comments;
        private List<VotePostDto> votePosts;


}
