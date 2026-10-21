package reddit.application.dto;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reddit.application.models.Community;
import reddit.application.models.UserEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDto {
    private Long id;
    private UserEntity user;
    private Community community;
    private LocalDateTime joinedOn;
}
