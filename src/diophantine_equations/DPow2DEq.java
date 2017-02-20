package diophantine_equations;

//two dimensional diophantine equation
//ax+by=d
public class DPow2DEq {
    // coefficients
    private int a = 1;
    private int b = 1;
    private int d = 0;
    private int pow = 1;

    private int upBound = 0;
    private int lowBound = 0;

    public DPow2DEq(int a, int b, int d, int pow) {
	super();
	this.a = a;
	this.b = b;
	this.d = d;
	this.pow = pow;
    }

    public int diffBtwSolutions(int x, int y) {
	int diff = 0;
	int dd = a * (int) Math.pow(x, this.pow) + b
		* (int) Math.pow(y, this.pow);
	diff = Math.abs(d - dd);
	return diff;
    }

    public int getA() {
	return a;
    }

    public void setA(int a) {
	this.a = a;
    }

    public int getB() {
	return b;
    }

    public void setB(int b) {
	this.b = b;
    }

    public int getD() {
	return d;
    }

    public void setD(int d) {
	this.d = d;
    }

    public int getUpBound() {
	return upBound;
    }

    public void setUpBound(int upBound) {
	this.upBound = upBound;
    }

    public int getLowBound() {
	return lowBound;
    }

    public void setLowBound(int lowBound) {
	this.lowBound = lowBound;
    }

    @Override
    public String toString() {
	return a + "x^" + pow + "+" + b + "y^" + pow + "=" + d;
    }

}
