import java.lang.Math;
import java.util.Scanner;

public class Cipher {
	private String txt;
	private final String startChar = "$", spaceChar = "%";
	private final long aVal = 250, bVal = 658326;
	/*
	 * $ indicates the start of a character
	 * % indicates a space
	 */

	public Cipher(String text) {
		txt = text;
	}

	public boolean isEncrypted() {
		if (txt.substring(0, 1).equals(startChar)) {
			return true;
		} else {
			return false;
		}
	}

	public String cipher() {
		if (isEncrypted() == true) {
			return decryptTxt();
		} else {
			return encryptTxt();
		}
	}

	public String encryptTxt() {
		String[] encTxtArr = txt.split(" ");
		long charVal = 0;
		String encTxt = "";

		for (int i = 0; i < encTxtArr.length; i++) {
			for (int j = 0; j < encTxtArr[i].length(); j++) {
				charVal = (long)encTxtArr[i].charAt(j); // get ASCII code for jth char in encTxt[j] and convert it to long
				encTxt += startChar; // add start character
				/*
        		 * f(x) = ϕ(x)^2 + σx
        		 * plugging in any ASCII code in f(x) will return the encrypted ASCII code
        		 * give that the ϕ and σ are provided
        		 * where ϕ and σ are any arbitrary numbers (the larger the values of ϕ and σ are, the harder it is to break the encrypted code).
        		 */
				encTxt += ( aVal * (long)(Math.pow(charVal, 2)) ) + (bVal * charVal); // f(x) = ϕ(x)^2 + σx
			}
			encTxtArr[i] = encTxt;
			encTxt = "";
		}
		return getOutput(encTxtArr, spaceChar);
	}


	public String decryptTxt() {
		String[] decTxtArr = txt.split(spaceChar);
		String decTxt = "", repTxt = "";
		char longToChar;

		for (int i = 0; i < decTxtArr.length; i++) {
			repTxt = decTxtArr[i].replace(startChar, " ").substring(1); // replace startCahr with space and skip the first character so we don't have an extra space as the first character
			String[] wordArr = repTxt.split(" ");

			for (int j = 0; j < wordArr.length; j++) {
				longToChar = (char) quadraticSolver(aVal, bVal, Integer.parseInt(wordArr[j])); // F(x) = (-σ + sqrt((σ)^2 - 4ϕ(-x))) / (2ϕ)
				decTxt += longToChar;
			}
			decTxtArr[i] = decTxt;
			decTxt = "";
		}
		return getOutput(decTxtArr, " ");
	}

	public long quadraticSolver(long a, long b, long c) {
		/*
		 * F(x) = (-σ + sqrt((σ)^2 - 4ϕ(-x))) / (2ϕ)
		 * plugging in any encrypted ASCII code for x in F(x) will return the orginal ACII code
		 * given that ϕ and σ were not changed during the encryption
		 */
		long x = 0;
		c = -1 * c;
		double top = (-1 * b) + Math.sqrt( Math.pow(b, 2) - (4 * (a * c)) );
		double bottom = 2 * a;
		x = (long)(top / bottom);
		return x;
	}

	public String getOutput(String[] cipherArr, String seperator) {
		String output = "";

		for (int i = 0; i < cipherArr.length; i++) {
			output += cipherArr[i];

			if (i != cipherArr.length-1) {
				output += seperator;
			}
		}
		return output;
	}

    public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
       	System.out.print("Input: ");
		String inpTxt = keyboard.nextLine();

		keyboard.close();

		Cipher appEnc = new Cipher(inpTxt);

		System.out.println("Output: " + appEnc.cipher());
    }
}