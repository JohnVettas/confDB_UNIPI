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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class reservationController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    //sets columns and table
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

    //sets fields
    @FXML
    private ChoiceBox<String> customerIDChoice;
    @FXML
    private ChoiceBox<String> roomIDChoice;
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
    private TextField paymentAmountField;
    @FXML
    private CheckBox invoiceBox;
    @FXML
    private CheckBox payNowBox;
    @FXML
    private ChoiceBox<String> paymentMethodChoice;
    @FXML
    private ChoiceBox<String> cityBox;


    String reservationId = "-1";

    //go back
    public void backButtons(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    //put data on table
    public void showData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";
        String query = "SELECT * FROM reservation WHERE date_and_starting_time IS NOT NULL";

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

        String query3 = "SELECT room_ID FROM conf_room";
        ArrayList<String> nameList2 = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query3)
        ) {
            while (rs.next()) {
                nameList2.add(rs.getString("room_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] idroomList = nameList2.toArray(new String[0]);
        roomIDChoice.getItems().clear();
        roomIDChoice.getItems().addAll(idroomList);

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

        String[] method = {"Cash", "Card"};
        paymentMethodChoice.getItems().clear();
        paymentMethodChoice.getItems().addAll(method);


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

    }

    //initialize's data
    @FXML
    public void initialize() throws SQLException, IOException {
        cityBox.setOnAction(e -> {
            try {
                citySelected(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });
        centerIDChoice.setOnAction(e -> {
            try {
                centerSelected(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });
        showData();
    }

    //changes data based on city selected
    public void citySelected(ActionEvent e) throws IOException, SQLException {
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


        String query3 = "SELECT room_ID FROM conf_room, conf_center WHERE conf_center.city = '" + city + "' AND conf_center.center_id = conf_room.Conf_Center_center_ID";
        ArrayList<String> nameList2 = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query3)
        ) {
            while (rs.next()) {
                nameList2.add(rs.getString("room_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] idroomList = nameList2.toArray(new String[0]);
        roomIDChoice.getItems().clear();
        roomIDChoice.getItems().addAll(idroomList);

    }

    //changes data based on center selected
    public void centerSelected(ActionEvent e) throws IOException, SQLException {
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";

        String centerID = centerIDChoice.getValue();

        String query3 = "SELECT room_ID FROM conf_room WHERE '" + centerID + "' = conf_room.Conf_Center_center_ID";
        ArrayList<String> nameList2 = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query3)
        ) {
            while (rs.next()) {
                nameList2.add(rs.getString("room_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] idroomList = nameList2.toArray(new String[0]);
        roomIDChoice.getItems().clear();
        roomIDChoice.getItems().addAll(idroomList);

    }


    //inserts to table
    public void insertButton() throws IOException, SQLException {
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
            String perticipants = perticipantsField.getText();
            if (perticipants == null) {
                perticipants = "NULL";
            } else if (perticipants.isBlank()) {
                perticipants = "NULL";
            } else Integer.parseInt(perticipants);
            String startingTime = startingTimeChoice.getValue();
            String finishingTime = finifhingTimeChoice.getValue();
            String equipment = equipmentField.getText();
            if (equipment == null) equipment = "NULL";
            else if (equipment.isBlank()) equipment = "NULL";
            else equipment = "'" + equipment + "'";
            String paymentDate;
            String paymentAmount = paymentAmountField.getText();
            Integer.parseInt(paymentAmount);
            String customerID = customerIDChoice.getValue();
            String roomID = roomIDChoice.getValue();
            String centerID = centerIDChoice.getValue();
            String paymentMethod = paymentMethodChoice.getValue();
            String city = cityBox.getValue();
            String paid, invoice;

            LocalDate startingDate = startingDatePicker.getValue();
            LocalDate finishingDate = finishingDatePicker.getValue();

            String dateAndTime_s = startingDate.toString() + " " + startingTime;
            String dateAndTime_e = finishingDate.toString() + " " + finishingTime;

            if (payNowBox.isSelected()) paid = "1";
            else paid = "0";
            if (invoiceBox.isSelected()) invoice = "1";
            else invoice = "0";
            if (paid.equals("0")) paymentDate = "NULL";
            else paymentDate = "NOW()";

            // Define the format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Parse the strings
            LocalDateTime dateTime1 = LocalDateTime.parse(dateAndTime_s, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(dateAndTime_e, formatter);

            if (dateTime1.isAfter(dateTime2)) {
                wegood = false;
            }


            if (!wegood || city.isBlank() || paymentMethod.isBlank() || startingTime.isBlank() || finishingTime.isBlank() || paymentAmount.isBlank() || customerID.isBlank() || roomID.isBlank() || centerID.isBlank()) {
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

                String query1 = "INSERT INTO payment_info VALUES('" + reservationId + "','" + paymentAmount + "' , '" + paymentMethod + "', " + paymentDate + ", '" + paid + "')";

                String query2 = "INSERT INTO reservation VALUES('" + reservationId + "','" + city + "' , " + centerID + ", '" + dateAndTime_s + "', '" + dateAndTime_e + "', " + perticipants + ", " + equipment + ", '" + invoice + "', '" + customerID + "', '" + roomID + "', '" + reservationId + "')";

                st.executeUpdate(query1);
                st.executeUpdate(query2);


                cityBox.setValue(null);
                customerIDChoice.setValue(null);
                roomIDChoice.setValue(null);
                startingDatePicker.setValue(null);
                finishingDatePicker.setValue(null);
                centerIDChoice.setValue(null);
                paymentMethodChoice.setValue(null);
                startingTimeChoice.setValue(null);
                finifhingTimeChoice.setValue(null);
                perticipantsField.setText(null);
                paymentAmountField.setText(null);
                equipmentField.setText(null);
                invoiceBox.setSelected(false);
                payNowBox.setSelected(false);


                reservationId = "-1";

                showData();
            }
        } catch (Exception valueError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The data you have entered is incorrect \n Make sure you have entered the correct time format");
            alert.showAndWait();
            valueError.printStackTrace();
        }
    }

    //updates table
    public void updateButton() throws IOException, SQLException {
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
            String perticipants = perticipantsField.getText();

            if (perticipants == null) {
                perticipants = "NULL";
            } else if (perticipants.isBlank()) {
                perticipants = "NULL";
            } else Integer.parseInt(perticipants);
            String startingTime = startingTimeChoice.getValue();
            String finishingTime = finifhingTimeChoice.getValue();
            String equipment = equipmentField.getText();
            if (equipment == null) equipment = "NULL";
            else if (equipment.isBlank()) equipment = "NULL";
            else equipment = "'" + equipment + "'";
            String paymentDate;
            String paymentAmount = paymentAmountField.getText();
            Integer.parseInt(paymentAmount);
            String customerID = customerIDChoice.getValue();
            String roomID = roomIDChoice.getValue();
            String centerID = centerIDChoice.getValue();
            String paymentMethod = paymentMethodChoice.getValue();
            String city = cityBox.getValue();
            String paid, invoice;

            LocalDate startingDate = startingDatePicker.getValue();
            LocalDate finishingDate = finishingDatePicker.getValue();

            String dateAndTime_s = startingDate.toString() + " " + startingTime;
            String dateAndTime_e = finishingDate.toString() + " " + finishingTime;

            if (payNowBox.isSelected()) paid = "1";
            else paid = "0";
            if (invoiceBox.isSelected()) invoice = "1";
            else invoice = "0";
            if (paid.equals("0")) paymentDate = "NULL";
            else paymentDate = "NOW()";

            // Define the format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Parse the strings
            LocalDateTime dateTime1 = LocalDateTime.parse(dateAndTime_s, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(dateAndTime_e, formatter);

            ResultSet existResult = st.executeQuery("SELECT 1 FROM reservation WHERE reservation_ID = '" + reservationId + "';");
            if (existResult.next()) ;
            else wegood = false;

            if (dateTime1.isAfter(dateTime2)) {
                wegood = false;
            }


            if (!wegood || city.isBlank() || paymentMethod.isBlank() || startingTime.isBlank() || finishingTime.isBlank() || paymentAmount.isBlank() || customerID.isBlank() || roomID.isBlank() || centerID.isBlank()) {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect \n Make sure you have entered an ending date after the starting date \n and that none of the mandatory fields are empty");
                alert.showAndWait();
            }

            if (wegood) {
                ResultSet countResult = st.executeQuery("SELECT COUNT(*) FROM reservation");
//                if (countResult.next()) {
//                    int count = countResult.getInt(1);
//                    reservationId = "" + count;
//                }

                String query1 = "UPDATE payment_info SET amount = '" + paymentAmount + "', payment_method = '" + paymentMethod + "', payment_date = " + paymentDate + ", payment_condition = '" + paid + "' WHERE Payment_Info_ID = " + reservationId + ";";

                String query2 = "UPDATE reservation SET city = '" + city + "', prefered_center = " + centerID + ", date_and_starting_time = '" + dateAndTime_s + "', date_and_finishing_time = '" + dateAndTime_e + "', number_of_participators = " + perticipants + ", prefrences_in_equipment = " + equipment + ", invoice = '" + invoice + "', Customer_customer_ID = '" + customerID + "', Conf_Room_room_ID = '" + roomID + "' WHERE reservation_ID = " + reservationId + ";";


                st.executeUpdate(query1);
                st.executeUpdate(query2);


                cityBox.setValue(null);
                customerIDChoice.setValue(null);
                roomIDChoice.setValue(null);
                startingDatePicker.setValue(null);
                finishingDatePicker.setValue(null);
                centerIDChoice.setValue(null);
                paymentMethodChoice.setValue(null);
                startingTimeChoice.setValue(null);
                finifhingTimeChoice.setValue(null);
                perticipantsField.setText(null);
                paymentAmountField.setText(null);
                equipmentField.setText(null);
                invoiceBox.setSelected(false);
                payNowBox.setSelected(false);


                reservationId = "-1";

                showData();
            }
        } catch (Exception valueError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The data you have entered is incorrect \n Make sure you have entered the correct time format");
            alert.showAndWait();
            //valueError.printStackTrace();
        }
    }

    public void rowSelected() {
        ObservableList<String> selectedRow = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 11) {
            reservationId = selectedRow.get(0);
            perticipantsField.setText(selectedRow.get(5));
            equipmentField.setText(selectedRow.get(6));

        }
    }

    //cencels booking based on
    public void cancelBooking() throws SQLException, IOException {


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

        String query = "SELECT * FROM reservation JOIN payment_info pi ON pi.Payment_info_ID = Payment_Info_Payment_info_ID WHERE NOW() < date_and_starting_time - INTERVAL 72 HOUR AND pi.payment_condition = 0 AND reservation_ID = " + reservationId;


        ResultSet existResult = st.executeQuery("SELECT 1 FROM reservation WHERE reservation_ID = '" + reservationId + "';");

        boolean exists = existResult.next();
        if (exists) {
            ResultSet existResult2 = st.executeQuery(query);
            boolean exists2 = existResult2.next();
            if (exists2) {
                String query2 = "UPDATE reservation SET date_and_starting_time = NULL, date_and_finishing_time = NULL WHERE reservation_ID = " + reservationId;
                st.executeUpdate(query2);
                showData();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The table you have enter is either starting in less than 72 hours or \n it has already been paid");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You need to first select a reservation to cancel it");
            alert.showAndWait();
        }


    }

}


