package sample;


public class School {
    private String region;
    private String city;
    private String street;
    private String name;
    private String directorName;

    public School(String _region, String _city, String _street, String _name, String _directorName){
        this.region = _region;
        this.city = _city;
        this.street = _street;
        this.name = _name;
        this.directorName = _directorName;
    }

    public String getRegion(){
        return region;
    }
    public String getCity(){
        return city;
    }
    public String getStreet(){
        return street;
    }
    public String getName(){
        return name;
    }
    public String getDirectorName(){
        return directorName;
    }

    public static School addSchoolFromConsole(){
        return new School(InputController.getStrFromCon("Enter Region:"),
                InputController.getStrFromCon("Enter City:"),
                InputController.getStrFromCon("Enter Street:"),
                InputController.getStrFromCon("Enter Name:"),
                InputController.getStrFromCon("Enter Director:"));
    }
}

