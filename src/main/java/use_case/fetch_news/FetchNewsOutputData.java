package use_case.fetch_news;

import entities.News;

import java.util.List;

/**
 * 封装获取新闻 Use Case 的输出数据
 *
 * 成功时，newsList 非空，errorMessage 为 null
 * 失败时，newsList 为空，errorMessage 有内容
 */
public class FetchNewsOutputData {

    private final List<News> newsList;
    private final String errorMessage;

    /**
     * 成功构造器
     */
    public FetchNewsOutputData(List<News> newsList) {
        this.newsList = newsList;
        this.errorMessage = null;
    }

    /**
     * 失败构造器
     */
    public FetchNewsOutputData(String errorMessage) {
        this.newsList = null;
        this.errorMessage = errorMessage;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }
}
