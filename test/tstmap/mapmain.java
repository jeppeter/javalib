

import java.util.HashMap;

public class mapmain {
	public static void main(String... args) {
		int i;
		HashMap<String,Object> map = new HashMap<String,Object>();

		for (i=0;i<args.length;i+=2) {
			map.put(args[i],args[i+1]);
		}

		for (String k : map.keySet()) {
			System.out.printf("%s=%s(%s)\n",k,map.get(k),map.get(k).getClass().getName());
		}
		return;
	}
}