package interface_adapters.controllers;

import use_case.fetch_news.FetchNewsInputBoundary;
import interface_adapters.presenters.FetchNewsPresenter;
import use_case.fetch_news.FetchNewsInputData;

public class NewsController {

    private final FetchNewsInputBoundary interactor; // 调用 UseCase
    private final FetchNewsPresenter presenter;           // 控制分页

    public NewsController(FetchNewsInputBoundary interactor, FetchNewsPresenter presenter) {
        this.interactor = interactor;
        this.presenter = presenter;
    }

    /** 从 API / DAO 获取新闻，调用 Interactor */
    public void fetchNews() {
        try {
            // 创建 InputData，如果有查询参数可以传入
            FetchNewsInputData inputData = new FetchNewsInputData();
            interactor.execute(inputData);
        } catch (Exception e) {
            presenter.presentError("Failed to fetch the news: " + e.getMessage());
        }
    }

    /** 上一页按钮点击 */
    public void goToPreviousPage() {
        presenter.prevPage();
    }

    /** 下一页按钮点击 */
    public void goToNextPage() {
        presenter.nextPage();
    }
}
