

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;


class ValueComparator implements Comparator<Relationship> {
	 Map<Relationship, Integer> base;
	    public ValueComparator(Map<Relationship, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(Relationship a, Relationship b) {
	        if (base.get(a) <= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }


}

class ValueComparatorList implements Comparator<List<String>> {
	 Map<List<String>, Integer> base;
	    public ValueComparatorList(Map<List<String>, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(List<String> a, List<String> b) {
	        if (base.get(a) <= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }


}

class ValueComparatorPath implements Comparator<Path> {
	 Map<Path, Integer> base;
	    public ValueComparatorPath(Map<Path, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(Path a, Path b) {
	        if (base.get(a) <= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }


}

class ValueComparatorResult implements Comparator<Map<List<Node>, List<Relationship>>> {
	 Map<Map<List<Node>, List<Relationship>>, Integer> base;
	    public ValueComparatorResult(Map<Map<List<Node>, List<Relationship>>, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(Map<List<Node>, List<Relationship>> a, Map<List<Node>, List<Relationship>> b) {
	        if (base.get(a) <= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }


}