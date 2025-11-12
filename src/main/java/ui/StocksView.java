package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

/** Step 0: Pure JavaFX UI with mock data — search → suggestions → header → ranges → chart. */
public class StocksView extends BorderPane {

    private final TextField search = new TextField();
    private final ListView<Suggestion> suggestions = new ListView<>();

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

    private static final List<Suggestion> MOCK = List.of(
            new Suggestion("AAPL","Apple Inc.","NASDAQ"),
            new Suggestion("TSLA","Tesla, Inc.","NASDAQ"),
            new Suggestion("MSFT","Microsoft Corporation","NASDAQ"),
            new Suggestion("GOOGL","Alphabet Inc.","NASDAQ"),
            new Suggestion("NVDA","NVIDIA Corporation","NASDAQ"),
            new Suggestion("AMZN","Amazon.com, Inc.","NASDAQ")
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
        suggestions.setMaxHeight(200);
        suggestions.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Suggestion s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) setText(null);
                else setText(s.name + "\n" + s.symbol + " • " + s.exchange);
                setPadding(new Insets(8,12,8,12));
            }
        });

        searchCard.getChildren().addAll(search, suggestions);

        // Details card
        var detailsCard = new VBox(6);
        detailsCard.setPadding(new Insets(16));
        detailsCard.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

        companyName.setFont(Font.font(18));
        symbolLabel.setStyle("-fx-text-fill:#667085;");
        priceLabel.setFont(Font.font(18));
        changeLabel.setStyle("-fx-text-fill:#22c55e;");

        var headerLeft = new VBox(4, companyName, new HBox(10, symbolLabel));
        var headerRight = new HBox(8, refreshBtn, watchBtn);
        headerRight.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(headerRight, Priority.ALWAYS);

        var detailsHeader = new HBox(16, headerLeft, new Region(), priceLabel, changeLabel, headerRight);
        detailsHeader.setAlignment(Pos.CENTER_LEFT);
        detailsCard.getChildren().add(detailsHeader);

        // Range bar
        for (String r : List.of("1D","1W","1M","3M","1Y","5Y")) {
            ToggleButton b = new ToggleButton(r);
            b.setToggleGroup(rangeGroup);
            b.setOnAction(e -> {
                if (!symbolLabel.getText().isBlank()) renderMockSeries(symbolLabel.getText(), r);
            });
            rangeBar.getChildren().add(b);
        }
        ((ToggleButton)rangeBar.getChildren().get(0)).setSelected(true);

        // Chart card
        var chartCard = new VBox(10);
        chartCard.setPadding(new Insets(16));
        chartCard.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

        xAxis.setForceZeroInRange(false);
        yAxis.setForceZeroInRange(false);
        chart.setLegendVisible(false);
        chart.setCreateSymbols(false);
        chart.setMinHeight(320);

        // Hover tooltip
        final Tooltip tt = new Tooltip();
        tt.setShowDelay(Duration.millis(0));
        chart.setOnMouseMoved(ev -> Tooltip.install(chart, tt));

        chartCard.getChildren().addAll(new Label("Price Chart"), new HBox(rangeBar), chart);

        setCenter(new VBox(14, searchCard, detailsCard, chartCard));

        // Interactions (mock)
        search.textProperty().addListener((o, ov, nv) -> {
            if (nv == null || nv.isBlank()) {
                suggestions.setVisible(false);
                suggestions.getItems().clear();
                return;
            }
            var list = MOCK.stream()
                    .filter(s -> s.symbol.toLowerCase().contains(nv.toLowerCase())
                            || s.name.toLowerCase().contains(nv.toLowerCase()))
                    .limit(6).collect(Collectors.toList());
            suggestions.getItems().setAll(list);
            suggestions.setVisible(true);
        });

        suggestions.setOnMouseClicked(e -> {
            var sel = suggestions.getSelectionModel().getSelectedItem();
            if (sel != null) choose(sel);
        });

        search.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !suggestions.getItems().isEmpty()) {
                choose(suggestions.getItems().get(0));
            }
        });

        showEmpty();
    }

    private void choose(Suggestion s) {
        search.setText(s.symbol);
        suggestions.setVisible(false);
        suggestions.getItems().clear();

        companyName.setText(s.name);
        symbolLabel.setText(s.symbol + " • " + s.exchange);

        double p = 100 + new Random().nextDouble() * 200;
        double chg = (new Random().nextDouble() - 0.5) * 4.0;
        priceLabel.setText(String.format("$%.2f", p));
        changeLabel.setText(String.format("%+.2f%%", chg));
        changeLabel.setStyle("-fx-text-fill:" + (chg >= 0 ? "#22c55e" : "#ef4444"));

        ToggleButton tb = (ToggleButton) rangeGroup.getSelectedToggle();
        renderMockSeries(s.symbol, tb == null ? "1D" : tb.getText());
    }

    private void renderMockSeries(String symbol, String range){
        chart.getData().clear();
        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        int n = switch (range) {
            case "1D" -> 70;
            case "1W" -> 50;
            case "1M" -> 30;
            case "3M" -> 40;
            case "1Y" -> 52;
            default -> 60;
        };
        double base = 120 + Math.abs(symbol.hashCode() % 40);
        double val = base;
        Random r = new Random(symbol.hashCode() + range.hashCode());
        for (int i=0;i<n;i++){
            val += (r.nextDouble()-0.5) * (range.equals("1D")?0.6:2.0);
            series.getData().add(new XYChart.Data<>(i, Math.max(1, val)));
        }
        chart.getData().add(series);
    }

    private void showEmpty(){
        companyName.setText("Search for a stock");
        symbolLabel.setText("");
        priceLabel.setText("");
        changeLabel.setText("");
        chart.getData().clear();
    }

    private static final class Suggestion {
        final String symbol;
        final String name;
        final String exchange;
        Suggestion(String symbol, String name, String exchange) {
            this.symbol = symbol;
            this.name = name;
            this.exchange = exchange;
        }
    }

}
