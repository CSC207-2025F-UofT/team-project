package use_case.fetch_news;

/**
 * 封装获取新闻 Use Case 的输入数据
 *
 * 目前不需要任何字段（默认获取最新新闻），
 * 但未来可扩展，比如增加关键词、新闻类别等。
 */
public class FetchNewsInputData {

    // 示例可扩展字段：
    // private String keyword;
    // private String category;

    public FetchNewsInputData() {
        // 现在不需要任何参数
    }

    // 如果未来需要关键词，可以加 getter/setter
    // public String getKeyword() { return keyword; }
    // public void setKeyword(String keyword) { this.keyword = keyword; }

    // 如果未来需要类别，可以加 getter/setter
    // public String getCategory() { return category; }
    // public void setCategory(String category) { this.category = category; }
}
