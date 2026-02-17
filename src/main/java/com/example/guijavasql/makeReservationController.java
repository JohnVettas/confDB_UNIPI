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

public class makeReservationController
{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TableView<ObservableList<String>> roomTable;

    @FXML
    TableColumn<ObservableList<String>, String> roomIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> nameCol;
    @FXML
    TableColumn<ObservableList<String>, String> roomCapCol;
    @FXML
    TableColumn<ObservableList<String>, String> roomSeatTypeCol;
    @FXML
    TableColumn<ObservableList<String>, String> roomEquipCol;
    @FXML
    TableColumn<ObservableList<String>, String> roomWifiCol;
    @FXML
    TableColumn<ObservableList<String>, String> roomPPHcol;
    @FXML
    TableColumn<ObservableList<String>, String> roomAvailabilityCol;

    @FXML
    private ChoiceBox<String> centerIDChoice;
    @FXML
    private TextField perticipantsField;
    @FXML
    private DatePicker startingDatePicker;
    @FXML
    private DatePicker finishingDatePicker;
    @FXML
    private ChoiceBox<String> startingTimeChoice;
    @FXML
    private ChoiceBox<String> finifhingTimeChoice;
    @FXML
    private TextField equipmentField;
    @FXML
    private ChoiceBox<String> cityBox;
    @FXML
    private CheckBox invoiceBox;
    @FXML
    private CheckBox payNowBox;
    @FXML
    private ChoiceBox<String> paymentMethodChoice;
    @FXML
    private ChoiceBox<String> customerIDChoice;

    String reservationId = "-1";
    String roomId = "-1";
    String perticipants;
    String equipment;
    String centerID;
    String city;
    String dateAndTime_s;
    String dateAndTime_e;

    public void backButtons(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void citySelected(ActionEvent e) throws IOException, SQLException
    {
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";

        String city = cityBox.getValue();

        String query2 = "SELECT center_ID FROM conf_center WHERE city = '" + city + "'";
        ArrayList<String> nameList = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query2)
        ) {
            while (rs.next()) {
                nameList.add(rs.getString("center_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] idCenterList = nameList.toArray(new String[0]);
        centerIDChoice.getItems().clear();
        centerIDChoice.getItems().addAll(idCenterList);
    }

    @FXML
    public void initialize() throws SQLException, IOException
    {
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";

        cityBox.setOnAction(e -> {
            try {
                citySelected(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace(); // or handle however you'd like
            }
        });


        String query2 = "SELECT center_ID FROM conf_center";
        ArrayList<String> nameList = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query2)
        ) {
            while (rs.next()) {
                nameList.add(rs.getString("center_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] idCenterList = nameList.toArray(new String[0]);
        centerIDChoice.getItems().clear();
        centerIDChoice.getItems().addAll(idCenterList);


        String query5 = "SELECT DISTINCT city FROM conf_center";
        ArrayList<String> nameList4 = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query5)
        ) {
            while (rs.next()) {
                nameList4.add(rs.getString("city"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] cityList = nameList4.toArray(new String[0]);
        cityBox.getItems().clear();
        cityBox.getItems().addAll(cityList);


        String[] times = {
                "00:00:00", "00:30:00", "01:00:00", "01:30:00", "02:00:00", "02:30:00",
                "03:00:00", "03:30:00", "04:00:00", "04:30:00", "05:00:00", "05:30:00",
                "06:00:00", "06:30:00", "07:00:00", "07:30:00", "08:00:00", "08:30:00",
                "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00",
                "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00",
                "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00",
                "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00", "20:30:00",
                "21:00:00", "21:30:00", "22:00:00", "22:30:00", "23:00:00", "23:30:00"
        };
        startingTimeChoice.getItems().clear();
        startingTimeChoice.getItems().addAll(times);
        finifhingTimeChoice.getItems().clear();
        finifhingTimeChoice.getItems().addAll(times);

        String query4 = "SELECT customer_ID FROM customer";
        ArrayList<String> nameList3 = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query4)
        ) {
            while (rs.next()) {
                nameList3.add(rs.getString("customer_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] idcustomerList = nameList3.toArray(new String[0]);
        customerIDChoice.getItems().clear();
        customerIDChoice.getItems().addAll(idcustomerList);

        String[] method = {"Cash", "Card"};
        paymentMethodChoice.getItems().clear();
        paymentMethodChoice.getItems().addAll(method);
    }

    public void searchData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";



        try {



            boolean wegood = true;
            perticipants = perticipantsField.getText();
            if (perticipants == null)
            {
                perticipants = "NULL";
            }
            else if (perticipants.isBlank()) {
                perticipants = "NULL";
            }
            String startingTime = startingTimeChoice.getValue();
            String finishingTime = finifhingTimeChoice.getValue();
            equipment = equipmentField.getText();
            if (equipment == null) equipment = "NULL";
            else if (equipment.isBlank()) equipment = "NULL";
            else equipment = "'" + equipment + "'";
            centerID = centerIDChoice.getValue();
            city = cityBox.getValue();

            LocalDate startingDate = startingDatePicker.getValue();
            LocalDate finishingDate = finishingDatePicker.getValue();

            dateAndTime_s = startingDate.toString() + " " + startingTime;
            dateAndTime_e = finishingDate.toString() + " " + finishingTime;


            // Define the format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

            // Parse the strings
            LocalDateTime dateTime1 = LocalDateTime.parse(dateAndTime_s, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(dateAndTime_e, formatter);


            if (dateTime1.isAfter(dateTime2) || !dateTime1.isAfter(startOfToday)) {
                wegood = false;
            }


            if (!wegood || city.isBlank() || startingTime.isBlank() || finishingTime.isBlank() || centerID.isBlank()) {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect \n Make sure you have entered an ending date after the starting date \n and that none of the mandatory fields are empty");
                alert.showAndWait();
            }


            if (wegood) {

                String query = "SELECT DISTINCT cr.* FROM reservation r JOIN conf_room cr ON r.Conf_Room_room_ID = cr.room_ID JOIN conf_center cc ON cr.Conf_Center_center_ID = cc.center_ID "
                        + " WHERE (r.date_and_starting_time > '" + dateAndTime_s + "' OR r.date_and_finishing_time < '" + dateAndTime_e + "') "
                        + " AND cc.city = '" + city + "' AND (" + centerID + " IS NULL OR cr.Conf_Center_center_ID = " + centerID + ") "
                        + " AND (" + perticipants + " IS NULL OR cr.max_capacity >= " + perticipants + ")"
                        + " AND (" + equipment + " IS NULL or cr.equipment = " + equipment + ") ";

                // Set up TableColumn cell factories (once is enough)
                roomIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
                centerIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
                nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
                roomCapCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
                roomSeatTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
                roomEquipCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
                roomWifiCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(6)));
                roomPPHcol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(7)));
                roomAvailabilityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(8)));

                // Clear old data
                roomTable.getItems().clear();

                try (
                        Connection conn = DriverManager.getConnection(url, userName, password);
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery(query)
                ) {
                    while (rs.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= 9; i++) {
                            row.add(rs.getString(i));
                        }
                        roomTable.getItems().add(row);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The data you have entered is incorrect");
            alert.showAndWait();
        }



    }

    public void makeReservation() throws SQLException, IOException {
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
        try {

            boolean wegood = true;
            String customerID = customerIDChoice.getValue();
            String paymentMethod = paymentMethodChoice.getValue();
            String paid, invoice, paymentDate;
            if (payNowBox.isSelected()) paid = "1";
            else paid = "0";
            if (invoiceBox.isSelected()) invoice = "1";
            else invoice = "0";
            if (paid.equals("0")) paymentDate = "NULL";
            else paymentDate = "NOW()";

            if (paymentMethod.isBlank() || customerID.isBlank()) {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect \n Make sure you have entered an ending date after the starting date \n and that none of the mandatory fields are empty");
                alert.showAndWait();
            }

            if (wegood) {
                ResultSet countResult = st.executeQuery("SELECT COUNT(*) FROM reservation");
                if (countResult.next()) {
                    int count = countResult.getInt(1);
                    reservationId = "" + count;
                }

                int pricePerHour = 0;

                ResultSet rs2 = st.executeQuery("SELECT Price_Per_Hour FROM conf_room WHERE room_id =" + roomId);
                if (rs2.next()) {
                    pricePerHour = rs2.getInt("Price_Per_Hour");
                }


                // Define the format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Parse the strings
                LocalDateTime dateTime1 = LocalDateTime.parse(dateAndTime_s, formatter);
                LocalDateTime dateTime2 = LocalDateTime.parse(dateAndTime_e, formatter);

                Timestamp timestamp1 = Timestamp.valueOf(dateTime1);
                Timestamp timestamp2 = Timestamp.valueOf(dateTime2);

                Duration duration = Duration.between(timestamp1.toInstant(), timestamp2.toInstant());
                double hours = duration.toMinutes() / 60.0;

                double totalPrice = pricePerHour * hours;

                String query1 = "INSERT INTO payment_info VALUES('" + reservationId + "','" + totalPrice + "' , '" + paymentMethod + "', " + paymentDate + ", '" + paid + "')";

                String query2 = "INSERT INTO reservation VALUES('" + reservationId + "','" + city + "' , " + centerID + ", '" + dateAndTime_s + "', '" + dateAndTime_e + "', " + perticipants + ", " + equipment + ", '" + invoice + "', '" + customerID + "', '" + roomId + "', '" + reservationId + "')";

                st.executeUpdate(query1);
                st.executeUpdate(query2);


                cityBox.setValue(null);
                customerIDChoice.setValue(null);
                startingDatePicker.setValue(null);
                finishingDatePicker.setValue(null);
                centerIDChoice.setValue(null);
                paymentMethodChoice.setValue(null);
                startingTimeChoice.setValue(null);
                finifhingTimeChoice.setValue(null);
                perticipantsField.setText(null);
                equipmentField.setText(null);
                invoiceBox.setSelected(false);
                payNowBox.setSelected(false);


                reservationId = "-1";

            }
        }
        catch (Exception valueError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The data you have entered is incorrect");
            alert.showAndWait();
    }

}



    public void rowSelected()
    {
        ObservableList<String> selectedRow = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 9) {
            roomId = selectedRow.get(0);
            customerIDChoice.setDisable(false);
            paymentMethodChoice.setDisable(false);
            payNowBox.setDisable(false);
        }
    }
}
