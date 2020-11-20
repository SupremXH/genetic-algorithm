import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GA {
	static final int pop_size = 1000;
	static ArrayList<Integer[]> ran = new ArrayList<>();
	static ArrayList<Integer[]> BBSran = new ArrayList<>();
	static Integer[] Bestrandomsolution;
	static Integer[] BestBBSsolution;
	static ArrayList<Integer> primeList;
	static int[][] G;
	static int[] circuit;
	static int length;
	static boolean found = false;
	static int p, q, x;
	static File input;
	static File output;
	static FileWriter w;

	public static void generateRandomSolution() {
		for (int i = 0; i < pop_size; i++) {
			Integer[] randomcircuit = new Integer[length + 1];
			for (int index = 0; index <= length; index++) {
				randomcircuit[index] = (int) (Math.random() * length);
			}
			ran.add(randomcircuit);
		}
	}

	public static boolean isprime(int n) {
		for (int i = 2; i < n / 2; i++)
			if (n % i == 0)
				return false;
		return true;
	}

	public static void generatePrime() {
		primeList = new ArrayList<>();
		for (int i = 50; i < 200; i++) {
			if (isprime(i) && i % 4 == 3) {
				primeList.add(i);
			}
		}
	}

	public static void generateBBSSolution() {
		generatePrime();
		Integer[] randomcircuit = new Integer[length + 1];
		for (int i = 0; i < pop_size; i++) {
			p = primeList.get((int) (Math.random() * primeList.size()));
			q = primeList.get((int) (Math.random() * primeList.size()));
			x = (int) (Math.random() * 200 + 200);
			int remainder = 0;
			int l = 0;
			randomcircuit = new Integer[length + 1];
			while (l <= length) {
				remainder = x * x % (p * q);
				if (remainder == 0) {
					p = primeList.get((int) (Math.random() * primeList.size()));
					q = primeList.get((int) (Math.random() * primeList.size()));
					x = (int) (Math.random() * 200);
					remainder = x * x % (p * q);
				}
				while ((x * x) < (p * q) || remainder == x) {
					p = primeList.get((int) (Math.random() * primeList.size()));
					q = primeList.get((int) (Math.random() * primeList.size()));
					x = (int) (Math.random() * 200);
				}
				x = remainder;
				int bits = (int) (Math.log(length) / Math.log(2) + 1);
				double number = convertToRealNumber(x);
				StringBuilder binary = new StringBuilder();
				while (number > 0 && binary.length() < bits * 2) {
					number *= 2;
					if (number >= 1) {
						binary.append(1);
						number--;
					} else {
						binary.append(0);
					}
				}
				String bin = binary.toString();
				if (bin.length() <= bits && bin.length() > 0) {
					if (Integer.parseInt(bin) <= length) {
						randomcircuit[l++] = Integer.parseInt(bin);
					}
				}
				if (bin.length() > bits) {
					if (Integer.parseInt(bin.substring(0, bits), 2) <= length) {
						randomcircuit[l++] = Integer.parseInt(bin.substring(0, bits), 2);
					}
					if (l <= length && Integer.parseInt(bin.substring(bits, bin.length()), 2) <= length) {
						randomcircuit[l++] = Integer.parseInt(bin.substring(bits, bin.length()), 2);
					}
				}

			}
			BBSran.add(randomcircuit);
		}
	}

	private static double convertToRealNumber(double r) {
		return r >= 1 ? convertToRealNumber(r / 10) : r;
	}

	public static void generatefitness(ArrayList<Integer[]> list) throws Exception {
		int fitness = 0;
		double totalfitness = 0;
		for (Integer[] randomsolution : list) {
			int count = 0;
			for (int i = 0; i <= length; i++) {
				if (randomsolution[i] == circuit[i])
					count++;
			}
			totalfitness += count;
			if (count > fitness) {
				fitness = count;
				Bestrandomsolution = randomsolution;
			}
		}
		System.out.println(fitness + "  " + totalfitness / (list.size() * (length + 1)));
		for (Integer i : Bestrandomsolution)
			System.out.print(i + " ");
		System.out.println();
		System.out.println();
		for (Integer i : Bestrandomsolution)
			w.write(i + " ");
		w.write("\n");
		w.write("the max fitness is " + fitness + "\n");
		w.write("the fitness percentage from 1000 pop_size is " + totalfitness * 100 / (list.size() * (length + 1))
				+ "%\n");

	}

	private static void constract() throws Exception {
		Scanner sc = new Scanner(input);
		length = sc.nextInt();
		G = new int[length][length];
		circuit = new int[length + 1];
		Bestrandomsolution = new Integer[length + 1];
		for (int i = 0; i < length; i++) {
			circuit[i] = -1;
			for (int j = 0; j < length; j++) {
				G[i][j] = sc.nextInt();
			}
		}
		sc.close();
		circuit[0] = 5;
		bruceforce(circuit[0], 1);
		w.write("bruceforce:\n");
		for (int i : circuit)
			w.write(i + " ");
		w.write("\n");
		generateRandomSolution();
		w.write("random:\n");
		generatefitness(ran);
		w.write("BBSrandom:\n");
		generateBBSSolution();
		generatefitness(BBSran);
		w.close();
	}

	private static void bruceforce(int vertex, int index) {
		if (index == length) {
			if (G[vertex][circuit[0]] == 1) {
				circuit[index] = circuit[0];
				found = true;
			}
			return;
		}
		for (int i = 0; i < length; i++) {
			if (G[i][vertex] == 1 && !exist(i, index)) {
				circuit[index++] = i;
				G[i][vertex] = 0;
				G[vertex][i] = 0;
				bruceforce(i, index);
				if (found)
					return;
				circuit[--index] = -1;
				G[i][vertex] = 1;
				G[vertex][i] = 1;
			}
		}
	}

	private static boolean exist(int v, int end) {
		for (int i = 0; i < end; i++) {
			if (circuit[i] == v)
				return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		input = new File(args[0]);
		output = new File(args[1]);
		w = new FileWriter(output);
		constract();
	}

}
