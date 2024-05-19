package code.six.wecode.post;

import code.six.wecode.DataNotFoundException;
import code.six.wecode.comment.Comment;
import code.six.wecode.post.Post;
import code.six.wecode.post.PostRepository;
import code.six.wecode.user.SiteUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getPostsByUser(SiteUser user) {
        return postRepository.findByAuthor(user);
    }

    @SuppressWarnings("unused")
    private Specification<Post> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Post> p, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복을 제거
                Join<Post, SiteUser> u1 = p.join("author", JoinType.LEFT);
                Join<Post, Comment> c = p.join("commentList", JoinType.LEFT);
                Join<Comment, SiteUser> u2 = c.join("author", JoinType.LEFT);
                return cb.or(cb.like(p.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(p.get("content"), "%" + kw + "%"), // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"), // 게시글 작성자
                        cb.like(c.get("content"), "%" + kw + "%"), // 댓글 내용
                        cb.like(u2.get("username"), "%" + kw + "%")); // 댓글 작성자
            }
        };
    }

    public Page<Post> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.postRepository.findAllByKeyword(kw, pageable);
    }

    public Post getPost(Integer id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("Post not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        Post p = new Post();
        p.setSubject(subject);
        p.setContent(content);
        p.setCreateDate(LocalDateTime.now());
        p.setAuthor(user);
        this.postRepository.save(p);
    }

    public void modify(Post post, String subject, String content) {
        post.setSubject(subject);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    public void vote(Post post, SiteUser siteUser) {
        post.getVoter().add(siteUser);
        this.postRepository.save(post);
    }
}

