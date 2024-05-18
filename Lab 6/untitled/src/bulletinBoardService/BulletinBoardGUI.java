package bulletinBoardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Proxy;
import java.net.InetAddress;

public class BulletinBoardGUI {
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton clearButton;
    private JButton exitButton;
    private JTextField groupField;
    private JTextField portField;
    private JTextField nameField;
    private Messanger messenger;
    private InetAddress addr;
    private int port;
    private String name;

    public BulletinBoardGUI() {
        frame = new JFrame("Bulletin Board Service");

        messageArea = new JTextArea(20, 50);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        inputField = new JTextField(40);
        sendButton = new JButton("Send");

        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");

        groupField = new JTextField(15);
        portField = new JTextField(5);
        nameField = new JTextField(10);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());

        topPanel.add(new JLabel("Group:"));
        topPanel.add(groupField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(connectButton);
        topPanel.add(disconnectButton);

        bottomPanel.add(inputField);
        bottomPanel.add(sendButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(exitButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (messenger != null) {
                    messenger.send();
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageArea.setText("");
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    private void connect() {
        try {
            name = nameField.getText();
            addr = InetAddress.getByName(groupField.getText());
            port = Integer.parseInt(portField.getText());

            UITasks uiTasks = new UITasksImpl(inputField, messageArea);
            EDTInvocationHandler handler = new EDTInvocationHandler(uiTasks);
            UITasks proxyUITasks = (UITasks) Proxy.newProxyInstance(
                    UITasks.class.getClassLoader(),
                    new Class[]{UITasks.class},
                    handler
            );
            messenger = new MessangerImpl(addr, port, name, proxyUITasks);
            messenger.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void disconnect() {
        if (messenger != null) {
            messenger.stop();
            messenger = null;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BulletinBoardGUI::new);
    }
}
