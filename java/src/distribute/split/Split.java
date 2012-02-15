package distribute.split;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Split implements Iterable<String> {

	protected final List<LineSize> wordsPerLine;
	protected final int parts;

	protected static final Logger LOG = 
		Logger.getLogger(Split.class.getCanonicalName());
	
	public Split(int parts) {
		this.wordsPerLine = new ArrayList<LineSize>();
		this.parts = parts;
	}

	protected static <V extends Comparable<V>> int indexOfLeast(List<V> list) {

		V best = list.get(0);
		int bestIndex = 0;

		int i = -1;
		for (V value : list) {
			i += 1;
			if (value.compareTo(best) < 0) {
				best = value;
				bestIndex = i;
			}
		}

		return bestIndex;

	}

	protected abstract int partitionForLine(int line);

	public Map<Integer, Map<Integer, Integer>> splitAndIndex(String outputPrefix, File indexFileLocation) throws FileNotFoundException {	
		Map<Integer, Map<Integer, Integer>> partitionContents = split(outputPrefix); {
	
			PrintStream out = new PrintStream(indexFileLocation);
			ArrayList<Integer> partitions = new ArrayList<Integer>(partitionContents.keySet());
			Collections.sort(partitions);
			for (Integer partition : partitions) {
				Map<Integer,Integer> map = partitionContents.get(partition);
				ArrayList<Integer> relativeLines = new ArrayList<Integer>(map.keySet());
				Collections.sort(relativeLines);
				for (Integer relativeLine : relativeLines) {
					Integer originalLine = map.get(relativeLine);
					out.print("Partition ");
					out.print(partition);
					out.print(" line ");
					out.print(relativeLine);
					out.print(" is original line ");
					out.print(originalLine);
					out.print('\n');
				}
			}
			out.flush();
			out.close();
		}
		
		return partitionContents;
	}
	
	public Map<Integer, Map<Integer, Integer>> split() {

	    Map<Integer, List<Integer>> indices = new HashMap<Integer, List<Integer>>();
   
	    for (int i = 1; i <= this.parts; i += 1) {
		indices.put(i, new ArrayList<Integer>());
	    }

	    boolean logging = LOG.isLoggable(Level.FINE);
	    int lineNumber = -1;
	    for (String line : this) {
		lineNumber += 1;
		int partition = partitionForLine(lineNumber);
		if (logging) {
		    LOG.fine("Line number " + lineNumber + " is assigned to partition " + partition);
		}
		indices.get(partition).add(lineNumber);	
	    }

	    logging = LOG.isLoggable(Level.FINEST);
	    Map<Integer, Map<Integer, Integer>> maps = new HashMap<Integer, Map<Integer, Integer>>();
	    for (Map.Entry<Integer, List<Integer>> entry : indices.entrySet()) {
		int partition = entry.getKey();
		List<Integer> list = entry.getValue();

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		{
		    int partitionLineNumber = 0;
		    for (int originalLineNumber : list) {
			map.put(partitionLineNumber, originalLineNumber);
			partitionLineNumber += 1;
		    }
		}

		if (logging) {
		    StringBuilder s = new StringBuilder();
				
		    s.append("Partition "); 
		    s.append(partition);
		    s.append(" has the following line mapping: {");
		    s.append('\n');
		    for (Map.Entry<Integer, Integer> lineMapEntry : map.entrySet()) {
			s.append('\t');
			s.append(lineMapEntry.getKey());
			s.append(" => original ");
			s.append(lineMapEntry.getValue());
			s.append('\n');
		    }
		    s.append("}");
		    
		    LOG.finest(s.toString());
		}
		
		maps.put(partition, map);
	    }

	    return maps;

	}
	
 
	public Map<Integer, Map<Integer, Integer>> split(String outputPrefix)
			throws FileNotFoundException {

		Map<Integer, List<Integer>> indices = new HashMap<Integer, List<Integer>>();

		Map<Integer, PrintStream> outFiles = new HashMap<Integer, PrintStream>(
				parts);

		for (int i = 1; i <= parts; i += 1) {
			outFiles.put(i, new PrintStream(outputPrefix + i));
			indices.put(i, new ArrayList<Integer>());
		}

		int lineNumber = -1;
		for (String line : this) {

			lineNumber += 1;
			int partition = partitionForLine(lineNumber);
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Line number " + lineNumber + " is assigned to partition " + partition);
			}
			// System.err.println("Partition " + partition + " for line " +
			// lineNumber);
			indices.get(partition).add(lineNumber);
			outFiles.get(partition).println(line);

		}

		for (PrintStream out : outFiles.values()) {
			out.flush();
			out.close();
		}

		Map<Integer, Map<Integer, Integer>> maps = new HashMap<Integer, Map<Integer, Integer>>();
		for (Map.Entry<Integer, List<Integer>> entry : indices.entrySet()) {
			int partition = entry.getKey();
			List<Integer> list = entry.getValue();

			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			{
				int partitionLineNumber = 0;
				for (int originalLineNumber : list) {
					map.put(partitionLineNumber, originalLineNumber);
					partitionLineNumber += 1;
				}
			}

			if (LOG.isLoggable(Level.FINEST)) {
				StringBuilder s = new StringBuilder();
				
				s.append("Partition "); 
				s.append(partition);
				s.append(" has the following line mapping: {");
				s.append('\n');
				for (Map.Entry<Integer, Integer> lineMapEntry : map.entrySet()) {
					s.append('\t');
					s.append(lineMapEntry.getKey());
					s.append(" => original ");
					s.append(lineMapEntry.getValue());
					s.append('\n');
				}
				s.append("}");
				
				LOG.finest(s.toString());
			}
			
			maps.put(partition, map);
		}

		return maps;
	}

}
