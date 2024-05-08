package tcpWork;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class Client {
    private int port = -1;
    private String server = null;
    private Socket socket = null;
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private ArrayList<MetroCard> metroCards;
    private ArrayList<User> users;
    public Client(String server, int port) {
        this.port = port;
        this.server = server;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server, port), 1000);
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (InterruptedIOException e) {
            System.out.println("Error: " + e);
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
    public void setMetroCards(ArrayList<MetroCard> metroCards) {
        this.metroCards = metroCards;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public ArrayList<MetroCard> getAllCards() {
        ArrayList<MetroCard> metroCards = new ArrayList<>();
        try {
            os.writeObject(new GetAllCardsOperation());
            os.flush();
            Object response = is.readObject();
            if (response instanceof ArrayList) {
                metroCards.addAll((ArrayList<MetroCard>) response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e);
        }
        return metroCards;
    }
    public ArrayList<User> getAllUsers() {
        try {
            os.writeObject(new GetAllUsersOperation());
            os.flush();
            Object response = is.readObject();
            if (response instanceof ArrayList) {
                users = (ArrayList<User>) response;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e);
        }
        return users;
    }
    public void finish() {
        try {
            os.writeObject(new StopOperation());
            os.flush();
            System.out.println(is.readObject());
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }
    public void applyOperation(CardOperation op) {
        try {
            os.writeObject(op);
            os.flush();
            System.out.println(is.readObject());
            metroCards = getAllCards();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static void main(String[] args) {
        Client cl = new Client("localhost", 7891);
        Scanner scanner = new Scanner(System.in);
        int choice;
        ArrayList<MetroCard> metroCards = cl.getAllCards();
        ArrayList<User> users = cl.getAllUsers();
        while (true){
            System.out.println("1 - Add Metro Card");
            System.out.println("2 - Add Money to Card");
            System.out.println("3 - Pay Money from Card");
            System.out.println("4 - Show Card Balance");
            System.out.println("5 - Remove Card");
            System.out.println("6 - Show all cards");
            System.out.println("7 - Show all users");
            System.out.println("8 - Save XML file");
            System.out.println("9 - Read XML file");
            System.out.println("0 - Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter card serial number:");
                    String serialNumber = scanner.nextLine();
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Surname: ");
                    String surname = scanner.nextLine();
                    System.out.print("Sex: ");
                    String sex = scanner.nextLine();
                    System.out.print("Birthday (dd.MM.yyyy): ");
                    String birthday = scanner.nextLine();
                    System.out.print("College: ");
                    String college = scanner.nextLine();
                    System.out.print("Balance: ");
                    double balance = scanner.nextDouble();
                    scanner.nextLine();
                    AddMetroCardOperation addMetroCardOperation = new AddMetroCardOperation(
                            serialNumber,
                            new User(name, surname, sex, birthday),
                            college,
                            balance
                    );
                    cl.applyOperation(addMetroCardOperation);
                    break;
                case 2:
                    System.out.println("Enter card serial number:");
                    String cardSerialNumber = scanner.nextLine();
                    System.out.println("Enter amount to add:");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    AddMoneyOperation addMoneyOperation = new AddMoneyOperation(cardSerialNumber, amount);
                    cl.applyOperation(addMoneyOperation);
                    break;
                case 3:
                    System.out.println("Enter card serial number:");
                    String payMoneySerialNumber = scanner.nextLine();
                    System.out.println("Enter amount to pay:");
                    double payAmount = scanner.nextDouble();
                    scanner.nextLine();
                    PayMoneyOperation payMoneyOperation = new PayMoneyOperation(payMoneySerialNumber, payAmount);
                    cl.applyOperation(payMoneyOperation);
                    break;
                case 4:
                    System.out.println("Enter card serial number:");
                    String showBalanceSerialNumber = scanner.nextLine();
                    ShowBalanceOperation showBalanceOperation = new ShowBalanceOperation(showBalanceSerialNumber);
                    cl.applyOperation(showBalanceOperation);
                    break;
                case 5:
                    System.out.println("Enter card serial number to remove:");
                    String removeCardSerialNumber = scanner.nextLine();
                    RemoveCardOperation removeCardOperation = new RemoveCardOperation(removeCardSerialNumber);
                    cl.applyOperation(removeCardOperation);
                    break;
                case 6:
                    for (MetroCard card : metroCards) {
                        System.out.println(card);
                    }
                    break;
                case 7:
                    for (User user : users) {
                        System.out.println(user);
                    }
                    break;
                case 8:
                    try {
                        String filename = "file.xml";
                        SAXHandler handler = new SAXHandler(filename);
                        metroCards = cl.getAllCards();
                        users = cl.getAllUsers();
                        for (MetroCard card : metroCards) {
                            handler.writeMetroCard(card);
                        }
                        for (User user : users) {
                            handler.writeUser(user);
                        }
                        handler.endDocument();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    try {
                        String xmlFile = "file.xml";
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser saxParser = factory.newSAXParser();
                        MetroCardSAXHandler handler = new MetroCardSAXHandler();
                        saxParser.parse(new File(xmlFile), handler);
                        metroCards = handler.getMetroCards();
                        users = handler.getUsers();
                        System.out.println("Data loaded successfully.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid operation.");
            }
        }
    }
}