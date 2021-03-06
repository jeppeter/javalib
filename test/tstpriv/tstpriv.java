
import callpkg.*;

class Dog {
    private String name;
    private int age;

    public Dog(String sname, int sage) {
        this.name = sname;
        this.age = sage;
    }

    public int Age() {
        return this.age;
    }

    public String Name() {
        return this.name;
    }

    protected void finalize() {
        this.name = "";
        this.age = -1;
    }
}

class Demo {
    void test() {
        System.out.println("No parameters");
    }
    void test(int a) {
        System.out.println("a: " + a);
    }
    void test(int a, int b) {
        System.out.println("a and b: " + a + " " + b);
    }
    double test(double a) {
        System.out.println("double a: " + a);
        return a * a;
    }
}

public class tstpriv {
    public static void main(String args[]) {
        Dog mydog = new Dog("jim", 3);
        Demo objs = new Demo();
        ObjectName objc = new ObjectName("cc");
        objs.test();
        objs.test(2);
        objs.test(2, 3);
        objs.test(2.0);
        System.out.printf("mydog %s age %d\n", mydog.Name(), mydog.Age());
        System.out.printf("object %s\n",objc.Name());
        return;
    }
}