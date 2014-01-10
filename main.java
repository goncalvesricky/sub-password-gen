import java.util.Scanner;


public class main {

	public static void main(String[] args) {

		System.out.println("What would you like to do? (Enter #)");
		System.out.println("1) Generate sub-passwords");
		System.out.println("2) Find password");
		
		Scanner input = new Scanner(System.in);
		int ans = input.nextInt();
		
		if(ans == 1) generateSubs();
		else findPassword();
		
	}

	public static void generateSubs() {
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter master password: ");
		String pw = in.next();
		System.out.print("How many sub-passwords would you like? ");
		int num = in.nextInt();
		
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
		for(int i = 0; i < num; i++) {
			for(int j = 0; j < num; j++) {
				System.out.print(A[i][j] + "\t");
			}
			System.out.print(b[i]);
			System.out.println();
		}
		
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
					
				for(int col = d; col < 3; col++){
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
