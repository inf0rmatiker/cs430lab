public class Main {

    public static void main(String[] args) {
        //DataReader.generateDumps();
        //StatementExecutor.executeStatements();
        CheckoutXmlParser parser = new CheckoutXmlParser("./data/Libdata.xml");
        for (Checkout c: parser.checkouts) {
            System.out.println(c);
        }

    }
}
