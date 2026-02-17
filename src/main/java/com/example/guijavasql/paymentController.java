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
import java.util.ArrayList;
import java.util.Objects;

public class paymentController
{
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String paymentId = "-1";

    @FXML
    TableView<ObservableList<String>> paymentTable;

    @FXML
    TableColumn<ObservableList<String>, String> paymentIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> amountCol;
    @FXML
    TableColumn<ObservableList<String>, String> methodCol;
    @FXML
    TableColumn<ObservableList<String>, String> dateCol;
    @FXML
    TableColumn<ObservableList<String>, String> beenPayedCol;


    public void rowSelected() {
        ObservableList<String> selectedRow = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 5) {
            paymentId = selectedRow.get(0);
        }
    }

    public void backButtons(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void payButton(ActionEvent e) throws IOException, SQLException {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "confdatabase";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";

        try {

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (Exception ev) {
            ev.printStackTrace();
        }
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE payment_info SET payment_condition = 1, payment_date = NOW() WHERE payment_info_ID = " + paymentId + " AND payment_condition = 0");

        showData();
    }

    public void showData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";
        String query = "SELECT DISTINCT *\n" +
                "FROM payment_info\n" +
                "WHERE payment_info_id IN (\n" +
                "    SELECT reservation_id FROM reservation\n" +
                "    WHERE date_and_starting_time IS NOT NULL\n" +
                ");";

        // Set up TableColumn cell factories (once is enough)
        paymentIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        amountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        methodCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        beenPayedCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));


        // Clear old data
        paymentTable.getItems().clear();

        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)
        ) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= 5; i++) {
                    row.add(rs.getString(i));
                }
                paymentTable.getItems().add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void initialize() throws SQLException, IOException
    {
        showData();
    }
}
