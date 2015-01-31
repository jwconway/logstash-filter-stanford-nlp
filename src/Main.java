import com.google.gson.Gson;

/**
 * Created by james.conway on 30/01/2015.
 */

public class Main {

    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            uk.co.jaywayco.Parser parser = new uk.co.jaywayco.Parser();
            System.out.print(gson.toJson(parser.processLine("I am a sentence")));
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
}
