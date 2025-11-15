package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import data.AlphaVantageAPI;
import controllers.StockSearchController;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** Stock search and chart view with three states: empty, search results, selected stock */
public class StocksView extends BorderPane {

    private final TextField search = new TextField();
    private final ListView<AlphaVantageAPI.StockSearchResult> suggestions = new ListView<>();

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

    private final StockSearchController searchController;
    private final AlphaVantageAPI api;   // reuse for quote & chart calls

    public StocksView(StockSearchController searchController, AlphaVantageAPI api) {
        this.searchController = searchController;
        this.api = api;
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
        suggestions.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(AlphaVantageAPI.StockSearchResult item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label name = new Label(item.getName());
                    name.setFont(Font.font(null, FontWeight.BOLD, 14));
                    Label symbol = new Label(item.getSymbol() + " • " + item.getExchange());
                    symbol.setStyle("-fx-text-fill:#667085; -fx-font-size:12px;");
                    VBox box = new VBox(4, name, symbol);
                    box.setPadding(new Insets(8, 12, 8, 12));
                    setGraphic(box);
                }
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
                    fetchAndRenderSeries(currentSymbol, range);
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
                suggestions.getItems().clear();
                suggestions.setVisible(false);
                return;
            }
            final String query = nv.trim();
            CompletableFuture
                .supplyAsync(() -> searchController.search(query))
                .thenAccept(result -> Platform.runLater(() -> {
                    if (!search.getText().trim().equals(query)) {
                        return;
                    }
                    if (!result.isSuccess()) {
                        suggestions.getItems().clear();
                        suggestions.setPlaceholder(new Label(result.getMessage()));
                        suggestions.setVisible(true);
                        return;
                    }
                    suggestions.getItems().setAll(result.getResults());
                    suggestions.setVisible(true);
                }))
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    Platform.runLater(() -> showError("Search failed: " + cause.getMessage()));
                    return null;
                });
        });

        suggestions.setOnMouseClicked(e -> {
            AlphaVantageAPI.StockSearchResult sel = suggestions.getSelectionModel().getSelectedItem();
            selectSuggestion(sel);
        });

        search.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !suggestions.getItems().isEmpty()) {
                selectSuggestion(suggestions.getItems().get(0));
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                suggestions.setVisible(false);
            }
        });

        showEmpty();
    }

    private void selectSuggestion(AlphaVantageAPI.StockSearchResult s) {
        if (s == null) return;
        search.setText(s.getSymbol());
        suggestions.getItems().clear();
        suggestions.setVisible(false);
        loadQuote(s);
    }

    private void showEmpty(){
        currentSymbol = "";
        companyName.setText("Search for a stock");
        symbolLabel.setText("");
        priceLabel.setText("");
        changeLabel.setText("");
        chart.getData().clear();
    }

    private void loadQuote(AlphaVantageAPI.StockSearchResult result) {
        CompletableFuture
            .supplyAsync(() -> {
                try {
                    return api.getQuote(result.getSymbol());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .thenAccept(quote -> Platform.runLater(() -> {
                companyName.setText(result.getName());
                symbolLabel.setText(result.getSymbol() + " • " + result.getExchange());
                priceLabel.setText(String.format("$%.2f", quote.getPrice()));
                double change = quote.getChange();
                double changePct = quote.getChangePercent();
                changeLabel.setText(String.format("%+.2f (%+.2f%%)", change, changePct));
                changeLabel.setStyle("-fx-text-fill:" + (change >= 0 ? "#22c55e" : "#ef4444"));
                currentSymbol = result.getSymbol();
                Toggle selected = rangeGroup.getSelectedToggle();
                String range = selected instanceof ToggleButton ? ((ToggleButton) selected).getText() : "1D";
                fetchAndRenderSeries(currentSymbol, range);
            }))
            .exceptionally(ex -> {
                Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                Platform.runLater(() -> showError("Failed to load quote: " + cause.getMessage()));
                return null;
            });
    }

    private void fetchAndRenderSeries(String symbol, String range) {
        chart.getData().clear();
        CompletableFuture
            .supplyAsync(() -> {
                try {
                    return api.getTimeSeries(symbol, range);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .thenAccept(seriesData -> Platform.runLater(() -> {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                for (int i = 0; i < seriesData.size(); i++) {
                    series.getData().add(new XYChart.Data<>(i, seriesData.get(i).getPrice()));
                }
                chart.getData().add(series);
                if ("1D".equals(range)) {
                    xAxis.setLabel("Time (Market Hours)");
                } else {
                    xAxis.setLabel("Time");
                }
            }))
            .exceptionally(ex -> {
                Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                Platform.runLater(() -> showError("Failed to load chart: " + cause.getMessage()));
                return null;
            });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
