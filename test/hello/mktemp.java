import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

public class mktemp {
    public static void main(String[] args)  {
        for (String c : args ) {
            try {
                //create a temp file
                File temp = File.createTempFile(c, ".json");
                FileOutputStream outf;
                System.out.println("Temp file : " + temp.getAbsolutePath());
                outf = new FileOutputStream(temp.getAbsolutePath());
                outf.write("{\"age\":33}".getBytes());
                outf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}