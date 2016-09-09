
import java.lang.reflect.Method;
import java.lang.Class;

public class ClassWarning {
    void m() {
	try {
	    Class c = ClassWarning.class;
	    Class<?>[] params = new Class<void>();
	    Method m = c.getMethod("m",params);  // warning

        // production code should handle this exception more gracefully
	} catch (NoSuchMethodException x) {
    	    x.printStackTrace();
    	}
    }
}