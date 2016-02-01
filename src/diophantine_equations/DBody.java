package diophantine_equations;


public class DBody {
    DCoordinates position;
    DCoordinates velocity;

    DCoordinates newPosition;

    DCoordinates bestPosition;

    double mass;
    double bestMass;
    
    boolean track=false;

    public DBody(DCoordinates position) {
	this.position = new DCoordinates(position.getX(), position.getY());
	this.velocity = new DCoordinates(position.getX(), position.getY());
	this.newPosition = new DCoordinates(position.getX(), position.getY());
	this.bestPosition = new DCoordinates(position.getX(), position.getY());
	this.mass = 0;
	this.bestMass = 0;
    }

    public double computeMass(int v, int max) {
	double mass = 0;
	mass = 1 - ((double) v / (double) max);
	return mass;
    }

    public DCoordinates getPosition() {
	return position;
    }

    public void setPosition(DCoordinates position) {
	this.position = position;
    }

    public DCoordinates getVelocity() {
	return velocity;
    }

    public void setVelocity(DCoordinates velocity) {
	this.velocity = velocity;
    }

    public DCoordinates getNewPosition() {
	return newPosition;
    }

    public void setNewPosition(DCoordinates newPosition) {
	this.newPosition = newPosition;
    }

    public DCoordinates getBestPosition() {
	return bestPosition;
    }

    public void setBestPosition(DCoordinates bestPosition) {
	this.bestPosition = bestPosition;
    }

    public double getMass() {
	return mass;
    }

    public void setMass(double mass) {
	this.mass = mass;
    }

    public double getBestMass() {
	return bestMass;
    }

    public void setBestMass(double bestMass) {
	this.bestMass = bestMass;
    }

    public boolean isTrackable() {
        return track;
    }

    public void setTrackable(boolean track) {
        this.track = track;
    }

}
