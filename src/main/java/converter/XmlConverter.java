package converter;

import models.xmlOrder.XMLOrder;
import models.xmlProviderMenu.XmlMenu;
import models.xmlProviderResponse.ProviderResponse;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlConverter {

    private static final Logger log = Logger.getLogger(XmlConverter.class);

    private static Marshaller marshallerXmlOrder;

    private static Unmarshaller unmarshallerXmlProviderResponse;

    private static Transformer transformer;

    public static String xmlToString(XMLOrder xmlOrder) {
        try {
            if (marshallerXmlOrder == null)
                marshallerXmlOrder = JAXBContext.newInstance(XMLOrder.class).createMarshaller();
            if (transformer == null)
                transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter outWriter = new StringWriter();
            marshallerXmlOrder.marshal(xmlOrder, new StreamResult(outWriter));
            return outWriter.getBuffer().toString();
        } catch (TransformerConfigurationException | JAXBException e) {
            log.error("error marshaller xml to string", e);
            return null;
        }
    }

    public static ProviderResponse stringToXml(String xml) {
        try {
            if (unmarshallerXmlProviderResponse == null)
                unmarshallerXmlProviderResponse = JAXBContext.newInstance(ProviderResponse.class).createUnmarshaller();
            StringReader reader = new StringReader(xml);
            return (ProviderResponse) unmarshallerXmlProviderResponse.unmarshal(reader);
        } catch (JAXBException e) {
            log.error("error unmarshaller string to xml", e);
            return null;
        }
    }

}
