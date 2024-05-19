package code.six.wecode;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // 메인 화면을 http://localhost:8080/question/list에서 http://localhost:8080/post/list로 변경
    @GetMapping("/")
    public String root() {
        return "redirect:/post/list";
    }
}
