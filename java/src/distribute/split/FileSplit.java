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

public abstract class FileSplit extends Split {

	protected final String inputFile;
	protected final int totalLines;
	protected final int[] partitionForLine;
	
	public FileSplit(String inputFile, int parts) throws FileNotFoundException {
		super(parts);
		this.inputFile = inputFile;
	    
		int index = -1;
		for (String line : this) {
			index += 1;
			this.wordsPerLine.add(new LineSize(line, index));
		}

		this.totalLines = wordsPerLine.size();
		this.partitionForLine = new int[totalLines];

	}

	@Override
	public Iterator<String> iterator() {
		Iterator<String> iterator;
		try {
			iterator = new Iterator<String>() {
				final Scanner scanner = new Scanner(new File(inputFile));

				@Override
				public boolean hasNext() {
					boolean value = scanner.hasNextLine();
					if (value == false) {
						scanner.close();
					}
					return value;
				}

				@Override
				public String next() {
					return scanner.nextLine();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			iterator = null;
		}
		return iterator;
	}

}
