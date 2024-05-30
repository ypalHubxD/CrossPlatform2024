package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import participant.ParticipantsStorage;
import registerable.RegisterableImpl;

public class RMIServer {
    private JFrame frame;
    private JTextField textFieldHost;
    private JTextField textFieldPort;
    private JTextArea textAreaParticipants;
    private JLabel labelCount;
    private ParticipantsStorage storage;
    private RegisterableImpl regImpl;
    private Registry registry;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RMIServer::new);
    }
    public RMIServer() {
        storage = new ParticipantsStorage();
        initialize();
        textFieldHost.setText("localhost");
        textFieldPort.setText("1049");
    }
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("server.RMIServer");
        frame.setBounds(100, 100, 450, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        JLabel lblHost = new JLabel("Host:");
        lblHost.setBounds(10, 10, 80, 25);
        frame.getContentPane().add(lblHost);
        textFieldHost = new JTextField();
        textFieldHost.setBounds(100, 10, 160, 25);
        frame.getContentPane().add(textFieldHost);
        textFieldHost.setColumns(10);
        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(10, 40, 80, 25);
        frame.getContentPane().add(lblPort);
        textFieldPort = new JTextField();
        textFieldPort.setBounds(100, 40, 160, 25);
        frame.getContentPane().add(textFieldPort);
        textFieldPort.setColumns(10);
        JButton btnStart = new JButton("Start Server");
        btnStart.setBounds(10, 70, 250, 25);
        frame.getContentPane().add(btnStart);
        JButton btnStop = new JButton("Stop Server");
        btnStop.setBounds(10, 100, 250, 25);
        frame.getContentPane().add(btnStop);
        JButton btnSave = new JButton("Save to XML");
        btnSave.setBounds(10, 130, 250, 25);
        frame.getContentPane().add(btnSave);
        JButton btnLoad = new JButton("Load from XML");
        btnLoad.setBounds(10, 160, 250, 25);
        frame.getContentPane().add(btnLoad);
        JLabel lblParticipants = new JLabel("Participants:");
        lblParticipants.setBounds(10, 190, 100, 25);
        frame.getContentPane().add(lblParticipants);
        textAreaParticipants = new JTextArea();
        textAreaParticipants.setBounds(10, 220, 400, 150);
        frame.getContentPane().add(textAreaParticipants);
        labelCount = new JLabel("Count: 0");
        labelCount.setBounds(10, 350, 100, 25);
        frame.getContentPane().add(labelCount);
        storage.addPropertyChangeListener(evt -> {
            textAreaParticipants.setText(storage.toString());
            labelCount.setText("Count: " + storage.getParticipants().size());
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String host = textFieldHost.getText();
                    int port = Integer.parseInt(textFieldPort.getText());
                    regImpl = new RegisterableImpl(storage);
                    registry = LocateRegistry.createRegistry(port);
                    registry.rebind("registratible.Registerable", regImpl);
                    JOptionPane.showMessageDialog(frame, "Server started on port " + port);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (registry != null) {
                        registry.unbind("registratible.Registerable");
                        UnicastRemoteObject.unexportObject(regImpl, true);
                        JOptionPane.showMessageDialog(frame, "Server stopped.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    storage.saveToFile("participants.xml");
                    JOptionPane.showMessageDialog(frame, "Saved to participants.xml");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    storage.loadFromFile("participants.xml");
                    JOptionPane.showMessageDialog(frame, "Loaded from participants.xml");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        frame.setVisible(true);
    }
}
