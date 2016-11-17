
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class getanno {
	public static void main(String[] args)  throws Exception {
		for(String c : args) {
			ClassLoader clsloader = getanno.class.getClassLoader();
			Class ldcls = clsloader.loadClass(c);
			Annotation[] annos = ldcls.getDeclaredAnnotations();
			Field[] fields = ldcls.getDeclaredFields();
			int i;
			System.out.printf("%s annos :",c);
			i = 0;
			for (Annotation a : annos) {
				if ((i % 5) == 0) {
					System.out.printf("\n    ");
				}
				System.out.printf(" %s",a.toString());
				i ++ ;
			}
			System.out.printf("\n");

			System.out.printf("%s fields :",c);
			i = 0;
			for (Field f : fields) {
				if ((i % 5) == 0) {
					System.out.printf("\n    ");
				}
				System.out.printf(" %s",f.getName());
				i ++;
			}
			System.out.printf("\n");
		}
	}
}