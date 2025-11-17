package use_case.fetch_news;

public interface FetchNewsInputBoundary {
    /**
     * 执行获取新闻的 Use Case
     *
     * @param inputData 封装用户输入信息，比如关键词或其他参数
     */
    void execute(FetchNewsInputData inputData);
}
