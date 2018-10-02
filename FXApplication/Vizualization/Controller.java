package Vizualization;


import Logic.CommandsGetter;
import Logic.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class Controller {

    Logic.Program tempProg;

    @FXML
    private Button settingBtn;
    @FXML
    private ListView serviceList;
    @FXML
    private TextField workTimeField;
    @FXML
    private TextField restTimeField;
    @FXML
    private CheckBox isChecked;
    @FXML
    private Label workLab;
    @FXML
    private Label restLab;
    @FXML
    private Button saveBtn;
    @FXML
    private Button refBtn;
    @FXML
    private Button submit;
    @FXML
    private Label openingLab;
    @FXML
    private CheckBox inTray;
    @FXML
    private CheckBox openingWithWindows;
    @FXML
    private TextField openingTimeField;

    public void makeRefresh(ActionEvent event) {
        List<String> services = CommandsGetter.getCurrentApplications();
        serviceList.getItems().clear();
        for (String elem :
                services) {
            if (elem.compareTo("Program Manager") != 0)
                serviceList.getItems().add(elem);
        }
    }

    public void handleMouseClick(MouseEvent arg0) {
        tempProg = CommandsGetter.getService(serviceList.getSelectionModel().getSelectedItem().toString());
        workTimeField.clear();
        workTimeField.insertText(0, Integer.toString(tempProg.getWork_time()));
        restTimeField.clear();
        restTimeField.insertText(0, Integer.toString(tempProg.getRest_time()));
        isChecked.setSelected(true);
        if (tempProg.getIs_checked() == 0)
            isChecked.setSelected(false);
    }

    public void saveServiceSettings(ActionEvent event) {
        if (tempProg != null) {
            tempProg.setIs_checked(true);
            if (!isChecked.isSelected())
                tempProg.setIs_checked(false);
            try {
                if (Integer.parseInt(workTimeField.getText()) >= 0)
                    tempProg.setWork_time(Integer.parseInt(workTimeField.getText()));
                else
                    throw new Exception();
            } catch (Exception e) {
                workTimeField.clear();
                workTimeField.insertText(0, Integer.toString(tempProg.getWork_time()));
            }
            try {
                if (Integer.parseInt(restTimeField.getText()) >= 0)
                    tempProg.setRest_time(Integer.parseInt(restTimeField.getText()));
                else
                    throw new Exception();
            } catch (Exception e) {
                restTimeField.clear();
                restTimeField.insertText(0, Integer.toString(tempProg.getRest_time()));
            }
        }
    }

    public void submitBtn(ActionEvent event){
        List<Property> tempProp = CommandsGetter.getProperties();
        tempProp.get(0).setValue(0);
        if (inTray.isSelected())
            tempProp.get(0).setValue(1);
        tempProp.get(1).setValue(0);
        if (openingWithWindows.isSelected())
            tempProp.get(1).setValue(1);
        try {
            if (Integer.parseInt(openingTimeField.getText()) >= 0)
                tempProp.get(2).setValue(Integer.parseInt(openingTimeField.getText()));
            else
                throw new Exception();
        } catch (Exception e) {
           openingTimeField.clear();
           openingTimeField.insertText(0, Integer.toString(tempProp.get(2).getValue()));
        }
        CommandsGetter.controlTray();
        CommandsGetter.controlStartUp();
    }

    private void setUpSettings() {
        inTray.setSelected(false);
        if (CommandsGetter.getProperties().get(0).getValue() == 1)
            inTray.setSelected(true);
        openingWithWindows.setSelected(false);
        if (CommandsGetter.getProperties().get(1).getValue() == 1)
            openingWithWindows.setSelected(true);
        openingTimeField.clear();
        openingTimeField.setText(Integer.toString(CommandsGetter.getProperties().get(2).getValue()));
    }

    public void handleSettingBtn(ActionEvent event) {
        if (settingBtn.getText().compareTo("Settings") == 0) {
            settingBtn.setText("Main");
            workLab.setVisible(false);
            restLab.setVisible(false);
            saveBtn.setVisible(false);
            refBtn.setVisible(false);
            serviceList.setVisible(false);
            isChecked.setVisible(false);
            workTimeField.setVisible(false);
            restTimeField.setVisible(false);
            submit.setVisible(true);
            openingLab.setVisible(true);
            inTray.setVisible(true);
            openingWithWindows.setVisible(true);
            openingTimeField.setVisible(true);
            setUpSettings();
        } else {
            settingBtn.setText("Settings");
            workLab.setVisible(true);
            restLab.setVisible(true);
            saveBtn.setVisible(true);
            refBtn.setVisible(true);
            serviceList.setVisible(true);
            isChecked.setVisible(true);
            workTimeField.setVisible(true);
            restTimeField.setVisible(true);
            submit.setVisible(false);
            openingLab.setVisible(false);
            inTray.setVisible(false);
            openingWithWindows.setVisible(false);
            openingTimeField.setVisible(false);
        }
    }
}
