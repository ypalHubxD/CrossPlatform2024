package tcpWork;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MetroCardSAXHandler extends DefaultHandler {
    private ArrayList<MetroCard> metroCards;
    private ArrayList<User> users;
    private StringBuilder data;
    private MetroCard currentMetroCard;
    private User currentUser;
    private String currentElement;

    public MetroCardSAXHandler() {
        metroCards = new ArrayList<>();
        users = new ArrayList<>();
        data = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data.setLength(0);
        currentElement = qName;
        if (qName.equalsIgnoreCase("MetroCard")) {
            currentMetroCard = new MetroCard();
        } else if (qName.equalsIgnoreCase("User")) {
            currentUser = new User();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("MetroCard")) {
            metroCards.add(currentMetroCard);
            currentMetroCard = null;
        } else if (qName.equalsIgnoreCase("User")) {
            users.add(currentUser);
            currentUser = null;
        } else if (qName.equalsIgnoreCase("serNum")) {
            if (currentMetroCard != null) {
                currentMetroCard.setSerNum(data.toString().trim());
            }
        } else if (qName.equalsIgnoreCase("colledge")) {
            if (currentMetroCard != null) {
                currentMetroCard.setColledge(data.toString().trim());
            }
        } else if (qName.equalsIgnoreCase("balance")) {
            if (currentMetroCard != null) {
                currentMetroCard.setBalance(Double.parseDouble(data.toString().trim()));
            }
        } else if (qName.equalsIgnoreCase("name")) {
            if (currentUser != null) {
                currentUser.setName(data.toString().trim());
            }
        } else if (qName.equalsIgnoreCase("surName")) {
            if (currentUser != null) {
                currentUser.setSurName(data.toString().trim());
            }
        } else if (qName.equalsIgnoreCase("sex")) {
            if (currentUser != null) {
                currentUser.setSex(data.toString().trim());
            }
        } else if (qName.equalsIgnoreCase("birthday")) {
            if (currentUser != null) {
                try {
                    DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
                    Date birthday = dateFormatter.parse(data.toString().trim());
                    currentUser.setBirthday(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    public ArrayList<MetroCard> getMetroCards() {
        return metroCards;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
