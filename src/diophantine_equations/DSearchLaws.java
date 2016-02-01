package diophantine_equations;

import java.util.Random;


public class DSearchLaws {

    public static double euclidianDistance(DCoordinates p1, DCoordinates p2) {
	double dist = 0.0;
	double a = (p1.getX() - p2.getX());
	double b = (p1.getY() - p2.getY());
	dist = Math.sqrt(a * a + b * b);
	return dist;
    }

    public static void updateVelocity(DBody b, DCoordinates gpos, double gm) {

	double fp = DSearchLaws.computeForce(70, b.getMass(), b.getBestMass(),
		b.position, b.bestPosition);
	double fg = DSearchLaws.computeForce(70, b.getMass(), gm, b.position,
		gpos);

	double vx = (b.getMass()) * b.velocity.getX() + Math.random() * fp
		* (b.bestPosition.getX() - b.position.getX()) + Math.random()
		* fg * (gpos.getX() - b.position.getX());
	double vy = (b.getMass()) * b.velocity.getY() + Math.random() * fp
		* (b.bestPosition.getY() - b.position.getY()) + Math.random()
		* fg * (gpos.getY() - b.position.getY());

	b.velocity.setX((int) Math.round(vx));
	b.velocity.setY((int) Math.round(vy));

    }

    public static void updateVelocity_PSO(DBody b, DCoordinates gpos,
	    double c1, double c2, double c3) {

	double vx = c1 * b.velocity.getX() + Math.random() * c2
		* (b.bestPosition.getX() - b.position.getX()) + Math.random()
		* c3 * (gpos.getX() - b.position.getX());

	double vy = c1 * b.velocity.getY() + Math.random() * c2
		* (b.bestPosition.getY() - b.position.getY()) + Math.random()
		* c3 * (gpos.getY() - b.position.getY());

	b.velocity.setX((int) Math.round(vx));
	b.velocity.setY((int) Math.round(vy));

    }

    public static DCoordinates randomNeighbour(DCoordinates gpos, int step) {
	DCoordinates neighbour = new DCoordinates(gpos.getX(), gpos.getY());
	Random random = new Random();
	int rnd;
	// for x
	rnd = random.nextInt(3);
	switch (rnd) {
	case 0:
	    neighbour.setX(neighbour.getX());
	    break;
	case 1:
	    neighbour.setX(neighbour.getX() + step);
	    break;
	case 2:
	    neighbour.setX(neighbour.getX() - step);
	    break;

	default:
	    neighbour.setX(neighbour.getX());
	    break;
	}
	// for y
	rnd = random.nextInt(3);
	switch (rnd) {
	case 0:
	    neighbour.setY(neighbour.getY());
	    break;
	case 1:
	    neighbour.setY(neighbour.getY() + step);
	    break;
	case 2:
	    neighbour.setY(neighbour.getY() - step);
	    break;

	default:
	    neighbour.setY(neighbour.getY());
	    break;
	}

	return neighbour;

    }

    public static void updatePosition(DBody b, int upBound, int lowBound) {
	// System.out.println("Update position");
	// System.out.println("Old position " + b.position);
	// System.out.println("Velocity " + b.velocity);
	int px = b.position.getX() + b.velocity.getX();

	while (px > upBound) {
	    px = px - upBound;
	}
	while (px < lowBound) {
	    px = px + (-lowBound);
	}

	int py = b.position.getY() + b.velocity.getY();
	while (py > upBound) {
	    py = py - upBound;
	}
	while (py < lowBound) {
	    py = py + (-lowBound);
	}

	b.position.setX(px);
	b.position.setY(py);
	// System.out.println("New position: " + b.position);

    }
    
    public static void updatePosition_HC (DBody b,DCoordinates n, int upBound, int lowBound) {
   	// System.out.println("Update position");
   	// System.out.println("Old position " + b.position);
   	// System.out.println("Velocity " + b.velocity);
   	int px = n.getX() ;

   	while (px > upBound) {
   	    px = px - upBound;
   	}
   	while (px < lowBound) {
   	    px = px + (-lowBound);
   	}

   	int py = n.getY();
   	while (py > upBound) {
   	    py = py - upBound;
   	}
   	while (py < lowBound) {
   	    py = py + (-lowBound);
   	}

   	b.position.setX(px);
   	b.position.setY(py);
   	// System.out.println("New position: " + b.position);

       }

    public static double computeForce(int g, double m1, double m2,
	    DCoordinates p1, DCoordinates p2) {
	double force = 0.0;

	double r = DSearchLaws.euclidianDistance(p1, p2);
	if (r != 0) {
	    // force = (g * m1 * m2) / (r * r);
	    force = (g * m2) / (r * r);
	} else
	    force = 0.0;

	return force;
    }

}
