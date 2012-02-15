package distribute.split;

import java.io.FileNotFoundException;
import java.util.Collections;


public class HistogramFileSplit extends FileSplit {

	public HistogramFileSplit(String inputFile, int parts) throws FileNotFoundException {
		super(inputFile, parts);
		
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