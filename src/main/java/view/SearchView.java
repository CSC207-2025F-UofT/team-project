package view;

import javax.swing.*;
import org.jdatepicker.impl.*;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchView {
    // view name
    private final String viewName = "Search Panel";

    // search field panel
    private final JTextField searchField = new JTextField("your event name");

    // JComboBox panel
    private final JComboBox continentComboBox = new JComboBox();
    private final JComboBox countryComboBox = new JComboBox();
    private final JComboBox cityComboBox = new JComboBox();

    // artist name panel
    private final JLabel artistLabel = new JLabel("Artist");
    private final JTextField artistField = new JTextField();

    // price panel
    private final JLabel priceLabel = new JLabel("Price");
    private final JLabel priceMinLabel = new JLabel("Min:");
    private final JTextField priceMinField = new JTextField();
    private final JLabel priceMaxLabel = new JLabel("Max:");
    private final JTextField priceMaxField = new JTextField();

    //genre panel
    private final JLabel genreLabel = new JLabel("Genre");
    private final JTextField genreField = new JTextField();

    // data picker panel
    private final JLabel dateLabel = new JLabel("Date");
    private final UtilCalendarModel model = new UtilCalendarModel();

    // find button field
    private final JButton findButton = new JButton("Find");

    // system information field
    private final JLabel systemInfoLabel = new JLabel();


}
