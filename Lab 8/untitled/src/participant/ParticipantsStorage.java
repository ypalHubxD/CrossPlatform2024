package participant;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ParticipantsStorage {
    private List<Participant> participants;
    private PropertyChangeSupport support;
    public ParticipantsStorage() {
        participants = new ArrayList<>();
        support = new PropertyChangeSupport(this);
    }
    public synchronized void addParticipant(Participant participant) {
        participants.add(participant);
        support.firePropertyChange("participants", null, participants);
        System.out.println(participant);
    }
    public synchronized List<Participant> getParticipants() {
        return participants;
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Participant participant : participants) {
            sb.append(participant).append("\n");
        }
        return sb.toString();
    }
    public String toXMLString() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("RegisteredConferees");
        doc.appendChild(rootElement);
        for (Participant participant : participants) {
            Element conferee = doc.createElement("Conferee");
            rootElement.appendChild(conferee);
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(participant.getName()));
            conferee.appendChild(name);
            Element family = doc.createElement("family");
            family.appendChild(doc.createTextNode(participant.getFamily()));
            conferee.appendChild(family);
            Element organization = doc.createElement("organization");
            organization.appendChild(doc.createTextNode(participant.getOrganization()));
            conferee.appendChild(organization);
            Element report = doc.createElement("report");
            report.appendChild(doc.createTextNode(participant.getReport()));
            conferee.appendChild(report);
            Element email = doc.createElement("email");
            email.appendChild(doc.createTextNode(participant.getEmail()));
            conferee.appendChild(email);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }
    public void saveToFile(String filePath) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("RegisteredConferees");
        doc.appendChild(rootElement);
        for (Participant participant : participants) {
            Element conferee = doc.createElement("Conferee");
            rootElement.appendChild(conferee);
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(participant.getName()));
            conferee.appendChild(name);
            Element family = doc.createElement("family");
            family.appendChild(doc.createTextNode(participant.getFamily()));
            conferee.appendChild(family);
            Element organization = doc.createElement("organization");
            organization.appendChild(doc.createTextNode(participant.getOrganization()));
            conferee.appendChild(organization);
            Element report = doc.createElement("report");
            report.appendChild(doc.createTextNode(participant.getReport()));
            conferee.appendChild(report);
            Element email = doc.createElement("email");
            email.appendChild(doc.createTextNode(participant.getEmail()));
            conferee.appendChild(email);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
    public void loadFromFile(String filePath) throws Exception {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Conferee");
        participants.clear();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                String family = element.getElementsByTagName("family").item(0).getTextContent();
                String organization = element.getElementsByTagName("organization").item(0).getTextContent();
                String report = element.getElementsByTagName("report").item(0).getTextContent();
                String email = element.getElementsByTagName("email").item(0).getTextContent();
                Participant participant = new Participant(name, family, organization, report, email);
                participants.add(participant);
            }
        }
        support.firePropertyChange("participants", null, participants);
    }
}
