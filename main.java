import java.util.Scanner;


public class main {

    private static final double EPSILON = 1e-10;
	
	public static void main(String[] args) {
		
		System.out.println("1) Generate sub-passwords **IN PROGRESS**");
		System.out.println("2) Find password");
		System.out.print("Enter number of task you'd like performed: ");
		
		Scanner input = new Scanner(System.in);
		
		if(input.nextInt() == 1) generateSubs();
		else findPassword(); 
		
	}

	public static void generateSubs() {

		char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter (at most) 4-digit password: ");
		int pw = in.nextInt();
		System.out.print("How many sub-passwords would you like (8 max)? ");
		int num = in.nextInt();
		
		double[][] points = new double[num][2];
		points[0][0] = 0;
		points[0][1] = pw;
		
		for(int i = 1; i < num; i++) {
			points[i][0] = (int) (Math.random()*100);
			points[i][1] = (int) (Math.random()*100);
		}

		double[][] A = new double[points.length][points.length];
		double[] b = new double[points.length];

		for(int i = 0; i < A.length; i++) {
			for(int j = 0; j < A[0].length; j++) {
				A[i][j] = Math.pow(points[i][0], A.length - j - 1);
			}
		}
		
		for(int i = 0; i < b.length; i++) {
			b[i] = points[i][1];
		}

		Scanner input = new Scanner(System.in);
		for(int i = 1; i < points.length; i++) {
			System.out.println("(" + points[i][0] + ", " + points[i][1] + ")");
			System.out.print("Press ENTER for next sub-password");
			if(input.nextLine().equals("")) {
				continue;
			}
		}
		System.out.println("(" + points[0][0] + ", " + points[0][1] + ")");
		
	}

	public static void findPassword() {
		
		Scanner in = new Scanner(System.in);
		System.out.print("How many sub-passwords do you have? ");
		int num = in.nextInt();
		
		int[][] subArray = new int[num][2];
		double[][] A = new double[num][num];
		double[] b = new double[num];
		
		for(int i = 0; i < num; i++) {
			System.out.print("Enter 1st number of sub-password #" + (i+1) + " : ");
			subArray[i][0] = in.nextInt();
			System.out.print("Enter 2nd number of sub-password #" + (i+1) + " : ");
			b[i] = in.nextInt();
		}

		for(int i = 0; i < num; i++) 
			for(int j = 0; j < num; j++) 
				A[i][j] = Math.pow(subArray[i][0], num - j - 1);

		double[] x = lsolve(A, b);
		
		System.out.println("==========");
		System.out.println("Password: " + (int) x[x.length - 1]);

		
	}
	
    public static double[] lsolve(double[][] A, double[] b) {

    	int N  = b.length;

        for (int p = 0; p < N; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new RuntimeException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;

    }
	
}
