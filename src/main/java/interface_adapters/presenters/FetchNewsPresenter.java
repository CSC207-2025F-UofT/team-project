package interface_adapters.presenters;

import entity.News;
import ui.NewsView;
import ui.NewsViewModel;
import use_case.fetch_news.FetchNewsOutputBoundary;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FetchNewsPresenter implements FetchNewsOutputBoundary {

    private final NewsView view;

    /** 保存全部新闻，翻页时用 */
    private List<News> allNews = new ArrayList<>();

    /** 当前页 index，从 0 开始 */
    private int currentPage = 0;

    /** 时间格式化 */
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FetchNewsPresenter(NewsView view) {
        this.view = view;
    }

    // --------------------------
    //   必须实现的接口方法
    // --------------------------

    @Override
    public void presentNews(List<News> newsList) {
        this.allNews = newsList;
        this.currentPage = 0; // 新数据从第一页开始
        updateViewModelAndRender();
    }

    @Override
    public void presentError(String errorMessage) {
        view.showError(errorMessage);
    }

    // --------------------------
    //     Presenter 额外方法：上一页 / 下一页
    // --------------------------

    public void nextPage() {
        if ((currentPage + 1) * 3 < allNews.size()) {
            currentPage++;
            updateViewModelAndRender();
        }
    }

    public void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            updateViewModelAndRender();
        }
    }

    // --------------------------
    //     构建 ViewModel 并更新 View
    // --------------------------

    private void updateViewModelAndRender() {
        int start = currentPage * 3;
        int end = Math.min(start + 3, allNews.size());

        List<String> titles = new ArrayList<>();
        List<String> publishTimes = new ArrayList<>();
        List<String> urls = new ArrayList<>();

        // 构建 3 条新闻
        for (int i = start; i < end; i++) {
            News n = allNews.get(i);
            titles.add(n.getTitle());
            publishTimes.add(n.getTimePublished().format(formatter));
            urls.add(n.getUrl());
        }

        // 不足 3 条补空
        while (titles.size() < 3) {
            titles.add("");
            publishTimes.add("");
            urls.add("");
        }

        boolean hasPrev = currentPage > 0;
        boolean hasNext = end < allNews.size();

        NewsViewModel vm = new NewsViewModel(
                titles, publishTimes, urls, hasPrev, hasNext
        );

        view.updateView(vm);
    }
}
