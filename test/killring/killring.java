import java.util.*;

public class killring {
	private static int[] GetRing(int num,int leftnum) {
		int[] tmpring,kring;
		int i;
		int curnum=num;
		kring = new int[curnum];
		for (i=0;i<num;i++){
			kring[i] = (i+1);
		}

		while(curnum > leftnum) {
			if ((curnum % 2) == 0) {
				tmpring = new int[curnum/2];
			} else {
				tmpring = new int[(curnum/2)+1];
			}
			for (i=0;i<curnum;i++){
				if ((i%2) == 0 ) {
					tmpring[i/2] = kring[i];
				}
			}

			kring = tmpring;
			curnum = kring.length;
		}

		return kring;
	}
	public static void main(String args[]){
		int[] leftarr;
		int number,leftnum=2;
		int i;
		if (args.length < 1) {
			System.err.printf("killring num\n");
			System.exit(3);
		}

		number = Integer.parseInt(args[0]);
		if (args.length > 1) {
			leftnum = Integer.parseInt(args[1]);
		}

		leftarr = GetRing(number,leftnum);
		if (leftarr != null) {
			i = 0;
			for (i=0;i<leftarr.length;i++){
				System.out.printf("[%d] %d\n",i,leftarr[i]);
			}
		}
		return;
	}
}