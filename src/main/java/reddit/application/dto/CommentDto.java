package reddit.application.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reddit.application.models.Comment;
import reddit.application.models.Post;
import reddit.application.models.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotEmpty(message="Comment cannot be empty")
    private String comment;
    private int votes;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Post post;
    private Comment parentComment;
    private List<CommentDto> replies;
    private UserEntity createdBy;
    private List<VoteCommentDto> voteComments;


}
