package diophantine_equations;

public class DPoint extends DCoordinates {
    private DKey key = new DKey(0, 0);
    private double value = -1;

    public DPoint(int x, int y) {
	super();
	this.x = x;
	this.y = y;
	this.setKey(new DKey(x, y));
    }

    public DPoint(DCoordinates coord) {
	super();
	this.x = coord.getX();
	this.y = coord.getY();
	this.setKey(new DKey(x, y));
    }

    public void computeValue(int v, int max) {
	this.value = 1 - ((double) v / (double) max);
    }

    public DKey getKey() {
	return key;
    }

    public void setKey(DKey key) {
	this.key = key;
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

    @Override
    public String toString() {
	return "Point [x=" + x + ", y=" + y + ", value=" + value + "]";
    }

}
