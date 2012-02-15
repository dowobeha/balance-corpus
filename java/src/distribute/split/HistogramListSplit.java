package distribute.split;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class HistogramListSplit extends ListSplit {

	public HistogramListSplit(List<String> input, int parts) {
		super(input, parts);
		
		Collections.sort(wordsPerLine);
		
		int partition=0;
		for (LineSize line : wordsPerLine) {
			if (partition < parts) {
				partition+=1;
			} else {
				partition=1;
			}
			partitionForLine[line.index] = partition;
		}
		
	}

	@Override
	protected int partitionForLine(int line) {
		return partitionForLine[line];
	}
	
}
