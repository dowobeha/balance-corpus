package distribute.split;


final class LineSize implements Comparable<LineSize> {

	Double size;
	final Integer index;
	
	LineSize(String line, int index) {
		this.size = new Double(line.trim().split("\\s+").length);
		this.index = index;
	}
	
	protected void setSize(double size) {
		this.size = size;
	}
	
	@Override
	public int compareTo(LineSize other) {
		if (size.equals(other.size)) {
			return index.compareTo(other.index);
		} else {
			return size.compareTo(other.size);
		}
	}
	
}