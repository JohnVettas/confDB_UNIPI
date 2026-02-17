package com.example.guijavasql;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class deleteController
{
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    TableView<ObservableList<String>> reservationTable;

    @FXML
    TableColumn<ObservableList<String>, String> resIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> cityCol;
    @FXML
    TableColumn<ObservableList<String>, String> prefCentCol;
    @FXML
    TableColumn<ObservableList<String>, String> StartDateCol;
    @FXML
    TableColumn<ObservableList<String>, String> endDateCol;
    @FXML
    TableColumn<ObservableList<String>, String> perticipatorsCol;
    @FXML
    TableColumn<ObservableList<String>, String> equipmentCol;
    @FXML
    TableColumn<ObservableList<String>, String> invoicecol;
    @FXML
    TableColumn<ObservableList<String>, String> customerIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> roomIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> paymentIdCol;

    String reservationId = "-1";

    public void backButtons(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() throws SQLException, IOException
    {
        showData();
    }
    public void showData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";
        String query = "SELECT r.* FROM reservation r WHERE date_and_finishing_time IS NULL";

        // Set up TableColumn cell factories (once is enough)
        resIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        cityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        prefCentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        StartDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        endDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        perticipatorsCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
        equipmentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(6)));
        invoicecol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(7)));
        customerIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(8)));
        roomIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(9)));
        paymentIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(10)));

        // Clear old data
        reservationTable.getItems().clear();

        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)
        ) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= 11; i++) {
                    row.add(rs.getString(i));
                }
                reservationTable.getItems().add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void rowSelected() {
        ObservableList<String> selectedRow = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 11) {
            reservationId = selectedRow.get(0);
        }
    }

    public void deleteRow() throws SQLException, IOException {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "confdatabase";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";

        try {

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Statement st = conn.createStatement();


        ResultSet existResult = st.executeQuery("SELECT 1 FROM reservation WHERE reservation_ID = '" + reservationId + "';");
        boolean exists = existResult.next();
        if (exists) {
            String query1 = "DELETE FROM reservation WHERE reservation_ID = " + reservationId;
            String query2 = "DELETE FROM payment_info WHERE payment_info_ID = " + reservationId;
            st.executeUpdate(query1);
            st.executeUpdate(query2);
            showData();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Something went wrong \n make sure you have selected a reservation you want to delete");
            alert.showAndWait();
        }
    }

}
