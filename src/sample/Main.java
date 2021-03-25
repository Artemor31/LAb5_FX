package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Properties.initialize();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("School DataBase");
        primaryStage.setScene(new Scene(root, 340, 490));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }

    public static class Properties{
        public static String FilePath;
        public static String userName;
        public static String password;
        public static String URL;
        public static int MaxValuesLength;
        public static String INPUT_ERROR;
        public static String SQL_ERROR;
        public static String ENTITY_ERROR;

        public static void initialize(){
            java.util.Properties prop = new java.util.Properties();
            loadProperties(prop);
            FilePath = getNextProperty("filePath", prop);
            userName = getNextProperty("userName", prop);
            password = getNextProperty("password", prop);
            URL = getNextProperty("URL", prop);
            MaxValuesLength =  Integer.parseInt(Objects.requireNonNull(getNextProperty("maxLength", prop)));
            INPUT_ERROR = getNextProperty("INPUT_ERROR", prop);
            SQL_ERROR = getNextProperty("SQL_ERROR", prop);
            ENTITY_ERROR = getNextProperty("ENTITY_ERROR", prop);
        }

        private static void loadProperties(java.util.Properties prop) {
            try {
                FileInputStream fis = new FileInputStream("./laba5.properties");
                prop.load(fis);
            }catch (IOException e) {
                System.out.println("Properties Not Found");
                e.printStackTrace();
            }
        }

        private static String getNextProperty(String name, java.util.Properties prop) {
            try {
                return new String(prop.getProperty(name).getBytes("ISO8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
