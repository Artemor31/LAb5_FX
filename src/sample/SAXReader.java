package sample;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAXReader {

    public static ArrayList<School> read(String path) {
        ArrayList<School> schools = new ArrayList<>();
        String[] str = new String[5];
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                String tag = "";
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    tag = qName;
                }
                // Метод вызывается когда SAXParser считывает текст между тегами
                @Override
                public void characters(char[] ch, int start, int length) {

                    if (tag.equalsIgnoreCase("region")) {
                        str[0] = new String(ch, start, length);
                    }
                    else if (tag.equalsIgnoreCase("city")) {
                        str[1] = new String(ch, start, length);
                    }
                    else if (tag.equalsIgnoreCase("street")) {
                        str[2] = new String(ch, start, length);
                    }
                    else if (tag.equalsIgnoreCase("name")) {
                        str[3] = new String(ch, start, length);
                    }
                    else if (tag.equalsIgnoreCase("directorName")) {
                        str[4] = new String(ch, start, length);
                    }
                    if(str[0] != null && str[1] != null && str[2] != null &&
                            str[3] != null && str[4] != null) {

                        School school = new School(str[0], str[1], str[2], str[3], str[4]);
                        Arrays.fill(str, null);
                        schools.add(school);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {tag = "";}

                public String[] resetStrings(){
                    return new String[5];
                }
            };
            saxParser.parse(path, handler);
        } catch (Exception e) {
            System.out.println("Nice file, awesome text");
            e.printStackTrace();
        }
        return schools;
    }

}



