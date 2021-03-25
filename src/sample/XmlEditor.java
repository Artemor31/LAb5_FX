package sample;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class XmlEditor implements IParser {
    private final String path;

    public XmlEditor(String path){
        this.path = path;
    }

    public void writeToXml(String path, ArrayList<School> schools){
        DOMWriter.write(path, schools);
    }

    public  ArrayList<School> readXml(String path){
        return SAXReader.read(path);
    }

    public void addRecord(String[] values){
        var list = readXml(path);
        list.add(new School(values[1], values[2], values[3], values[4], values[5]));
        writeToXml(path, list);
    }


    public void parseRecords(){
        DataBase dataBase =  new DataBase(Main.Properties.userName, Main.Properties.password, Main.Properties.URL);
        ArrayList<School> schools = this.readXml(path);
        Statement statement = DataBase.getConnectStatement();
        try {
            statement.executeUpdate("TRUNCATE TABLE schools");
            for (School school: schools) {
                statement.executeUpdate("insert into schools (region, city, street, name, directorName) " +
                        "VALUES ('" + school.getRegion() +
                        "', '" + school.getCity() +
                        "', '" + school.getStreet() +
                        "', '" + school.getName() +
                        "', '" + school.getDirectorName() + "')");
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeRecord(String[] searchValues, String[] newValues) {
        ArrayList<School> schools = readXml(path);

        for (int i = 0; i < schools.size(); i++) {
            if (containsField(schools.get(i), searchValues)) {
                for (int j = 1; j < newValues.length; j++)
                    schools.set(i, updateSchool(newValues[j], j, schools.get(i)));
            }
        }
        writeToXml(path, schools);
    }

    // update field in given school according entered new value
    private School updateSchool(String newValue, int valueNum, School oldSchool) {
        try {
            switch (valueNum) {
                case 1 -> {
                    return new School(newValue, oldSchool.getCity(),
                            oldSchool.getStreet(), oldSchool.getName(), oldSchool.getDirectorName());
                }
                case 2 -> {
                    return new School(oldSchool.getRegion(), newValue,
                            oldSchool.getStreet(), oldSchool.getName(), oldSchool.getDirectorName());
                }
                case 3 -> {
                    return new School(oldSchool.getRegion(), oldSchool.getCity(),
                            newValue, oldSchool.getName(), oldSchool.getDirectorName());
                }
                case 4 -> {
                    return new School(oldSchool.getRegion(), oldSchool.getCity(),
                            oldSchool.getStreet(), newValue, oldSchool.getDirectorName());
                }
                case 5 -> {
                    return new School(oldSchool.getRegion(), oldSchool.getCity(),
                            oldSchool.getStreet(), oldSchool.getName(), newValue);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    //is current school contains given fields?
    private boolean containsField(School school, String[] parameters){
        boolean[] isContain = new boolean[parameters.length];
        IntStream.range(0, parameters.length)
                .filter(i -> parameters[i] == null)
                .forEachOrdered(i -> isContain[i] = true);

        for(String parameter : parameters){
            if(school.getRegion().equals(parameter))
                isContain[0] = true;
            else if(school.getCity().equals(parameter))
                isContain[1] = true;
            else if(school.getStreet().equals(parameter))
                isContain[2] = true;
            else if(school.getName().equals(parameter))
                isContain[3] = true;
            else if(school.getDirectorName().equals(parameter))
                isContain[4] = true;
        }
        return isContain[0] && isContain[1] && isContain[2] && isContain[3] && isContain[4];
    }

    public void removeRecord(String[] searchValues) {
        ArrayList<School> schools = readXml(path);

        for (int i = 0; i < schools.size(); i++) {
            if (containsField(schools.get(i), searchValues)) {
                schools.remove(i);
                i = 0;
            }
        }
        writeToXml(path, schools);
    }


}
