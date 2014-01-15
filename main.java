import java.util.Scanner;


public class main {

	public static void main(String[] args) {

		System.out.println("1) Generate sub-passwords");
		System.out.println("2) Find password");
		System.out.print("Enter number of task you'd like performed: ");
		
		Scanner input = new Scanner(System.in);
		
		if(input.nextInt() == 1) generateSubs();
		else findPassword();
		
	}

	public static void generateSubs() {

		char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter 4-digit password: ");
		int pw = in.nextInt();
		System.out.print("How many sub-passwords would you like (8 max)? ");
		int num = in.nextInt();
		
		double[][] points = new double[num][2];
		points[0][0] = 0;
		points[0][1] = pw;
		
		for(int i = 1; i < num; i++) {
			points[i][0] = (int) (Math.random()*50);
			points[i][1] = (int) (Math.random()*1000);
		}

		double[] sol = getCoeff(pw, num, points);
		
		for(int i = 0; i < sol.length; i++) {
			System.out.println(alphabet[i] + " = " + sol[i]);
		}
		System.out.println(alphabet[sol.length] + " = " + pw);

		int x = (int) (Math.random()*100);
		double y = 0;
		for(int i = 0; i < sol.length; i++) {
			y += Math.pow(x, sol.length - i)*sol[i];
		}

/*
		while( (y - (int) y) != 0 ) {
			x = (int) (Math.random()*100);
			y = 0;
			for(int i = 0; i < sol.length; i++) {
				y += Math.pow(x, sol.length - i)*sol[i];
			}
		}
*/
		
		points[0][0] = x;
		points[0][1] = y;

		System.out.println("========");
		for(int i = 0; i < num; i++) {
			System.out.println("x = " + points[i][0] + "\ty = " + points[i][1]);
		}
		
	}

	public static double[] getCoeff(int pw, int num, double[][] points) {
		
		double[][] A = new double[num - 1][num - 1];
		double[] b = new double[num - 1];

		for(int i = 0; i < A.length; i++) {
			for(int j = 0; j < A[0].length; j++) {
				A[i][j] = Math.pow(points[i+1][0], A.length - j);
			}
		}
		
		for(int i = 0; i < b.length; i++) {
			b[i] = points[i+1][1] - pw;
		}

		double[][] result = new double[num-1][num-1];
		for(int m = 0; m < num-1; m++)
			for(int n = 0; n < num-1; n++)
				result[m][n] = A[m][n];
		
		double[] sol = new double[num-1];
		for(int i = 0; i < num-1; i++)
			sol[i] = b[i];
		
		for(int a = 0, d = 0; a < num - 2 && d < num - 2; a++){ // iter thru pivots

			while(d < num - 2 && result[a][d]==0) {
				d++;
			}
			
			for(int j = a + 1; j < num - 1; j++){ // iter thru rows below pivot
				
				double ratio = A[j][d]/A[a][d];
					
				for(int k = d; k < num - 1; k++){ // iter thru columns
					
					result[j][k] = A[j][k] - A[a][k]*ratio;
					A[j][k] = result[j][k];
						
				}
				
				sol[j] = b[j] - b[a]*ratio;
				b[j] = sol[j];
				
			}
			
			d = 0;
		}
		
		// divide by pivots
		for(int a = 0, d = 0; a < num - 1 && d < num - 1; a++){
					
			while(d < num - 1 && result[a][d] == 0){
				d++;
			}
					
			if(d < num - 1){
						
				double pivot = result[a][d];
					
				sol[a] /= pivot;
				b[a] /= pivot;
					
				for(int col = d; col < num - 1; col++){
					result[a][col] /= pivot;
					A[a][col] /= pivot;
				}	
							
			}
					
			d = 0;
		}

		// back-substitution
		for(int row = num - 2, col = 0; row > -1; row--){
			
			while(col < num - 1 && result[row][col] == 0){
				col++;
			}
			
			if(col < num - 1){
				
				for(int otherRow = row - 1; otherRow > -1; otherRow--){

					sol[otherRow] -= result[otherRow][col]*sol[row];
					result[otherRow][col] = 0;
					
				}
				
			}
			
			col = 0;
		}
				
		return sol;

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
		
/*		
		for(int i = 0; i < num; i++) {
			System.out.println("[" + subArray[i][0] + "]\t[" + subArray[i][1] + "]");
		}
*/
		
/*		for(int i = 0; i < num; i++) {
			for(int j = 0; j < num; j++) {
				System.out.print(A[i][j] + "\t");
			}
			System.out.print(b[i]);
			System.out.println();
		}
*/		
		double[][] result = new double[num][num];
		for(int m = 0; m < num; m++)
			for(int n = 0; n < num; n++)
				result[m][n] = A[m][n];
		
		double[] sol = new double[num];
		for(int i = 0; i < num; i++)
			sol[i] = b[i];
		
		for(int a = 0, d = 0; a < num - 1 && d < num - 1; a++){ // iter thru pivots

			while(d < num - 1 && result[a][d]==0) {
				d++;
			}
			
			for(int j = a + 1; j < num; j++){ // iter thru rows below pivot
				
				double ratio = A[j][d]/A[a][d];
					
				for(int k = d; k < num; k++){ // iter thru columns
					
					result[j][k] = A[j][k] - A[a][k]*ratio;
					A[j][k] = result[j][k];
						
				}
				
				sol[j] = b[j] - b[a]*ratio;
				b[j] = sol[j];
				
			}
			
			d = 0;
		}
		
		// divide by pivots
		for(int a = 0, d = 0; a < num && d < num; a++){
					
			while(d < num && result[a][d] == 0){
				d++;
			}
					
			if(d < num){
						
				double pivot = result[a][d];
					
				sol[a] /= pivot;
				b[a] /= pivot;
					
				for(int col = d; col < num; col++){
					result[a][col] /= pivot;
					A[a][col] /= pivot;
				}	
							
			}
					
			d = 0;
		}

		// back-substitution
		for(int row = num - 1, col = 0; row > -1; row--){
			
			while(col < num && result[row][col] == 0){
				col++;
			}
			
			if(col < num){
				
				for(int otherRow = row - 1; otherRow > -1; otherRow--){

					sol[otherRow] -= result[otherRow][col]*sol[row];
					result[otherRow][col] = 0;
					
				}
				
			}
			
			col = 0;
		}
		
		System.out.println("==========");
		System.out.println("Password: " + (int) sol[num - 1]);
		
	}
	
}
