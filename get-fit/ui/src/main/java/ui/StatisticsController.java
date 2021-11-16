package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import client.LogClient;
import client.ServerResponseException;
import client.LogClient.ListBuilder;

/*
---------------------------------------
This component is for a future release.
---------------------------------------
 */

public class StatisticsController {

    private final ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * Labels for different statistics.
     */
    @FXML
    private Label totalDuration, speed, numberOfSessions,
    averageFeeling, averageSpeed, maximumHr, averageDuration, errorLabel;
   
    /**
     * Button to press for returning to start page.
     */
    @FXML
    private Button showData;

    /**
     * Choosing which exercise type to display.
     */
    @FXML
    private ComboBox<String> exerciseType;

    /**
     * The duration for each pole in the diagram.
     */
    @FXML
    private RadioButton weeks, months, years;
    
    /**
     * The bar chart.
     */
    @FXML
    private LineChart<String, Number> statisticsChart;

    @FXML
    private DatePicker start, end;
    /**
     * x-axis for the bar chart.
     */
    @FXML
    private CategoryAxis xAxis;

    /**
     * y-axis for the bar chart.
     */
    @FXML
    private NumberAxis yAxis;


    
    private final LogClient client = new LogClient("http://localhost", 8080);

    private ObservableList<String> exerciseTypeSelecter =
            FXCollections.observableArrayList("running", "swimming",
                    "strength", "cycling");

    @FXML
    private void initialize() {
        exerciseType.setItems(exerciseTypeSelecter);
        weeks.setToggleGroup(toggleGroup);
        months.setToggleGroup(toggleGroup);
        years.setToggleGroup(toggleGroup);

        //Default values for dates are today and one year from now.
        start.setValue(LocalDate.now().plusYears(-1));
        end.setValue(LocalDate.now());

    }

    /**
     * Switches the view to start page.
     *
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void onReturn(final ActionEvent event)
            throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);
        Stage window =
                (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Start page");
        window.setScene(s);
        window.show();
    }

    @FXML
    public void onHandleData() {
        ListBuilder listBuilder = new ListBuilder();
        if (exerciseType.getValue() != null) {
            listBuilder.category(exerciseType.getValue().toUpperCase());
        }
        listBuilder.date(start.getValue().toString() + "-" + end.getValue().toString());
        HashMap<String, String> dataEntries;

        try {
            dataEntries = this.client.getStatistics(listBuilder);
            System.out.println(dataEntries);

            if (dataEntries.containsKey("empty")) {
                if(dataEntries.get("empty").equals("True")) {
                throw new IllegalStateException(
                    "There are no entries");
                }
            }

            for (String dataEntry : dataEntries.keySet()) {
                switch (dataEntry) {

                    case "count" :
                        numberOfSessions.setText(
                            dataEntries.get(dataEntry));
                        break;
                    case "totalDuration" :
                        totalDuration.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    case "averageDuration" :
                        averageDuration.setText(
                            dataEntries.get(dataEntry));
                        break;

                    case "averageSpeed" :
                        averageSpeed.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    case "averageFeeling" :
                        averageFeeling.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    case "maximumHr" :
                        maximumHr.setText(
                            dataEntries.get(dataEntry));
                        break;

                    }
                }
            } catch (URISyntaxException | InterruptedException | ExecutionException e) {
                errorLabel.setText("Could not connect to server");
                e.printStackTrace();
            } catch (ServerResponseException e) {
                errorLabel.setText(e.getMessage());
            } catch (IllegalStateException eae) {
                errorLabel.setText("There are no sessions saved");
            }
        }

        



    //Unfinished function:
    
    private void createStackedLineChart(final String xLabel,
            final Collection<String> category) {

                dateEntries 

                yAxis.setLabel("Hours");
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                if(selectedRadioButton.getText().equals("weeks")){
                    xAxis.setLabel("weeks");
                }
                if(selectedRadioButton.getText().equals("months")){
                    xAxis.setLabel("months");
                }
                if(selectedRadioButton.getText().equals("years")){
                    xAxis.setLabel("years");
                }
                

                    
                };
                                

        /*xAxis.setCategories(FXCollections.<String>observableArrayList(
                (Arrays.asList("test1", "test2"))));
        xAxis.setLabel(xLabel);


        //Create chart when core is finished
        statisticsChart = new StackedBarChart<>(xAxis, yAxis);
        //for (LogEntry entry : App.entryManager) {
        //final XYChart.Series<String, Number> series = new XYChart.Series<>();*/
    }
}

