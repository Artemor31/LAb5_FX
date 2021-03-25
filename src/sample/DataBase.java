package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase implements IParser{

    static String userName;
    static String password;
    static String URL;

    public DataBase(String userName, String password, String URL){
        DataBase.userName = userName;
        DataBase.password = password;
        DataBase.URL = URL;
    }

    public static Statement getConnectStatement() {
        Statement statement = null;
        Connection connection;

        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try{
            connection = DriverManager.getConnection(URL, userName, password);
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Schools( " +
                    "id int auto_increment primary key," +
                    "region varchar(30) not null," +
                    "city varchar(30) not null," +
                    "street varchar(30) not null," +
                    "name varchar(30) not null," +
                    "directorName varchar(30) not null)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return statement;
    }

    public void addRecord(String[] values) {
        School school = new School(values[1], values[2], values[3], values[4], values[5]);
        Statement statement = DataBase.getConnectStatement();
        if(statement == null) {
            System.out.println("Error to return statement");
            return;
        }
        try {
            statement.executeUpdate("insert into schools (region, city, street, name, directorName) " +
                    "VALUES ('" + school.getRegion() +
                    "', '" + school.getCity() +
                    "', '" + school.getStreet() +
                    "', '" + school.getName() +
                    "', '" + school.getDirectorName() + "')");
        } catch (SQLException e) {
            System.out.println(Main.Properties.SQL_ERROR);
            e.printStackTrace();
        }
    }

    public void parseRecords(){
        var schools = new ArrayList<School>();
        Statement statement = DataBase.getConnectStatement();
        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery("select * from schools");

            while (resultSet.next()) {
                schools.add(new School(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)));
            }
        }catch (SQLException e) {
            System.out.println(Main.Properties.SQL_ERROR);
            e.printStackTrace();
        }
        new XmlEditor(Main.Properties.FilePath).writeToXml(Main.Properties.FilePath, schools);
    }

    private List<Integer> findEntityInDB(String[] searchValues) {
        Request request = new Request();
        Statement statement = DataBase.getConnectStatement();
        ResultSet resultSet;
        ArrayList<Integer> ids = new ArrayList<>();
        createRequest(searchValues, request);
        try {
            String req = request.buildRequest();
            resultSet = statement.executeQuery(req);

            while (resultSet.next()) {
                ids.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(Main.Properties.ENTITY_ERROR);
            Main.main(null);
        }
        return ids;
    }

    private void createRequest(String[] searchValues, Request request) {
        if(!searchValues[0].equals(""))
            request.idReq = " id = '" + searchValues[0] + "'";
        if(!searchValues[1].equals(""))
            request.regReq = " region = '" + searchValues[1] + "'";
        if(!searchValues[2].equals(""))
            request.cityReq = " city = '"  + searchValues[2] + "'";
        if(!searchValues[3].equals(""))
            request.streetReq = " street = '" + searchValues[3] + "'";
        if(!searchValues[4].equals(""))
            request.nameReq = " name = '" + searchValues[4] + "'";
        if(!searchValues[5].equals(""))
            request.directorReq = " directorName = '" + searchValues[5] + "'";
    }

    public void changeRecord(String[] searchValues, String[] newValues) {
        List<Integer> ids = findEntityInDB(searchValues);
        Statement statement = DataBase.getConnectStatement();

        for (Integer i : ids) {
            if(!newValues[1].equals(""))
                executeUpdateRequest(statement, i, "region", newValues[1]);
            if(!newValues[2].equals(""))
                executeUpdateRequest(statement, i, "city", newValues[2]);
            if(!newValues[3].equals(""))
                executeUpdateRequest(statement, i, "street", newValues[3]);
            if(!newValues[4].equals(""))
                executeUpdateRequest(statement, i, "name", newValues[4]);
            if(!newValues[5].equals(""))
                executeUpdateRequest(statement, i, "directorName", newValues[5]);
        }
    }

    private void executeUpdateRequest(Statement statement, int id, String column, String newValues){
        try {
            statement.executeUpdate(" UPDATE schools " +
                    "SET " + column + " = '" + newValues + "' " +
                    "WHERE id = '" + id + "'");
        } catch (SQLException e) {
            System.out.println(Main.Properties.SQL_ERROR);
        }
    }

    public void removeRecord(String[] searchValues){
        List<Integer> ids = findEntityInDB(searchValues);
        Statement statement = DataBase.getConnectStatement();

        for(int id : ids){
            try {
                statement.executeUpdate("delete from schools " +
                                            "where id = " + id);
            } catch (SQLException e) {
                System.out.println(Main.Properties.ENTITY_ERROR);
                e.printStackTrace();
            }
        }
    }

    private static class Request{
        public boolean isFirst = true;
        public String startReq = "select * from schools where ";
        public String idReq = "";
        public String regReq = "";
        public String cityReq = "";
        public String streetReq = "";
        public String nameReq = "";
        public String directorReq = "";

        public String buildRequest(){
            addReq(idReq);
            addReq(regReq);
            addReq(cityReq);
            addReq(streetReq);
            addReq(nameReq);
            addReq(directorReq);
            return startReq;
        }

        private void addReq(String req){
            if(!req.equals("") && isFirst) {
                startReq += req;
                isFirst = false;
            }
            else if(!req.equals("")) {
                startReq += " and " + req;
            }
        }
    }
}

//    public void printAll(){
//        Statement statement = DataBase.getConnectStatement();
//        ResultSet resultSet;
//        try {
//            resultSet = statement.executeQuery("select * from schools");
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getInt(1));
//                System.out.println(resultSet.getString(2));
//                System.out.println(resultSet.getString(3));
//                System.out.println(resultSet.getString(4));
//                System.out.println("---------------------------------");
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
