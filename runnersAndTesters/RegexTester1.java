package runnersAndTesters;

import tool.ToolMethods;

public class RegexTester1 {

	public static void main(String[] args) {
		String n = "(2478,345)";
		boolean m = ToolMethods.isEdgeTuple(n);
		System.out.println(m);
		
		String spl = "(3,4);(4,5)";
		String[] splitd = spl.split(";");
		for (int i=0;i<splitd.length;i++) {
			int c = splitd[i].indexOf(",");
			System.out.println(splitd[i].substring(1, c));
			System.out.println(splitd[i].substring(c+1, splitd[i].length()-1));
			System.out.println(splitd[i]);
		}
		
		String example = "(0,1);(0,2);(1,3);(1,4)";
		Integer[][] exampleSplitted = ToolMethods.extractEdges(example);
		for (int i=0;i<exampleSplitted.length;i++) {
			for (int j=0;j<exampleSplitted[i].length;j++) {
				System.out.print(exampleSplitted[i][j] + " ");
			}
			System.out.println();
		}
		
		String si = "0 ,5";
		if (si.matches("(\\d+\\,)*\\d+")) {
			System.out.println(si);
		}
	}

}
