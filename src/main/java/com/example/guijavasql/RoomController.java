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

public class RoomController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    //Sets tables and columns
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

    //Sets all fields
    @FXML
    private ChoiceBox<String> centerIDChoice;
    @FXML
    private TextField nameField;
    @FXML
    private TextField maxCapField;
    @FXML
    private TextField seatTypeField;
    @FXML
    private TextField equipmentField;
    @FXML
    private TextField priceField;
    @FXML
    private CheckBox wifiBox;
    @FXML
    private CheckBox availabilityBox;
    @FXML
    private ChoiceBox<String> filterChoice;
    @FXML
    private ChoiceBox<String> centerID2Choice;

    String roomId = "-1";
    //Button to go to main screen
    public void backButtons(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }
    //Show table data
    public void showData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";
        String query = "SELECT * FROM conf_room";

        // Set up TableColumn cell factories
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

        String[] centerNames = nameList.toArray(new String[0]);
        centerIDChoice.getItems().clear();
        centerIDChoice.getItems().addAll(centerNames);

        String[] filters = {"Show all rooms (default)", "Filter for a specific center", "Show all booked rooms", "Show booked but not paid rooms on this instant"};
        filterChoice.getItems().clear();
        filterChoice.getItems().addAll(filters);

        centerID2Choice.getItems().clear();
        centerID2Choice.getItems().addAll(centerNames);



    }

    //updates filters
    public void filterSelected(ActionEvent e) throws IOException, SQLException {
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";

        String filter = filterChoice.getValue();
        if (filter == null) filter = "";
        if(filter.equals("Show all rooms (default)"))
        {
            centerID2Choice.setDisable(true);
            showData();
        }

        else if(filter.equals("Filter for a specific center"))
        {
            if (!centerID2Choice.isDisable())
            {
                centerID2Choice.setDisable(true);
                //centerIDChoice.getItems().clear();

            }
            else
            {
                centerID2Choice.setDisable(false);
            }

        }
        else if(filter.equals("Show all booked rooms"))
        {
            centerID2Choice.setDisable(true);
            String query = "SELECT cr.* FROM reservation r JOIN conf_room cr ON r.Conf_Room_room_ID = cr.room_ID WHERE NOW() BETWEEN r.date_and_starting_time AND r.date_and_finishing_time;";

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
        else if (filter.equals("Show booked but not paid rooms on this instant"))
        {
                centerID2Choice.setDisable(true);
                String query = "SELECT cr.* FROM reservation r JOIN payment_info pi ON r.Payment_Info_Payment_info_ID = pi.Payment_info_ID JOIN conf_room cr ON r.Conf_Room_room_ID = cr.room_ID WHERE pi.payment_condition = 0;";
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

    //updates filter based on a specific center
    public void centerSelected(ActionEvent e) throws IOException, SQLException {
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";

        String centerId2 = centerID2Choice.getValue();
        if(centerId2 != null) {

            String query = "SELECT * FROM conf_room WHERE Conf_Center_center_ID = " + centerId2 + "";
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


    //initializes data
    @FXML
    public void initialize() throws SQLException, IOException
    {
        showData();

        filterChoice.setOnAction(e -> {
            try {
                filterSelected(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace(); // or handle however you'd like
            }
        });

        centerID2Choice.setOnAction(e -> {
            try {
                centerSelected(e);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace(); // or handle however you'd like
            }
        });

    }

    //does insert button commands
    public void insertButton() throws IOException, SQLException
    {
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
            //String id = "-1";
            String name = nameField.getText();
            String centerId = centerIDChoice.getValue();
            String maxCap = maxCapField.getText();
            Integer.valueOf(maxCapField.getText());
            String seatType = seatTypeField.getText();
            String equipment = equipmentField.getText();
            String price = priceField.getText();
            Integer.valueOf(priceField.getText());
            String wifi = "0", availability = "0";

            if (wifiBox.isSelected()) wifi = "1";
            if (availabilityBox.isSelected()) availability = "1";

            if (name == "" || centerId == "" || maxCap == "" || seatType == "" || equipment == "" || price == "") {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect");
                alert.showAndWait();
            }

            if (wegood) {
                ResultSet countResult = st.executeQuery("SELECT COUNT(*) FROM conf_room");
                if (countResult.next()) {
                    int count = countResult.getInt(1);
                    roomId = "" + count;
                }


                String query = "INSERT INTO conf_room VALUES('" + roomId + "','"+centerId+"' , '"+name+"', '"+maxCap+"', '"+seatType+"', '"+equipment+"', '"+wifi+"', '"+price+"', '"+availability+"')";
                st.executeUpdate(query);

                nameField.clear();
                maxCapField.clear();
                seatTypeField.clear();
                equipmentField.clear();
                priceField.clear();
                wifiBox.setSelected(false);
                availabilityBox.setSelected(false);



                roomId = "-1";

                showData();
            }
        } catch (Exception valueError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The data you have entered is incorrect");
            alert.showAndWait();
        }
    }

    //updates the table
    public void updateButton() throws IOException, SQLException
    {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "confdatabase";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";

        try
        {

            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url+dbName,userName,password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Statement st = conn.createStatement();
        try
        {
            boolean wegood = true;
            String name = nameField.getText();
            String centerId = centerIDChoice.getValue();
            String maxCap = maxCapField.getText();
            Integer.valueOf(maxCapField.getText());
            String seatType = seatTypeField.getText();
            String equipment = equipmentField.getText();
            String price = priceField.getText();
            Integer.valueOf(priceField.getText());
            String wifi = "0", availability = "0";

            if (wifiBox.isSelected()) wifi = "1";
            if (availabilityBox.isSelected()) availability = "1";


            ResultSet existResult = st.executeQuery("SELECT 1 FROM conf_room WHERE room_ID = '"+roomId+"';");
            if(existResult.next()) ;
            else wegood = false;

            if (name == "" || centerId == "" || maxCap == "" || seatType == "" || equipment == "" || price == ""|| !wegood)
            {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect");
                alert.showAndWait();
            }

            if (wegood)
            {


                String query = "UPDATE conf_room SET name = '"+name+"', Conf_Center_center_ID = '"+centerId+"', max_capacity = '"+maxCap+"', seat_type = '"+seatType+"', equipment = '"+equipment+"', WIFI_availability = '"+wifi+"', Price_Per_Hour = '"+price+"', availability = '"+availability+"' WHERE room_ID = "+roomId+";";
                st.executeUpdate(query);

                nameField.clear();
                maxCapField.clear();
                seatTypeField.clear();
                equipmentField.clear();
                priceField.clear();
                wifiBox.setSelected(false);
                availabilityBox.setSelected(false);

                showData();
            }
        }
        catch (Exception valueError)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The data you have entered is incorrect");
            alert.showAndWait();
        }

    }

    //highlights row and copies inputs to fields
    public void rowSelected()
    {
        ObservableList<String> selectedRow = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 9) {
            roomId = selectedRow.get(0);
            nameField.setText(selectedRow.get(2));
            maxCapField.setText(selectedRow.get(3));
            seatTypeField.setText(selectedRow.get(4));
            equipmentField.setText(selectedRow.get(5));
            priceField.setText(selectedRow.get(7));
        }
    }
}
