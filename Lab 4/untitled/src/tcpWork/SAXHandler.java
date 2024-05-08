package tcpWork;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class SAXHandler extends DefaultHandler {
    private FileWriter writer;
    private StringBuilder data;
    private boolean isMetroCard;
    private boolean isUser;
    private boolean isFirstElement;

    public SAXHandler(String filename) {
        try {
            writer = new FileWriter(filename);
            data = new StringBuilder();
            isMetroCard = false;
            isUser = false;
            isFirstElement = true;
            startDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startDocument() {
        try {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<data>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void endDocument() {
        try {
            writer.write("</data>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        data.setLength(0);
        if (isFirstElement) {
            isFirstElement = false;
            return;
        }
        if (qName.equalsIgnoreCase("MetroCard")) {
            isMetroCard = true;
        } else if (qName.equalsIgnoreCase("User")) {
            isUser = true;
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) {
        try {
            if (isMetroCard) {
                writer.write("<" + qName + ">" + data.toString().trim() + "</" + qName + ">\n");
                isMetroCard = false;
            } else if (isUser) {
                writer.write("<" + qName + ">" + data.toString().trim() + "</" + qName + ">\n");
                isUser = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) {
        data.append(new String(ch, start, length));
    }
    public void writeMetroCard(MetroCard card) {
        try {
            writer.write("<MetroCard>\n");
            writer.write("<serNum>" + card.getSerNum() + "</serNum>\n");
            writer.write("<usr>" + card.getUsr() + "</usr>\n");
            writer.write("<colledge>" + card.getColledge() + "</colledge>\n");
            writer.write("<balance>" + card.getBalance() + "</balance>\n");
            writer.write("</MetroCard>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeUser(User user) {
        try {
            writer.write("<User>\n");
            writer.write("<name>" + user.getName() + "</name>\n");
            writer.write("<surName>" + user.getSurName() + "</surName>\n");
            writer.write("<sex>" + user.getSex() + "</sex>\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String birthdayString = dateFormat.format(user.getBirthday());
            writer.write("<birthday>" + birthdayString + "</birthday>\n");
            writer.write("</User>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}