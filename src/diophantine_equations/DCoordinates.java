package diophantine_equations;

public class DCoordinates {

    public DCoordinates(int x, int y) {
	super();
	this.x = x;
	this.y = y;
    }

    public DCoordinates() {
	super();

    }

    int x = 0;
    int y = 0;

    public void generateNewCoordinates(int lowerBound, int upperBound) {
	// Random rn = new Random();

	this.x = lowerBound
		+ (int) (Math.random() * (upperBound - lowerBound + 1));
	this.y = lowerBound
		+ (int) (Math.random() * (upperBound - lowerBound + 1));
    }

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    @Override
    public String toString() {
	return "[x=" + x + ", y=" + y + "]";
    }

}
