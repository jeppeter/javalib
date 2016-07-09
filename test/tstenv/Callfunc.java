import java.lang.reflect.*;
public class Callfunc {

    public String methodName(int i) {
        return "Hello World: " + i;
    }

    public static void main(String... args) throws Exception {
        Callfunc t = new Callfunc();
        Method m = Callfunc.class.getMethod("methodName", int.class);
        String returnVal = (String) m.invoke(t, 5);
        System.out.println(returnVal);
    }
}