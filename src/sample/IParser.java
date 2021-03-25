package sample;

public interface IParser {
     void parseRecords();
     void addRecord(String[] values);
     void changeRecord(String[] searchValues, String[] newValues);
     void removeRecord(String[] searchValues);
}
