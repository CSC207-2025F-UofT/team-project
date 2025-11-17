package use_case.fetch_news;

import data.news.NewsApiDAO;
import entities.News;
import java.util.List;

public class FetchNewsInteractor implements FetchNewsInputBoundary {

    private final NewsDataAccessInterface newsDao; // 可以改成 NewsDataAccessInterface
    private final FetchNewsOutputBoundary presenter;

    public FetchNewsInteractor(NewsDataAccessInterface newsDao, FetchNewsOutputBoundary presenter) {
        this.newsDao = newsDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(FetchNewsInputData inputData) {
        try {
            // 调用 DAO 获取新闻
            List<News> newsList = newsDao.fetchNews(null);

            // 封装输出数据
            FetchNewsOutputData outputData = new FetchNewsOutputData(newsList);

            // 通过 Output Boundary 返回结果
            presenter.presentNews(outputData.getNewsList());

        } catch (NewsApiDAO.RateLimitExceededException e) {
            // DAO 限流异常
            FetchNewsOutputData outputData = new FetchNewsOutputData(e.getMessage());
            presenter.presentError(outputData.getErrorMessage());
        } catch (Exception e) {
            // 其他异常
            FetchNewsOutputData outputData = new FetchNewsOutputData("Failed to fetch news: " + e.getMessage());
            presenter.presentError(outputData.getErrorMessage());
        }
    }
}
