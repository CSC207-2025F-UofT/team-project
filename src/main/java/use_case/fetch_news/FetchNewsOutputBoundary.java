package use_case.fetch_news;

import java.util.List;
import entity.News;

public interface FetchNewsOutputBoundary {
    /**
     * 将获取到的新闻返回给 Presenter / UI 层
     *
     * @param newsList 新闻列表
     */
    void presentNews(List<News> newsList);

    /**
     * 如果获取新闻失败，可以返回错误信息
     *
     * @param errorMessage 错误信息
     */
    void presentError(String errorMessage);
}
