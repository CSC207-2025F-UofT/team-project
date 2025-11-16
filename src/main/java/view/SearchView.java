package view;

import javax.swing.*;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    I have not added or made any actionListener currently, 11/12
    It supposes to be done later
    Current constructor will only provide a view without no actual function on its components

    default continents: NA, EU
    default countries: Canada, USA
    default cities: Toronto, London
    default genre: Pop, Rock, Hip-pop
    above information should be replaced later
*/

public class SearchView extends JPanel{
    // default information
    String[] continents = {"NA", "EU"};
    String[] countries = {"Canada", "USA"};
    String[] cities = {"Toronto", "London"};
    String[] genre = {"Pop", "Rock", "Hip-pop"};

    // view name
    private final String viewName = "Search Panel";

    // search field panel
    private final JTextField searchField = new JTextField("your event name");

    // JComboBox panel
    private final JComboBox continentComboBox = new JComboBox(continents);
    private final JComboBox countryComboBox = new JComboBox(countries);
    private final JComboBox cityComboBox = new JComboBox(cities);

    // artist name panel
    private final JLabel artistLabel = new JLabel("Artist");
    private final JTextField artistField = new JTextField();

//    // price panel
//    private final JLabel priceLabel = new JLabel("Price");
//    private final JLabel priceMinLabel = new JLabel("Min:");
//    private final JTextField priceMinField = new JTextField();
//    private final JLabel priceMaxLabel = new JLabel("Max:");
//    private final JTextField priceMaxField = new JTextField();

    //genre panel
    private final JLabel genreLabel = new JLabel("Genre");
    private final JComboBox genreField = new JComboBox(genre);

    // data picker panel
    private final JLabel dateLabel = new JLabel("Date");
//    private final UtilCalendarModel model = new UtilCalendarModel();
    private final JDatePickerImpl startDatePicker = generateDataPicker();
    private final JDatePickerImpl endDataPicker = generateDataPicker();

    // find button field
    private final JButton findButton = new JButton("Find");

    // system information field
    private final JLabel systemInfoLabel = new JLabel();

    public SearchView() {
        JFrame frame = new JFrame(viewName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // set general layout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // row 0: search JTextField
        // layout parameter
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;

        // item panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.add(searchField);
        frame.add(searchField, gbc);

        // row 1
        // layour parameter
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        // panel
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.X_AXIS));
        comboBoxPanel.add(continentComboBox);
        comboBoxPanel.add(countryComboBox);
        comboBoxPanel.add(cityComboBox);
        frame.add(comboBoxPanel, gbc);

        // row 3
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        JPanel artistNamePanel = new JPanel();
        artistNamePanel.setLayout(new BoxLayout(artistNamePanel, BoxLayout.X_AXIS));
        artistNamePanel.add(artistLabel);
        artistNamePanel.add(artistField);
        frame.add(artistNamePanel, gbc);

//        JPanel pricePanel = new JPanel();
//        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.X_AXIS));
//        pricePanel.add(priceLabel);
//        pricePanel.add(priceMinLabel);
//        pricePanel.add(priceMinField);
//        pricePanel.add(priceMaxLabel);
//        pricePanel.add(priceMaxField);

        // row 4
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.X_AXIS));
        genrePanel.add(genreLabel);
        genrePanel.add(genreField);
        frame.add(genrePanel, gbc);

        // row 5
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        JPanel datePickerPanel = new JPanel();
        datePickerPanel.setLayout(new BoxLayout(datePickerPanel, BoxLayout.X_AXIS));
        datePickerPanel.add(dateLabel);
        datePickerPanel.add(startDatePicker);
        datePickerPanel.add(endDataPicker);
        frame.add(datePickerPanel, gbc);

        // row 6
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        gbc.gridwidth = 2;
        JPanel findPanel = new JPanel();
        findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.X_AXIS));
        findPanel.add(findButton);
        frame.add(findPanel, gbc);

        // row 7
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JPanel systemInfoPanel = new JPanel();
        systemInfoPanel.setLayout(new BoxLayout(systemInfoPanel, BoxLayout.X_AXIS));
        systemInfoPanel.add(systemInfoLabel);
        frame.add(systemInfoPanel, gbc);

//        mainPanel.add(searchPanel);
//        mainPanel.add(comboBoxPanel);
//        mainPanel.add(artistNamePanel);
////        mainPanel.add(pricePanel);
//        mainPanel.add(genrePanel);
//        mainPanel.add(datePickerPanel);
//        mainPanel.add(findPanel);
//        mainPanel.add(systemInfoPanel);
//
//        frame.add(mainPanel);
        frame.setVisible(true);
    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    private JDatePickerImpl generateDataPicker(){
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    public static void main(String[] args){
        SearchView view = new SearchView();
    }
}
