package distribute.split;

import java.io.FileNotFoundException;

public class NaiveFileSplit extends FileSplit {

	public NaiveFileSplit(String inputFile, int parts) throws FileNotFoundException {
		super(inputFile, parts);
				
		int ceil  = (int) Math.ceil( (double) totalLines/parts);
		int floor = (int) Math.floor((double) totalLines/parts);
		
		int partCeil  = totalLines - floor*parts;
		
		int partition = 1;
		int linesInThisPart = 0;
		
		for (int index=0; index<totalLines; index+=1) {
			
            if (partition <= partCeil) {
            	if (linesInThisPart >= ceil) {
            		linesInThisPart=0; 
            		partition += 1;
            	}
            } else {
            	if (linesInThisPart >= floor) {
            		linesInThisPart=0;
            		partition += 1;
            	}
            }

            partitionForLine[index] = partition;
            linesInThisPart += 1;
		
		}
		
	}

	@Override
	protected int partitionForLine(int line) {
		return partitionForLine[line];
	}
	
}