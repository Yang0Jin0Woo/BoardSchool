package school.board.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import school.board.domain.Post;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalBoardService {

    private static final String BOARD_URL = "https://cyber.wku.ac.kr/Cyber/ComBoard_V005/Content/list.jsp?gid=1115983888724&bid=1115985252888";
    private static final String ROW_SELECTOR = "table.table-condensed tbody tr";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    public List<Post> fetchPosts() throws IOException {
        List<Post> posts = new ArrayList<>();

        Document doc = Jsoup.connect(BOARD_URL).get();
        Elements rows = doc.select(ROW_SELECTOR);

        for (Element row : rows) {
            Elements tds = row.select("td");
            if (tds.size() < 6) {
                continue;
            }
            Post post = new Post();

            // 1td: 공지 여부 (텍스트에 "공지" 포함 여부)
            String typeText = tds.get(0).text();
            post.setNotice(typeText.contains("공지"));

            // 2td: 글쓴이
            post.setAuthor(tds.get(1).text().trim());

            // 3td: 제목 및 cid 추출
            Element titleElement = tds.get(2).selectFirst("a");
            if (titleElement != null) {
                post.setTitle(titleElement.text().trim());
                String href = titleElement.attr("href");            //JavaScript:viewGo("1742185235951", 1);
                String[] parts = href.split("\"");                      // 큰따옴표(")를 기준으로 분리하여 두 번째 요소가 cid
                if (parts.length >= 2) {
                    post.setCid(parts[1]);
                }
            } else {
                post.setTitle(tds.get(2).text().trim());
            }

            // 4td: 등록일 (문자열을 날짜로 파싱)
            String dateText = tds.get(3).text().trim();
            try {
                LocalDate date = LocalDate.parse(dateText, DATE_FORMATTER);
                post.setCreatedDate(date.atStartOfDay());
            } catch (Exception e) {
                post.setCreatedDate(null);
            }

            // 5td: 조회수 (숫자로 변환)
            try {
                int viewCount = Integer.parseInt(tds.get(4).text().trim());
                post.setViewCount(viewCount);
            } catch (NumberFormatException e) {
                post.setViewCount(0);
            }

            // 6td: 파일 첨부 여부 (텍스트에 "File" 포함 여부)
            Element fileElement = tds.get(5).selectFirst("span.label");
            if (fileElement != null && fileElement.text().contains("File")) {
                post.setFile("첨부파일 있음");
            } else {
                post.setFile(null);
            }

            posts.add(post);
        }

        return posts;
    }
}
