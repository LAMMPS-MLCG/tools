package contactMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainclass {

	private static int N;// rows (first group)
	private static int M;// columns (second group)
	private static float cutoff;
	private static boolean verbose = false;

	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);
		cutoff = Float.parseFloat(getParameter(args, "-cutoff", "0", "100"));

		N = Integer.parseInt(getParameter(args, "-N", "0", "-1"));
		M = Integer.parseInt(getParameter(args, "-M", "0", "-1"));
		if (M < 0 || N < 0) {
			// TODO parse file name in the format "(\\d+)X(\\d+)"
		}

		// parse verbose from command line (-v)
		verbose = Boolean.parseBoolean(getParameter(args, "-v", "true", "False"));

		try {
			String inputFilename = getParameter(args, "-f", "", "dist.xvg");
			Scanner in = new Scanner(new File(inputFilename));
			int[][] a = parseInput(in);

			String outputFilename = getParameter(args, "-o", "", "output.csv");
			PrintStream out = new PrintStream(outputFilename);
			writeOutput(a, out);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(mainclass.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	static int[][] parseInput(Scanner scanner) {
		String[] arr;
		int[][] a;
		int oRow = 0, oCol = 0;
		float dist = 0;
		a = new int[N][M];
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.startsWith("@") || line.startsWith("#"))
				continue;
			line = line.substring(11);
			arr = line.split(" +");// assuming that the output is space-delimited
			oRow = 0;
			oCol = 0;

			for (int i = 1; i < arr.length; i++) {// pypass the first column (the time time)
				dist = Float.parseFloat(arr[i]);
				// System.out.print(dist+",");//test

				if ((dist <= cutoff)) {
					a[oRow][oCol]++;
				}

				oRow++;
				if (oRow % N == 0) {
					// System.out.println("\nnrow="+(oRow)+", col="+(oCol)+"\t
					// lastVal="+dist);//test
					// System.out.println();//test
					oCol++;
					oRow = 0;
				}
			}
			// System.out.println();//test
		}
		return a;
	}

	static void writeOutput(int[][] a, PrintStream out) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				final int val = a[i][j];
				if (verbose)
					writeValue(val, System.out);
				writeValue(val, out);
			}
			if (verbose)
				System.out.println();
			out.println();
		}
	}

	static void writeValue(int v, PrintStream out) {
		out.format("%6d\t", v);
	}

	public static String getParameter(String[] args, String parameter, String defaultValueIfParameterFound,
			String valueIfNotFound) {
		boolean flagFound = false;
		String input = defaultValueIfParameterFound;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(parameter)) {
				flagFound = true;
				if (i + 1 >= args.length) {
					break;
				}
				String nextArg = args[i + 1];
				if (nextArg.startsWith("-")) {
					break;
				} else {
					input = nextArg;
				}
				i += 1;
			}
		}
		if (!flagFound) {
			return valueIfNotFound;
		}
		return input;
	}

}
