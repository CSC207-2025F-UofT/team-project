package data.news;

import entity.News;
import use_case.fetch_news.NewsDataAccessInterface;
import com.google.gson.*;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock DAO 用于单元测试
 * 读取本地 sample JSON 文件，解析成 List<News>
 */
public class MockNewsDAO implements NewsDataAccessInterface {

    private final String filePath;

    /**
     * @param filePath 本地 sample JSON 文件路径
     */
    public MockNewsDAO(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<News> fetchNews(String query) {
        List<News> newsList = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray feed = json.getAsJsonArray("feed");

            if (feed != null) {
                for (JsonElement elem : feed) {
                    JsonObject newsObj = elem.getAsJsonObject();
                    String title = newsObj.get("title").getAsString();
                    String url = newsObj.get("url").getAsString();
                    String timeStr = newsObj.get("time_published").getAsString();

                    // 注意你的 sample JSON 时间格式是 yyyyMMdd'T'HHmmss
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
                    LocalDateTime datePublished = LocalDateTime.parse(timeStr, formatter);

                    newsList.add(new News(title, url, datePublished));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read mock news file: " + e.getMessage(), e);
        }

        return newsList;
    }
}
