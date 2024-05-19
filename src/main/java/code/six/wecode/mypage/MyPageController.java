package code.six.wecode.mypage;

import code.six.wecode.answer.AnswerService;
import code.six.wecode.comment.Comment;
import code.six.wecode.comment.CommentService;
import code.six.wecode.post.Post;
import code.six.wecode.post.PostService;
import code.six.wecode.user.SiteUser;
import code.six.wecode.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
public class MyPageController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final PostService postService;
    private final AnswerService answerService;
    private final CommentService commentService;

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")        //프로필 관련 코드
    public String myPage(Model model, Principal principal) {
        // 사용자 정보 및 게시물, 댓글 정보 불러오기
        SiteUser userinfo = userService.getUser(principal.getName());
        if (userinfo == null) {
            return "redirect:/user/login";  // 로그인 페이지로 리다이렉트
        }
        model.addAttribute("user", userinfo);
        List<Post> myPosts = postService.getPostsByUser(userinfo);
        List<Comment> myComments = commentService.getCommentsByUser(userinfo);
        model.addAttribute("posts", myPosts);
        model.addAttribute("comments", myComments);
        return "mypage";
    }

    @PostMapping("/updateNickname")             //닉네임 변경
    public ResponseEntity<?> updateNickname(@RequestParam String nickname, Principal principal) {
        String username = principal.getName();
        myPageService.updateNickname(username, nickname);

        // 사용자 세션 무효화
        SecurityContextHolder.getContext().setAuthentication(null);

        // 클라이언트에게 로그아웃 후 리다이렉트 명령 보내기
        return ResponseEntity.ok().header("Logout", "true").body("닉네임이 변경되었습니다. 5초후 로그아웃 됩니다, 변경된 닉네임으로 다시 로그인 해주세요");
    }

    @PostMapping("/updateEmail")                //이메일 변경
    public ResponseEntity<String> updateEmail(@RequestParam String email, Principal principal) {
        String username = principal.getName();
        myPageService.updateEmail(username, email);
        return ResponseEntity.ok("이메일이 변경 되었습니다.");
    }

    @PostMapping("/updatePassword")             //패스워드 변경
    public ResponseEntity<String> updatePassword(@RequestParam String currentPassword, @RequestParam String newPassword, Principal principal) {
        String username = principal.getName();
        try {
            myPageService.updatePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok("비밀번호가 변경 되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/uploadProfile")
    public String uploadProfile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal SiteUser user, Principal principal, RedirectAttributes redirectAttributes) {
        if (user == null && principal != null) {
            user = userService.getUser(principal.getName());
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
                return "redirect:/user/login";
            }
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "업로드할 파일을 선택해주세요.");
            return "redirect:/user/mypage";
        }

        try {
            String fileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            String uploadDir = "/app/uploads";  // 변경된 경로
            Path uploadPath = Paths.get(uploadDir);

            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            String relativePath = "/uploads/" + fileName;
            userService.updateUserProfileImage(user.getId(), relativePath);
            redirectAttributes.addFlashAttribute("message", "프로필 이미지가 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "서버 오류로 인해 이미지를 업로드하지 못했습니다.");
        }

        return "redirect:/user/mypage";
    }

}
