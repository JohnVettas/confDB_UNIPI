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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;




public class MainController
{


    private Stage stage;
    private Scene scene;
    private Parent root;



    public void updateInsertButton(ActionEvent e) throws IOException
    {


        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("center-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();


    }
    public void backButtons(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }
    public void roomButton(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("room-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void makeReservationButton(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("makeReservation-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 825);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteReservationButton(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("delete-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 825);
        stage.setScene(scene);
        stage.show();
    }

    public void reservationButton(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("reservation-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1400, 825);
        stage.setScene(scene);
        stage.show();
    }

    public void paymentButton(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("payment-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }
    public void customerButton(ActionEvent e) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-view.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }
}