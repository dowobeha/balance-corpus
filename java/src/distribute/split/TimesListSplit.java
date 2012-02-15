package distribute.split;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class TimesListSplit extends ListSplit {

    public TimesListSplit(List<String> input, int parts, String sizeFile) throws FileNotFoundException {
	super(input, parts);
	    
	updateSizes(sizeFile);
	
	refresh();	
    }

    public TimesListSplit(List<String> input, int parts, List<Double> times) {
	super(input, parts);
	    
	updateSizes(times);
	
	refresh();
    }

    private void refresh() {
	Collections.sort(wordsPerLine);
	Collections.reverse(wordsPerLine);
	
	List<Double> totalInPart = new ArrayList<Double>(parts);
	for (int i=0; i<parts; i+=1) {
	    totalInPart.add(0.0);
	}
	
	for (LineSize lineSize : wordsPerLine) {
	    int partition = indexOfLeast(totalInPart);
            
	    //  part_for_line needs a 1-based partition index, so add 1
	    partitionForLine[lineSize.index] = partition+1;
            
	    double size = totalInPart.get(partition);
	    totalInPart.set(partition, size+lineSize.size);
	}
    }

    
    protected void updateSizes(List<Double> times) {
	if (times != null) {
	    int index = -1;
	    for (Double time : times) {
		index += 1;
		wordsPerLine.get(index).setSize(time);
	    }
	}
    }

    protected void updateSizes(String sizeFile) throws FileNotFoundException {
	if (sizeFile != null) {
	    int index = -1;
	    Scanner scanner = new Scanner(new File(sizeFile));
	    while (scanner.hasNextDouble()) {
		index += 1;
		double value = scanner.nextDouble();
		wordsPerLine.get(index).setSize(value);
	    }
	}
    }
    
    @Override
	protected int partitionForLine(int line) {
	return partitionForLine[line];
    }
    
}