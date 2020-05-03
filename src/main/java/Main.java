public class Main {

    // TODO: Write appropriate usage message (manual)
    private static void printUsageMessage() {
        System.out.println("Description:\n\tRequest file must be an XML file containing only checkin or checkout requests.");
        System.out.println("\tBefore running, system variables SQLUSERNAME and SQLPASSWORD must be set appropriately.\n");
        System.out.println("Usage:\n\t$ java Main <path_to_request_file>");
        System.out.println("\tOR");
        System.out.println("\t$ java Main (-d, --dumps)");
    }

    public static void main(String[] args) {
        //DataReader.generateDumps();
        if (args.length == 1) {
            if (args[0].equals("-d") || args[0].equals("--dumps")) {
                DataReader.generateDumps();
            }
            else if (args[0].equals("-c") || args[0].equals("--checkouts")) {
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
            else {

            }
        }
        else {
            printUsageMessage();
            System.exit(1);
        }

    }
}
