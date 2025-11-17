package ui;

import java.util.List;

public class NewsViewModel {

    /** 标题列表（每页最多3个） */
    public List<String> titles;

    /** 发布时间（格式化后的字符串，比如 "2025-11-17 15:59"） */
    public List<String> publishTimes;

    /** 新闻 URL 列表，用于点击打开浏览器 */
    public List<String> urls;

    /** 当前页是否有上一页 */
    public boolean hasPrevPage;

    /** 当前页是否有下一页 */
    public boolean hasNextPage;

    public NewsViewModel(
            List<String> titles,
            List<String> publishTimes,
            List<String> urls,
            boolean hasPrevPage,
            boolean hasNextPage
    ) {
        this.titles = titles;
        this.publishTimes = publishTimes;
        this.urls = urls;
        this.hasPrevPage = hasPrevPage;
        this.hasNextPage = hasNextPage;
    }
}
