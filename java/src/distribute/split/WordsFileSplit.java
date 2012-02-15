package distribute.split;

import java.io.FileNotFoundException;

public class WordsFileSplit extends TimesFileSplit {

	public WordsFileSplit(String inputFile, int parts) throws FileNotFoundException {
	    super(inputFile,parts,null);
	}
	
}
