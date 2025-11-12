package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;
import java.util.stream.Collectors;

/** Stock search and chart view with three states: empty, search results, selected stock */
public class StocksView extends BorderPane {

    private final TextField search = new TextField();
    private final ListView<StockSuggestion> suggestions = new ListView<>();

    private final Label companyName = new Label("Search for a stock");
    private final Label symbolLabel = new Label("");
    private final Label priceLabel = new Label("");
    private final Label changeLabel = new Label("");
    private final Button refreshBtn = new Button("⟳");
    private final Button watchBtn = new Button("♡");

    private final ToggleGroup rangeGroup = new ToggleGroup();
    private final HBox rangeBar = new HBox(8);

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Number,Number> chart = new LineChart<>(xAxis, yAxis);

    // Track current selected symbol
    private String currentSymbol = "";

    // Mock stock data with prices and changes
    private static final List<StockSuggestion> MOCK_STOCKS = List.of(
            new StockSuggestion("AAPL", "Apple Inc.", "NASDAQ", 178.25, 2.45, 1.39),
            new StockSuggestion("TSLA", "Tesla, Inc.", "NASDAQ", 242.84, 5.20, 2.19),
            new StockSuggestion("MSFT", "Microsoft Corporation", "NASDAQ", 378.91, -1.33, -0.35),
            new StockSuggestion("GOOGL", "Alphabet Inc.", "NASDAQ", 141.80, 0.95, 0.67),
            new StockSuggestion("NVDA", "NVIDIA Corporation", "NASDAQ", 485.20, 12.50, 2.64),
            new StockSuggestion("AMZN", "Amazon.com, Inc.", "NASDAQ", 152.30, -0.85, -0.55)
    );

    public StocksView() {
        setPadding(new Insets(16));
        setStyle("-fx-background-color:#f7f8fb;");

        // Search card
        var searchCard = new VBox(10);
        searchCard.setPadding(new Insets(16));
        searchCard.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

        search.setPromptText("Search stocks (e.g., AAPL, Tesla)...");
        search.setPrefHeight(40);

        suggestions.setVisible(false);
        suggestions.setMaxHeight(300);
        suggestions.setPrefHeight(USE_COMPUTED_SIZE);
        suggestions.setCellFactory(v -> new ListCell<>() {
            @Override 
            protected void updateItem(StockSuggestion s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create custom cell with name, symbol, price, and change
                    VBox vbox = new VBox(4);
                    vbox.setPadding(new Insets(8, 12, 8, 12));
                    
                    Label nameLabel = new Label(s.name);
                    nameLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
                    
                    HBox infoBox = new HBox(8);
                    Label symLabel = new Label(s.symbol + " • " + s.exchange);
                    symLabel.setStyle("-fx-text-fill:#667085; -fx-font-size: 12px;");
                    
                    HBox priceBox = new HBox(8);
                    priceBox.setAlignment(Pos.CENTER_RIGHT);
                    Label priceLbl = new Label(String.format("$%.2f", s.price));
                    priceLbl.setFont(Font.font(null, FontWeight.BOLD, 14));
                    
                    String changeText = String.format("%+.2f%%", s.changePercent);
                    Label changeLbl = new Label(changeText);
                    changeLbl.setStyle(String.format("-fx-text-fill:%s; -fx-font-size: 12px;", 
                        s.changePercent >= 0 ? "#22c55e" : "#ef4444"));
                    
                    priceBox.getChildren().addAll(priceLbl, changeLbl);
                    HBox.setHgrow(priceBox, Priority.ALWAYS);
                    
                    infoBox.getChildren().addAll(symLabel, new Region(), priceBox);
                    HBox.setHgrow(infoBox, Priority.ALWAYS);
                    
                    vbox.getChildren().addAll(nameLabel, infoBox);
                    setGraphic(vbox);
                }
                setPadding(Insets.EMPTY);
            }
        });

        searchCard.getChildren().addAll(search, suggestions);

        // Details card
        var detailsCard = new VBox(6);
        detailsCard.setPadding(new Insets(16));
        detailsCard.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

        companyName.setFont(Font.font(null, FontWeight.BOLD, 20));
        symbolLabel.setStyle("-fx-text-fill:#667085; -fx-font-size: 14px;");
        priceLabel.setFont(Font.font(null, FontWeight.BOLD, 24));
        changeLabel.setFont(Font.font(16));

        var headerLeft = new VBox(4, companyName, new HBox(10, symbolLabel));
        var headerRight = new HBox(8, refreshBtn, watchBtn);
        headerRight.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(headerRight, Priority.ALWAYS);

        var detailsHeader = new HBox(16, headerLeft, new Region(), priceLabel, changeLabel, headerRight);
        detailsHeader.setAlignment(Pos.CENTER_LEFT);
        detailsCard.getChildren().add(detailsHeader);

        // Range bar
        rangeBar.setAlignment(Pos.CENTER_RIGHT);
        for (String r : List.of("1D","1W","1M","3M","1Y","5Y")) {
            ToggleButton b = new ToggleButton(r);
            b.setToggleGroup(rangeGroup);
            b.setStyle("-fx-background-radius: 4; -fx-padding: 6 12;");
            final String range = r;
            b.setOnAction(e -> {
                if (!currentSymbol.isBlank()) {
                    renderMockSeries(currentSymbol, range);
                }
            });
            rangeBar.getChildren().add(b);
        }
        ToggleButton firstBtn = (ToggleButton)rangeBar.getChildren().get(0);
        firstBtn.setSelected(true);
        firstBtn.setStyle("-fx-background-radius: 4; -fx-padding: 6 12; -fx-background-color: #3b82f6; -fx-text-fill: white;");
        
        // Update toggle button styles when selection changes
        rangeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) {
                ((ToggleButton)oldVal).setStyle("-fx-background-radius: 4; -fx-padding: 6 12;");
            }
            if (newVal != null) {
                ((ToggleButton)newVal).setStyle("-fx-background-radius: 4; -fx-padding: 6 12; -fx-background-color: #3b82f6; -fx-text-fill: white;");
            }
        });

        // Chart card
        var chartCard = new VBox(10);
        chartCard.setPadding(new Insets(16));
        chartCard.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

        Label chartTitle = new Label("Price Chart");
        chartTitle.setFont(Font.font(null, FontWeight.BOLD, 16));
        
        HBox chartHeader = new HBox(16, chartTitle, new Region(), rangeBar);
        chartHeader.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(chartHeader, Priority.ALWAYS);

        xAxis.setForceZeroInRange(false);
        yAxis.setForceZeroInRange(false);
        xAxis.setLabel("Time");
        yAxis.setLabel("Price ($)");
        chart.setLegendVisible(false);
        chart.setCreateSymbols(false);
        chart.setMinHeight(320);
        chart.setAnimated(false);
        chart.setStyle("-fx-background-color: transparent;");

        chartCard.getChildren().addAll(chartHeader, chart);

        setCenter(new VBox(14, searchCard, detailsCard, chartCard));

        // Interactions
        search.textProperty().addListener((o, ov, nv) -> {
            if (nv == null || nv.isBlank()) {
                suggestions.setVisible(false);
                suggestions.getItems().clear();
                return;
            }
            var list = MOCK_STOCKS.stream()
                    .filter(s -> s.symbol.toLowerCase().contains(nv.toLowerCase())
                            || s.name.toLowerCase().contains(nv.toLowerCase()))
                    .limit(6)
                    .collect(Collectors.toList());
            
            if (list.isEmpty()) {
                suggestions.setVisible(false);
            } else {
                suggestions.getItems().setAll(list);
                suggestions.setVisible(true);
            }
        });

        suggestions.setOnMouseClicked(e -> {
            var sel = suggestions.getSelectionModel().getSelectedItem();
            if (sel != null) choose(sel);
        });

        search.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !suggestions.getItems().isEmpty()) {
                choose(suggestions.getItems().get(0));
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                suggestions.setVisible(false);
            }
        });

        showEmpty();
    }

    private void choose(StockSuggestion s) {
        search.setText(s.symbol);
        suggestions.setVisible(false);
        suggestions.getItems().clear();

        // Update current symbol
        currentSymbol = s.symbol;

        companyName.setText(s.name);
        symbolLabel.setText(s.symbol + " • " + s.exchange);
        
        // Display price
        priceLabel.setText(String.format("$%.2f", s.price));
        
        // Display change: dollar amount and percentage (e.g., "+$2.45 (+1.39%)")
        double changeDollar = s.price * (s.changePercent / 100.0);
        String changeText;
        if (s.changePercent >= 0) {
            changeText = String.format("+$%.2f (+%.2f%%)", changeDollar, s.changePercent);
        } else {
            changeText = String.format("$%.2f (%.2f%%)", changeDollar, s.changePercent);
        }
        changeLabel.setText(changeText);
        changeLabel.setStyle(String.format("-fx-text-fill:%s;", 
            s.changePercent >= 0 ? "#22c55e" : "#ef4444"));

        // Render chart with selected stock data
        ToggleButton tb = (ToggleButton) rangeGroup.getSelectedToggle();
        renderMockSeries(s.symbol, tb == null ? "1D" : tb.getText());
    }

    private void renderMockSeries(String symbol, String range){
        chart.getData().clear();
        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        
        int n;
        switch (range) {
            case "1D":
                n = 70;
                break;
            case "1W":
                n = 50;
                break;
            case "1M":
                n = 30;
                break;
            case "3M":
                n = 40;
                break;
            case "1Y":
                n = 52;
                break;
            case "5Y":
                n = 60;
                break;
            default:
                n = 60;
                break;
        }
        
        // Get base price from mock data for this symbol
        double basePrice = MOCK_STOCKS.stream()
            .filter(s -> s.symbol.equals(symbol))
            .findFirst()
            .map(s -> s.price)
            .orElse(150.0);
        
        double val = basePrice;
        Random r = new Random(symbol.hashCode() + range.hashCode());
        
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            val += (r.nextDouble() - 0.5) * (range.equals("1D") ? 2.0 : 5.0);
            values.add(Math.max(1, val));
        }
        
        for (int i = 0; i < n; i++) {
            series.getData().add(new XYChart.Data<>(i, values.get(i)));
        }
        
        chart.getData().add(series);
        
        // Update axis labels based on range
        if (range.equals("1D")) {
            xAxis.setLabel("Time (Market Hours)");
        } else {
            xAxis.setLabel("Time");
        }
    }

    private void showEmpty(){
        currentSymbol = "";
        symbolLabel.setText("");
        priceLabel.setText("");
        changeLabel.setText("");
        chart.getData().clear();
    }

    // Inner class for stock suggestions with price and change data
    private static final class StockSuggestion {
        final String symbol;
        final String name;
        final String exchange;
        final double price;
        final double changePercent;
        
        StockSuggestion(String symbol, String name, String exchange, 
                       double price, double changeDollar, double changePercent) {
            this.symbol = symbol;
            this.name = name;
            this.exchange = exchange;
            this.price = price;
            this.changePercent = changePercent;
        }
    }
}
