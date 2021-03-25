package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button executeBtn;

    @FXML
    private Button parseBtn;

    @FXML
    private RadioButton dbRad;

    @FXML
    private ToggleGroup pamParam;

    @FXML
    private RadioButton xmlRad;

    @FXML
    private Label resultMsg;

    @FXML
    private ComboBox<String> actionsBox;

    @FXML
    private TextField idFld;

    @FXML
    private TextField regFld;

    @FXML
    private TextField cityFld;

    @FXML
    private TextField strFld;

    @FXML
    private TextField nameFld;

    @FXML
    private TextField dirFld;

    @FXML
    private Label guideMsg;

    TextField[] fields;
    String[] searchValues = null;

    @FXML
    void initialize() {
        setUp();
        fields = new TextField[]{idFld, regFld, cityFld, strFld, nameFld, dirFld};
        parseBtn.setOnMouseClicked(mouseEvent -> parse());
        executeBtn.setOnMouseClicked(mouseEvent -> executeAction());
        actionsBox.setOnMouseClicked(mouseEvent -> guideMsg.setText(""));
        actionsBox.setOnAction(actionEvent -> onActionChanged());
    }
    private void onActionChanged(){
        searchValues = null;
        for(var field : fields)
            field.setText("");

        switch (actionsBox.getValue()) {
            case "Add record" -> {
                guideMsg.setText("Enter new values");
                idFld.setEditable(false);
            }
            case "Change record",
                    "Remove record" -> {
                guideMsg.setText("Enter search values");
                idFld.setEditable(true);
            }
        }
    }

    private void executeAction(){
        IParser parserType;
        if(dbRad.isSelected())
            parserType = new DataBase(Main.Properties.userName, Main.Properties.password, Main.Properties.URL);
        else
            parserType = new XmlEditor(Main.Properties.FilePath);

        switch (actionsBox.getValue()) {
            case "Add record" -> add(parserType);
            case "Change record" -> change(parserType);
            case "Remove record" -> remove(parserType);
        }
    }

    private void add(IParser parser){
        parser.addRecord(getFieldsValues());
        guideMsg.setText("Record added");
    }

    private void change(IParser parser){
        if (searchValues == null) {
            searchValues = getFieldsValues();
            guideMsg.setText("Enter new values");
            for(var field : fields)
                field.setText("");

        }
        else{
            parser.changeRecord(searchValues, getFieldsValues());
            guideMsg.setText("Record changed");
        }
    }

    private void remove(IParser parser){
        parser.removeRecord(getFieldsValues());
        guideMsg.setText("Records removed");
    }

    private void parse(){
        if (xmlRad.isSelected())
            new XmlEditor(Main.Properties.FilePath).parseRecords();
        else
            new DataBase(Main.Properties.userName, Main.Properties.password,
                    Main.Properties.URL).parseRecords();
    }

    private void setUp() {
        ObservableList<String> actions = FXCollections.observableArrayList(
                "Add record", "Change record", "Remove record");
        actionsBox.setItems(actions);
        guideMsg.setText("Choose action: ");
        resultMsg.setText("");
    }

    private String[] getFieldsValues(){
        String[] values = new String[fields.length];

        for (int i = 0; i < fields.length; i++)
            values[i] = fields[i].getText();
        return values;
    }
}
