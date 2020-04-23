public class Main {

    public static void main(String[] args) {
        //DataReader.generateDumps();
        if (args.length > 0) {
            String libdata = args[0];
            CheckoutXmlParser parser = new CheckoutXmlParser(libdata);

            if (parser.checkouts.isEmpty()) {
                System.out.println("No checkin/out records to process");
            }
            else {
                StatementExecutor.executeStatements(parser.checkouts);
                System.out.println("Program executed successfully.");
            }
        }



    }
}
