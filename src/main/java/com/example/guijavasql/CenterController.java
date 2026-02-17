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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class CenterController
{
    private Stage stage;
    private Scene scene;
    private Parent root;

    String id = "-1";

    //sets table and columns
    @FXML
    TableView<ObservableList<String>> centerTable;

    @FXML
    TableColumn<ObservableList<String>, String> centerIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerNameCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerAddressCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerCityCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerPhoneCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerEmailCol;
    @FXML
    TableColumn<ObservableList<String>, String> centerAvailabilitycol;



    //sets fields
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField facilitiesField;


    //goes back
    public void backButtons(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    //puts data on the table
    public void showData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";
        String query = "SELECT * FROM conf_center";

        // Set up TableColumn cell factories (once is enough)
        centerIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        centerNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        centerAddressCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        centerCityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        centerPhoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        centerEmailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
        centerAvailabilitycol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(6)));

        // Clear old data
        centerTable.getItems().clear();

        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)
        ) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= 7; i++) {
                    row.add(rs.getString(i));
                }
                centerTable.getItems().add(row);
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

    //inserts data in table
    public void insertButton() throws IOException, SQLException
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
            //String id = "-1";
            String name = nameField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            Integer.valueOf(phoneField.getText());
            String phone = phoneField.getText();
            String email = emailField.getText();
            String facility = facilitiesField.getText();

            if (name == "" || address == "" || city == "" || email == "" || facility == "")
            {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect");
                alert.showAndWait();
            }

            if (wegood)
            {
                ResultSet countResult = st.executeQuery("SELECT COUNT(*) FROM conf_center");
                if (countResult.next()) {
                    int count = countResult.getInt(1);
                    id = "" + count;
                }


                String query = "INSERT INTO conf_center VALUES('" + id + "','"+name+"' , '"+address+"', '"+city+"', '"+phone+"', '"+email+"', '"+facility+"')";
                st.executeUpdate(query);

                nameField.clear();
                addressField.clear();
                cityField.clear();
                phoneField.clear();
                emailField.clear();
                facilitiesField.clear();

                id = "-1";

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


    //updates data on table
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
            String address = addressField.getText();
            String city = cityField.getText();
            Integer.valueOf(phoneField.getText());
            String phone = phoneField.getText();
            String email = emailField.getText();
            String facility = facilitiesField.getText();


            ResultSet existResult = st.executeQuery("SELECT 1 FROM conf_center WHERE center_ID = '"+id+"';");
            if(existResult.next()) ;
            else wegood = false;

            if (name == "" || address == "" || city == "" || email == "" || facility == ""|| wegood == false)
            {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect");
                alert.showAndWait();
            }

            if (wegood)
            {


                String query = "UPDATE conf_center SET name = '"+name+"', adress = '"+address+"', city = '"+city+"', phone_number = '"+phone+"', available_facilities = '"+facility+"', email = '"+email+"' WHERE center_id = "+id+";";
                st.executeUpdate(query);

                nameField.clear();
                addressField.clear();
                cityField.clear();
                phoneField.clear();
                emailField.clear();
                facilitiesField.clear();

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

    public void rowSelected()
    {
        ObservableList<String> selectedRow = centerTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 7) {
            id = selectedRow.get(0); // Save ID for update operations
            nameField.setText(selectedRow.get(1));
            addressField.setText(selectedRow.get(2));
            cityField.setText(selectedRow.get(3));
            phoneField.setText(selectedRow.get(4));
            emailField.setText(selectedRow.get(5));
            facilitiesField.setText(selectedRow.get(6));
        }
    }


}

