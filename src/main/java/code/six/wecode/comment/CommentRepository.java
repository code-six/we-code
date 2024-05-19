package code.six.wecode.comment;

import code.six.wecode.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByAuthor(SiteUser user);

}
