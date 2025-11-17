import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import entity.Transaction;

import org.jfree.data.category.DefaultCategoryDataset;

public class ChartDataConverter {
    
    public static DefaultCategoryDataset convertToCategoryDataset(List<Transaction> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Group by category and month, then sum amounts
        Map<String, Map<String, Double>> categoryData = transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.groupingBy(
                    Transaction::getMonthYear,
                    Collectors.summingDouble(Transaction::getAmount)
                )
            ));
        
        // Add to dataset
        for (String category : categoryData.keySet()) {
            Map<String, Double> monthlyData = categoryData.get(category);
            for (String month : monthlyData.keySet()) {
                dataset.addValue(monthlyData.get(month), category, month);
            }
        }
        
        return dataset;
    }
    
    // Alternative: Simple category totals (pie chart data)
    public static DefaultCategoryDataset convertToCategoryTotals(List<Transaction> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<String, Double> categoryTotals = transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(Transaction::getAmount)
            ));
        
        for (String category : categoryTotals.keySet()) {
            dataset.addValue(categoryTotals.get(category), "Expenses", category);
        }
        
        return dataset;
    }
}