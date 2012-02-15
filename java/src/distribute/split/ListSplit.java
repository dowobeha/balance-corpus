package distribute.split;

import java.util.List;
import java.util.Iterator;

abstract class ListSplit extends Split {

    private final List<String> list;
    protected final int totalLines;
    protected final int[] partitionForLine;
	
    public ListSplit(List<String> list, int parts) {
	super(parts);
	this.list = list;
	this.totalLines = list.size();
	this.partitionForLine = new int[totalLines];	

	int index = -1;
	for (String line : this) {
	    index += 1;
	    this.wordsPerLine.add(new LineSize(line, index));
	}
	
    }

    public Iterator<String> iterator() {
	return list.iterator();
    }

}
