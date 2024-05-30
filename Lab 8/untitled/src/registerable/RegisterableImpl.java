package registerable;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.StringWriter;

import participant.Participant;
import participant.ParticipantsStorage;

public class RegisterableImpl extends UnicastRemoteObject implements Registerable {
    private ParticipantsStorage storage;

    public RegisterableImpl(ParticipantsStorage storage) throws RemoteException {
        super();
        this.storage = storage;
    }
    @Override
    public synchronized void registerParticipant(Participant participant) throws RemoteException {
        storage.addParticipant(participant);
        System.out.println(participant);
    }
    @Override
    public synchronized String getParticipants() throws RemoteException {
        List<Participant> participants = storage.getParticipants();
        return convertParticipantsToXML(participants);
    }
    private String convertParticipantsToXML(List<Participant> participants) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("RegisteredConferees");
            doc.appendChild(rootElement);
            for (Participant p : participants) {
                Element conferee = doc.createElement("Conferee");
                rootElement.appendChild(conferee);
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(p.getName()));
                conferee.appendChild(name);
                Element family = doc.createElement("family");
                family.appendChild(doc.createTextNode(p.getFamily()));
                conferee.appendChild(family);
                Element organization = doc.createElement("organization");
                organization.appendChild(doc.createTextNode(p.getOrganization()));
                conferee.appendChild(organization);
                Element report = doc.createElement("report");
                report.appendChild(doc.createTextNode(p.getReport()));
                conferee.appendChild(report);
                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(p.getEmail()));
                conferee.appendChild(email);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "Windows-1251");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
