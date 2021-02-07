package perozzivittori;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class TestGraph {
	
	public TestGraph() {}
	
	public static void main(String[] args) {
		
		List<String> edges = new ArrayList<>();
		List<String> vertices = new ArrayList<>();
		Map<String, List<String>> adjacentList = new HashMap<>();
		
		List<pairStringList> adjacentList1 = new ArrayList<>();
		
		adjacentList.put("hey", new LinkedList<String>());
	}
	
	public static class pairStringList {
		String actorName;
		List<String> edges;
	
		public pairStringList() {};
		
		public pairStringList(String n) {actorName = n;};
	}

}
