public class Main {

    public static void main(String[] args) {
        //DataReader.generateDumps();
        if (args.length > 0) {
            String libdata = args[0];
            CheckoutXmlParser parser = new CheckoutXmlParser(libdata);
            StatementExecutor.executeStatements(parser.checkouts);
            System.out.println("Program executed successfully.");
        }



    }
}
