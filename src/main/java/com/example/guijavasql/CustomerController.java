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

public class CustomerController
{
    private Stage stage;
    private Scene scene;
    private Parent root;
    String id = "-1";

    @FXML
    TableView<ObservableList<String>> customerTable;

    @FXML
    TableColumn<ObservableList<String>, String> customerIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> nameIdCol;
    @FXML
    TableColumn<ObservableList<String>, String> emailCol;
    @FXML
    TableColumn<ObservableList<String>, String> phoneCol;




    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;



    public void backButtons(ActionEvent e) throws IOException {

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void showData() throws IOException, SQLException {
        // Database details
        String url = "jdbc:mysql://localhost:3306/confdatabase";
        String userName = "root";
        String password = "";
        String query = "SELECT * FROM customer";

        // Set up TableColumn cell factories (once is enough)
        customerIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        nameIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));


        // Clear old data
        customerTable.getItems().clear();

        try (
                Connection conn = DriverManager.getConnection(url, userName, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)
        ) {
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= 4; i++) {
                    row.add(rs.getString(i));
                }
                customerTable.getItems().add(row);
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
            Integer.parseInt(phoneField.getText());
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (name == "" || email == "")
            {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect");
                alert.showAndWait();
            }

            if (wegood)
            {
                ResultSet countResult = st.executeQuery("SELECT COUNT(*) FROM customer");
                if (countResult.next()) {
                    int count = countResult.getInt(1);
                    id = "" + count;
                }


                String query = "INSERT INTO customer VALUES('" + id + "','"+name+"' , '"+email+"', '"+phone+"')";
                st.executeUpdate(query);

                nameField.clear();
                phoneField.clear();
                emailField.clear();


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
            Integer.parseInt(phoneField.getText());
            String phone = phoneField.getText();
            String email = emailField.getText();



            ResultSet existResult = st.executeQuery("SELECT 1 FROM customer WHERE customer_ID = '"+id+"';");
            if(existResult.next()) ;
            else wegood = false;

            if (name == "" || email == "" || wegood == false)
            {
                wegood = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The data you have entered is incorrect");
                alert.showAndWait();
            }

            if (wegood)
            {


                String query = "UPDATE customer SET full_name = '"+name+"', email = '"+email+"', phone_number = '"+phone+"' WHERE customer_ID = "+id+";";
                st.executeUpdate(query);

                nameField.clear();
                phoneField.clear();
                emailField.clear();


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
        ObservableList<String> selectedRow = customerTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null && selectedRow.size() >= 4) {
            id = selectedRow.get(0);
            nameField.setText(selectedRow.get(1));
            emailField.setText(selectedRow.get(2));
            phoneField.setText(selectedRow.get(3));


        }
    }
}
