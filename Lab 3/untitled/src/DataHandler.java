import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.List;

public class DataHandler extends DefaultHandler {
    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private boolean isX, isY;
    private double k, b;
    private double SumX, SumY;
    private double SumXX, SumXY;
    private double AvrX, AvrY;

    public static void main(String[] args) {
        if (validateXMLSchema("data.xsd", "data.xml")) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = factory.newSAXParser();
                DefaultHandler handler = new DataHandler();

                InputStream xmlInput = new FileInputStream("data.xml");

                saxParser.parse(xmlInput, handler);
            } catch (SAXException | ParserConfigurationException | IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The XML document does not conform to the XSD schema");
        }
    }
    public void startDocument() throws SAXException {
        System.out.println("Start Document Parsing Process ...");
        SumX = 0;
        SumY = 0;
        AvrX = 0;
        AvrY = 0;
    }
    public void endDocument() throws SAXException {
        System.out.println("End Document Parsing Process ...");
        int i;
        for (i = 0; i < x.size(); i++) {
            System.out.println("date: " + dates.get(i));
            System.out.println("x: " + x.get(i));
            System.out.println("y: " + y.get(i));

            SumX += x.get(i);
            SumY += y.get(i);
            SumXX += x.get(i) * x.get(i);
            SumXY += x.get(i) * y.get(i);
        }
        AvrX = SumX / x.size();
        AvrY = SumY / y.size();

        k = ((SumXY - (SumX * AvrY)) / (SumXX - (SumX * AvrX)));
        b = AvrY - (k * AvrX);

        System.out.println("k: " + k);
        System.out.println("b: " + b);
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("Analyser");
            doc.appendChild(rootElement);

            Element dataTable = doc.createElement("dataTable");
            rootElement.appendChild(dataTable);

            for (i = 0; i < dates.size(); i++) {
                Element dataPoint = doc.createElement("dataPoint");
                dataPoint.setAttribute("date", dates.get(i));

                Element xElement = doc.createElement("x");
                xElement.appendChild(doc.createTextNode(String.valueOf(x.get(i))));
                dataPoint.appendChild(xElement);

                Element yElement = doc.createElement("y");
                yElement.appendChild(doc.createTextNode(String.valueOf(y.get(i))));
                dataPoint.appendChild(yElement);

                dataTable.appendChild(dataPoint);
            }

            Element line = doc.createElement("line");
            line.setAttribute("b", String.valueOf(b));
            line.setAttribute("k", String.valueOf(k));
            rootElement.appendChild(line);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("newdata.xml"));

            transformer.transform(source, result);

            System.out.println("XML file saved successfully");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
    public void startElement(String uri, String name, String qName, Attributes attrs) throws SAXException {
        if (qName.equals("x")) {
            isX = true;
        } else if (qName.equals("y")) {
            isY = true;
        }
        if (attrs.getLength() > 0 && attrs.getValue("date") != null) {
            dates.add(attrs.getValue("date"));
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("x")) {
            isX = false;
        } else if (qName.equals("y")) {
            isY = false;
        }
    }
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length).trim();
        double tmp = 0;
        if (isX) {
            tmp = Double.parseDouble(str);
            x.add(tmp);
        } else if (isY) {
            tmp = Double.parseDouble(str);
            y.add(tmp);
        }
    }
    public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlPath));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }
}
