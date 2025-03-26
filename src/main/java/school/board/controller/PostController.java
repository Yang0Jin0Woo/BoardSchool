package school.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import school.board.domain.Post;
import school.board.service.ExternalBoardService;
import school.board.service.PostService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final ExternalBoardService externalBoardService;

    public PostController(ExternalBoardService externalBoardService) {
        this.externalBoardService = externalBoardService;
    }

    @GetMapping
    public String showList(Model model) {
        try {
            List<Post> posts = externalBoardService.fetchPosts();
//            posts.sort((p1, p2) -> Integer.compare(p2.getViewCount(), p1.getViewCount()));
            model.addAttribute("posts", posts);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("posts", null);
        }
        return "school/list";
    }

    @GetMapping("/view/{cid}")
    public String showDetail(@PathVariable String cid, Model model) {
        String gid = "1115983888724"; // 고정된 값
        String bid = "1115985252888"; // 고정된 값
        try {
            String html = externalBoardService.fetchPostContent(gid, bid, cid);
            model.addAttribute("content", html);
        } catch (IOException e) {
            model.addAttribute("content", "내용을 불러오는 데 실패했습니다.");
        }
        return "school/view"; // view.html
    }

}
