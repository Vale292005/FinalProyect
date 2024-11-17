package com.example.finalproyect.controllers;

import com.example.finalproyect.MyMap.MyTreeMap;
import com.example.finalproyect.UserTree.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.example.finalproyect.MyMap.MyTreeMap.loadFromFile;

public class User1Controller {

    @FXML
    private Button Aceptar;

    @FXML
    private TextField Contrasenha;

    @FXML
    private Button Registrar;

    @FXML
    private Button Regresar;

    @FXML
    private TextField Usuario;

    private MyTreeMap<User,String> UserMap;

    public void initialize(){
        UserMap=new MyTreeMap<>();
        UserMap=loadFromFile("C:\\Users\\Valeria\\Desktop\\user.txt");
        for (MyTreeMap.Entry<User, String> entry : UserMap.entrySet()) {
            System.out.println("Usuario: " + entry.getKey() + " | Valor: " + entry.getValue());
        }
        if (UserMap.size() == 0) {
            System.out.println("El mapa está vacío.");
        } else {
            System.out.println("El mapa contiene " + UserMap.size() + " entradas.");
        }
    }
    @FXML
    void OpenUser2(ActionEvent event) {
        String cent=Usuario.getText();
        User user=new User(null,null,null,cent);
        String contrasenhaText=Contrasenha.getText();

        if(UserMap!=null&&!contrasenhaText.isEmpty()){
            System.out.println(contrasenhaText);
            for (MyTreeMap.Entry<User, String> entry : UserMap.entrySet()) {

                if(entry.getValue().equals(contrasenhaText)){
                    try {
                        Stage currentStage = (Stage) Aceptar.getScene().getWindow();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User2.fxml"));
                        Scene secondScene = new Scene(loader.load());
                        Stage secondStage = new Stage();
                        secondStage.setScene(secondScene);
                        secondStage.show();
                        currentStage.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }}
        else{            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Ingreso invalido");
            alert.showAndWait();}
    }

    @FXML
    void Registrar(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Registrar.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registro.fxml"));
            Scene secondScene = new Scene(loader.load());
            Stage secondStage = new Stage();
            secondStage.setScene(secondScene);
            secondStage.show();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Return(ActionEvent event) {
        try {
            Stage currentStage = (Stage) Aceptar.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
            Scene secondScene = new Scene(loader.load());
            Stage secondStage = new Stage();
            secondStage.setScene(secondScene);
            secondStage.show();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void getContrasenha(ActionEvent event) {

    }

    @FXML
    void getUser(ActionEvent event) {

    }




}

