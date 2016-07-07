import java.util.*;

public class killring {
	private static void DebugRing(List<Integer> l) {
		int i;
		System.out.println("size " + l.size());
		for (i = 0; i < l.size(); i++) {
			System.out.println(i + "=" + l.get(i));
		}

	}
	private static int[] GetRing(int num, int leftnum) {
		List<Integer> kring = new ArrayList<Integer>();
		List<Integer> tmpring = new ArrayList<Integer>();
		int i;
		int curnum = num;
		int[] getinte;
		int[] retnums = null;
		for (i = 0; i < num; i++) {
			kring.add((i + 1));
		}

		while (curnum > leftnum) {
			tmpring.clear();
			for (i = 0; i < kring.size() ; i ++) {
				if ((i % 2) == 0) {
					tmpring.add(kring.get(i));
				}
			}

			kring = new ArrayList<Integer>(tmpring);
			curnum = kring.size();
		}

		retnums = new int[kring.size()];

		for (i = 0; i < kring.size(); i++) {
			retnums[i] = kring.get(i).intValue();
		}

		return retnums;
	}
	public static void main(String args[]) {
		int[] leftarr;
		int number, leftnum = 2;
		int i;
		if (args.length < 1) {
			System.err.printf("killring num\n");
			System.exit(3);
		}

		number = Integer.parseInt(args[0]);
		if (args.length > 1) {
			leftnum = Integer.parseInt(args[1]);
		}

		leftarr = GetRing(number, leftnum);
		if (leftarr != null) {
			i = 0;
			for (i = 0; i < leftarr.length; i++) {
				System.out.printf("[%d] %d\n", i, leftarr[i]);
			}
		}
		return;
	}
}