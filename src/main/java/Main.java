public class Main {

    public static void main(String[] args) {
        //DataReader.generateDumps();
        CheckoutXmlParser parser = new CheckoutXmlParser("./data/Libdata.xml");
        StatementExecutor.executeStatements(parser.checkouts);

    }
}
