package reddit.application.dto;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import reddit.application.models.UserEntity;

@Data
@Builder
public class CommunityDto {
    private Long id;
    @NotEmpty(message = "Community title should not be empty")
    private String name;
    @NotEmpty(message = "Community title should not be empty")
    private String description;
    private String communityPictureUrl;
    private String communityPictureHeaderUrl;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private UserEntity createdBy;
    private List<PostDto> posts;
    private List<MembershipDto> memberships;


}
