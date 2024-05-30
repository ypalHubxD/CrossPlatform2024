package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import participant.Participant;
import registerable.Registerable;

public class RMIClient {
    private JFrame frame;
    private JTextField textFieldHost;
    private JTextField textFieldPort;
    private JTextField textFieldName;
    private JTextField textFieldFamily;
    private JTextField textFieldOrganization;
    private JTextField textFieldReport;
    private JTextField textFieldEmail;
    private JTextArea textAreaInfo;
    private JScrollPane scrollPane;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RMIClient::new);
    }
    public RMIClient() {
        initialize();
        textFieldHost.setText("localhost");
        textFieldPort.setText("1049");
    }
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("client.RMIClient");
        frame.setBounds(100, 100, 450, 600);
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
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 70, 80, 25);
        frame.getContentPane().add(lblName);
        textFieldName = new JTextField();
        textFieldName.setBounds(100, 70, 160, 25);
        frame.getContentPane().add(textFieldName);
        textFieldName.setColumns(10);
        JLabel lblFamilyName = new JLabel("Family:");
        lblFamilyName.setBounds(10, 100, 80, 25);
        frame.getContentPane().add(lblFamilyName);
        textFieldFamily = new JTextField();
        textFieldFamily.setBounds(100, 100, 160, 25);
        frame.getContentPane().add(textFieldFamily);
        textFieldFamily.setColumns(10);
        JLabel lblPlaceOfWork = new JLabel("Organization:");
        lblPlaceOfWork.setBounds(10, 130, 80, 25);
        frame.getContentPane().add(lblPlaceOfWork);
        textFieldOrganization = new JTextField();
        textFieldOrganization.setBounds(100, 130, 160, 25);
        frame.getContentPane().add(textFieldOrganization);
        textFieldOrganization.setColumns(10);
        JLabel lblReportTitle = new JLabel("Report:");
        lblReportTitle.setBounds(10, 160, 80, 25);
        frame.getContentPane().add(lblReportTitle);
        textFieldReport = new JTextField();
        textFieldReport.setBounds(100, 160, 160, 25);
        frame.getContentPane().add(textFieldReport);
        textFieldReport.setColumns(10);
        JLabel lblEmail = new JLabel("e-mail:");
        lblEmail.setBounds(10, 190, 80, 25);
        frame.getContentPane().add(lblEmail);
        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(100, 190, 160, 25);
        frame.getContentPane().add(textFieldEmail);
        textFieldEmail.setColumns(10);
        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(10, 220, 250, 25);
        frame.getContentPane().add(btnRegister);
        JButton btnGetInfo = new JButton("Get Info");
        btnGetInfo.setBounds(10, 250, 250, 25);
        frame.getContentPane().add(btnGetInfo);
        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(10, 280, 250, 25);
        frame.getContentPane().add(btnClear);
        JButton btnFinish = new JButton("Finish");
        btnFinish.setBounds(10, 310, 250, 25);
        frame.getContentPane().add(btnFinish);
        textAreaInfo = new JTextArea();
        textAreaInfo.setEditable(false);
        scrollPane = new JScrollPane(textAreaInfo);
        scrollPane.setBounds(10, 340, 400, 200);
        frame.getContentPane().add(scrollPane);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String host = textFieldHost.getText();
                    int port = Integer.parseInt(textFieldPort.getText());
                    Registry registry = LocateRegistry.getRegistry(host, port);
                    Registerable comp = (Registerable) registry.lookup("registratible.Registerable");
                    Participant participant = new Participant(
                            textFieldName.getText(),
                            textFieldFamily.getText(),
                            textFieldOrganization.getText(),
                            textFieldReport.getText(),
                            textFieldEmail.getText()
                    );
                    comp.registerParticipant(participant);
                    JOptionPane.showMessageDialog(frame, "Registered successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        btnGetInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String host = textFieldHost.getText();
                    int port = Integer.parseInt(textFieldPort.getText());
                    Registry registry = LocateRegistry.getRegistry(host, port);
                    Registerable comp = (Registerable) registry.lookup("registratible.Registerable");
                    String info = comp.getParticipants();
                    textAreaInfo.setText(info);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldName.setText("");
                textFieldFamily.setText("");
                textFieldOrganization.setText("");
                textFieldReport.setText("");
                textFieldEmail.setText("");
            }
        });
        btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}
