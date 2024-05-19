package code.six.wecode.comment;

import code.six.wecode.post.Post;
import code.six.wecode.post.PostService;
import code.six.wecode.user.SiteUser;
import code.six.wecode.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{postId}")
    public String createComment(Model model, @PathVariable("postId") Integer postId, @Valid CommentForm commentForm,
                                BindingResult bindingResult, Principal principal) {
        Post post = this.postService.getPost(postId);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post_detail";
        }
        Comment comment = this.commentService.create(post, commentForm.getContent(), siteUser);
        return String.format("redirect:/post/detail/%s#comment_%s", comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{commentId}")
    public String commentModify(CommentForm commentForm, @PathVariable("commentId") Integer commentId, Principal principal) {
        Comment comment = this.commentService.getComment(commentId);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{commentId}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("commentId") Integer commentId, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = this.commentService.getComment(commentId);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/post/detail/%s#comment_%s", comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{commentId}")
    public String commentDelete(Principal principal, @PathVariable("commentId") Integer commentId) {
        Comment comment = this.commentService.getComment(commentId);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/post/detail/%s", comment.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{commentId}")
    public String commentVote(Principal principal, @PathVariable("commentId") Integer commentId) {
        Comment comment = this.commentService.getComment(commentId);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.commentService.vote(comment, siteUser);
        return String.format("redirect:/post/detail/%s#comment_%s", comment.getPost().getId(), comment.getId());
    }
}

