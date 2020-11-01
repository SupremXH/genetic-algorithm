import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HamiltonianCircuit {

static int[][] G;
static int[] circuit;
static int length;
static boolean found=false;

private static void constract() throws IOException {

	Scanner sc=new Scanner(new File("input3.txt"));
	length=sc.nextInt();
	G=new int[length][length];
	circuit=new int[length+1];
	for(int i=0;i<length;i++) {
		circuit[i]=-1;
		for(int j=0;j<length;j++) {
			G[i][j]=sc.nextInt();
		}
	}

	circuit[0]=5;
	findcircuit(circuit[0],1);
	
}
	private static void findcircuit(int vertex,int index) {

		if(index==length) {
			if(G[vertex][circuit[0]]==1) {
				circuit[index]=circuit[0];
				found=true;
			}
			return;
		}
		for(int i=0;i<length;i++) {
			if(G[i][vertex]==1 && !exist(i,index)) {
				circuit[index++]=i;
				G[i][vertex]=0;
				G[vertex][i]=0;
				findcircuit(i,index);
				if(found) return;
				circuit[--index]=-1;
				G[i][vertex]=1;
				G[vertex][i]=1;
			}
		}
}
	private static boolean exist(int v,int end) {
		for(int i=0;i<end;i++) {
			if(circuit[i]==v)
				return true;
		}
		return false;
	}

	private static void print() throws Exception {
		FileWriter w=new FileWriter("output3.txt");
		if(found) {
			for(int i:circuit)
				w.write(i+" ");
		}else {
			w.write("Solution not found");
		}
		w.close();
		
	}

	public static void main(String[] args) throws Exception{
		constract();
		print();
	}





}
