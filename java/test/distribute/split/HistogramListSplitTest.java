package distribute.split;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests.
 * 
 * @author Lane Schwartz
 */
public class HistogramListSplitTest {

    /** Number of parts into which the sample file should be split. */
    private static final int parts = 16;
    
    /** Total number of lines in the sample corpus. */
    private static final int totalLines = 89;

    /** Path to the sample file to split. */
    private static final String filePath = "../sample/corpus/udhr.en";

    private static final String outputPrefix = "../sample/histogram/out.";

    private static final String outputIndexPrefix = "../sample/histogram/index.";

    @Test
    public void testBalance() {

	Split splitter = null;
	try {
	    List<String> list = new ArrayList<String>(totalLines);
	    Scanner scanner = new Scanner(new File(filePath));
	    while (scanner.hasNextLine()) {
		String line = scanner.nextLine();
		list.add(line);
	    }
	    splitter = new HistogramListSplit(list,parts);    
	} catch (FileNotFoundException e) {
	    Assert.fail("Input file not found. It should have been located at " + filePath);
	}

	Map<Integer,Integer> expectedPartForLine = new HashMap<Integer,Integer>();

	for (int part=1; part<=parts; part+=1) {

	    String indexFilePath = 
		(part<10) ? 
		outputIndexPrefix + "0" + part :
		outputIndexPrefix       + part ;
		
	    try {
		Scanner scanner = new Scanner(new File(indexFilePath));

		while (scanner.hasNextInt()) {

		    int value = scanner.nextInt();
		    expectedPartForLine.put(value,part);

		}
	    } catch (FileNotFoundException e) {
		Assert.fail("Index file not found. It should have been located at " + indexFilePath);
	    }

	}


	for (int line=1; line<=totalLines; line+=1) {

	    int expectedPart = expectedPartForLine.get(line);
	    int actualPart   = splitter.partitionForLine(line);
	    Assert.assertEquals(actualPart,expectedPart,"Expected line " + line + " to be in partition " + expectedPart + ", but it was actually in partition " + actualPart);

	}

    }
	
}
