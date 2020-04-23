import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CheckoutXmlParser {

    private final String filename;
    public List<Checkout> checkouts;

    public CheckoutXmlParser(String filename) {
        this.filename = filename;
        this.checkouts = new ArrayList<>();
        readXML();
    }

    public void readXML() {
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("Borrowed_by");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    int member_id;
                    String isbn, checkout_date, checkin_date;

                    Element sectionNode = (Element) fstNode;

                    NodeList memberIdElementList = sectionNode.getElementsByTagName("MemberID");
                    Element memberIdElmnt = (Element) memberIdElementList.item(0);
                    NodeList memberIdNodeList = memberIdElmnt.getChildNodes();
                    member_id = Integer.parseInt(((Node) memberIdNodeList.item(0)).getNodeValue().trim());

                    NodeList secnoElementList = sectionNode.getElementsByTagName("ISBN");
                    Element secnoElmnt = (Element) secnoElementList.item(0);
                    NodeList secno = secnoElmnt.getChildNodes();
                    isbn = ((Node) secno.item(0)).getNodeValue().trim();

                    NodeList codateElementList = sectionNode.getElementsByTagName("Checkout_date");
                    Element codElmnt = (Element) codateElementList.item(0);
                    NodeList cod = codElmnt.getChildNodes();
                    checkout_date = ((Node) cod.item(0)).getNodeValue().trim();

                    NodeList cidateElementList = sectionNode.getElementsByTagName("Checkin_date");
                    Element cidElmnt = (Element) cidateElementList.item(0);
                    NodeList cid = cidElmnt.getChildNodes();
                    checkin_date = ((Node) cid.item(0)).getNodeValue().trim();

                    if (isInvalidDateFormat(checkout_date)) {
                        checkout_date = null;
                    }

                    if (isInvalidDateFormat(checkin_date)) {
                        checkin_date = null;
                    }

                    checkouts.add(new Checkout(member_id, isbn, checkout_date, checkin_date));
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInvalidDateFormat(String value) {
        Pattern format = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        Matcher matcher = format.matcher(value);
        return !matcher.matches();
    }

}//end class