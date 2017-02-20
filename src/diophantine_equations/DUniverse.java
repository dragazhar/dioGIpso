package diophantine_equations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

public class DUniverse {
    // unique checked points
    static public TreeMap<DKey, DPoint> points = new TreeMap<DKey, DPoint>();
    // bodies in the universe
    static public ArrayList<DBody> bodies = new ArrayList<DBody>();
    // predefined size of the universe(computed)
    static int universeSize = 0;
    // % required for computing size
    static double pc = 0.0010;

    // equation
    // public static D2DEq equation;

    public static DPow2DEq equation;
    // max value of distance function
    static int max = 0;

    // global best position and it's mass
    static double globalBestMass;
    static DCoordinates bestPosition;
    // age of the universe
    static int age = 200;
    // current generation
    static int epoch = 0;

    static public ArrayList<DCoordinates> track = new ArrayList<DCoordinates>();

    public static void main(String[] args) throws IOException {
	// runStat(1000);
	runOnce();
    }

    static void runOnce() throws IOException {
	init();
	System.out.println("Size of the universe: " + universeSize);
	bigBang();
	// printInitialSwarm(bodies);
	// readSwarm();
	draw(String.valueOf(epoch), bestPosition.getX(), bestPosition.getY(),
		track);
	epoch++;

	while ((epoch <= age) && (globalBestMass < 1.0)) {

	    // step_giPSO();
	    step_PSO();
	    // step_SHC();

	    evaluate();

	    draw(String.valueOf(epoch), bestPosition.getX(),
		    bestPosition.getY(), track);

	    // printing out progress line
	    System.out.print("+");
	    if (epoch % 50 == 0) {
		System.out.println();
	    }
	    epoch++;
	}
	System.out.println();

	System.out.println("Found at epoch: " + (epoch - 1) + " out of " + age);
	System.out.println("The best solution is found: "
		+ (globalBestMass == 1.0));
	System.out.println(bestPosition);
	System.out.println(points.size() + " points checked");

	// compute the all set of points
	enumerateAllPoints();
	draw("all", bestPosition.getX(), bestPosition.getY(), track);
    }

    static void runStat(int iterations) throws IOException {
	ArrayList<Integer> pointsChecked = new ArrayList<Integer>();
	ArrayList<Integer> epochLast = new ArrayList<Integer>();
	int fails = 0;
	for (int i = 0; i < iterations; i++) {
	    init();
	    bigBang();
	    // readSwarm();
	    epoch++;
	    while ((epoch <= age) && (globalBestMass < 1.0)) {

		step_giPSO();

		// step_PSO();
		// step_SHC();

		evaluate();

		epoch++;
	    }
	    if (globalBestMass < 1.0) {
		fails++;
	    }
	    // System.out.println("Found at epoch: " + (epoch - 1) + " out of "
	    // + age);
	    /*
	     * System.out.println("The best solution is found: " +
	     * (globalBestMass == 1.0));
	     */
	    // System.out.println(bestPosition);
	    // System.out.println(points.size() + " points checked");
	    // log((epoch - 1),points.size());
	    pointsChecked.add(points.size());
	    epochLast.add(epoch - 1);
	    epoch = 0;
	    universeSize = 0;
	    points = new TreeMap<DKey, DPoint>();
	    bodies = new ArrayList<DBody>();
	    globalBestMass = -1;
	    bestPosition = null;
	}

	// compute the all set of points
	// enumerateAllPoints();

	// compute stat
	double averageEL = 0;
	for (Integer el : epochLast) {
	    averageEL += (double) el / epochLast.size();

	}

	double deviationEL = 0;
	for (Integer el : epochLast) {
	    deviationEL += Math.pow((double) (el - averageEL), 2)
		    / (double) (epochLast.size() - 1);
	}
	System.out.println("Found at epoch: " + " average=" + averageEL
		+ " deviation=" + Math.sqrt(deviationEL));

	double averagePC = 0;
	for (Integer pc : pointsChecked) {
	    averagePC += (double) pc / pointsChecked.size();

	}
	double deviationPC = 0;
	for (Integer pc : pointsChecked) {
	    deviationPC += Math.pow((double) (pc - averagePC), 2)
		    / (double) (pointsChecked.size() - 1);
	}

	System.out.println("Point checked: " + " average=" + averagePC
		+ " deviation=" + Math.sqrt(deviationPC));
	System.out.println("Number of fails: " + fails);
	enumerateAllPoints();

    }

    static void step_giPSO() {
	for (DBody b : bodies) {

	    DSearchLaws.updateVelocity(b, bestPosition, globalBestMass);
	    DSearchLaws.updatePosition(b, equation.getUpBound(),
		    equation.getLowBound());
	    if (b.isTrackable()) {
		track.add(new DCoordinates(b.position.getX(), b.position.getY()));

	    }

	}
    }

    static void step_PSO() {
	// 1 1 1 - ok
	// 1 0.5 0.5 - ok
	// 0.2 0.5 0.5 - bad initial set - collapse
	// 2.0 0.5 0.5 - bad initial set - not converging
	double inertia = 1.0;
	double cognitive = 0.5;
	double social = 0.5;
	for (DBody b : bodies) {
	    DSearchLaws.updateVelocity_PSO(b, bestPosition, inertia, cognitive,
		    social);
	    DSearchLaws.updatePosition(b, equation.getUpBound(),
		    equation.getLowBound());
	    if (b.isTrackable()) {
		track.add(new DCoordinates(b.position.getX(), b.position.getY()));

	    }
	}
    }

    static void step_SHC() {
	for (DBody b : bodies) {
	    double mass = 0.0;
	    DCoordinates n = new DCoordinates(b.position.getX(),
		    b.position.getY());
	    int counter = 0;
	    while ((mass < b.getMass()) && (counter < 7)) {
		n = DSearchLaws.randomNeighbour(b.position, 2);
		double v = equation.diffBtwSolutions(n.getX(), n.getY());
		mass = 1 - ((double) v / (double) max);
		// Add body to points set, if it is not there
		DPoint point = new DPoint(n.getX(), n.getY());
		DKey key = point.getKey();
		if (!points.containsKey(key)) {
		    point.setValue(mass);

		    points.put(key, point);
		}

		counter++;
	    }

	    if (mass >= b.getMass()) {
		DSearchLaws.updatePosition_HC(b, n, equation.getUpBound(),
			equation.getLowBound());

	    }

	    if (b.isTrackable()) {
		track.add(new DCoordinates(b.position.getX(), b.position.getY()));

	    }
	}
    }

    static void evaluate() {
	for (DBody b : bodies) {
	    // compute body mass

	    b.setMass(b.computeMass(
		    equation.diffBtwSolutions(b.position.getX(),
			    b.position.getY()), max));
	    // update local best mass and position
	    if (b.getBestMass() < b.getMass()) {
		b.setBestMass(b.getMass());
		b.setBestPosition(new DCoordinates(b.position.getX(),
			b.position.getY()));
	    }

	    // update global best mass and position
	    if (globalBestMass < b.getMass()) {
		bestPosition = new DCoordinates(b.position.getX(),
			b.position.getY());
		globalBestMass = b.getMass();

	    }

	    // Add body to points set, if it is not there
	    DPoint point = new DPoint(b.getPosition().getX(), b.getPosition()
		    .getY());
	    DKey key = point.getKey();
	    if (!points.containsKey(key)) {
		point.setValue(b.getMass());

		points.put(key, point);
	    }
	}
    }

    static void printInitialSwarm(ArrayList<DBody> swarm) {
	PrintWriter res = null;
	try {
	    res = new PrintWriter(new BufferedWriter(new FileWriter(new File(
		    "dio\\iswarm.txt"))));

	    for (DBody b : swarm) {
		res.println(b.position.getX() + " " + b.position.getY());

	    }

	} catch (IOException e) {
	    System.err.println("Error in input/output process...");
	} finally {
	    res.close();
	}
    }

    static void log(int gen, int points) {
	PrintWriter res = null;
	try {
	    res = new PrintWriter(new BufferedWriter(new FileWriter(new File(
		    "dio\\log.txt"), true)));

	    res.println(gen + " " + points);

	} catch (IOException e) {
	    System.err.println("Error in input/output process...");
	} finally {
	    res.close();
	}
    }

    static void log2() {
	PrintWriter res = null;
	try {
	    res = new PrintWriter(new BufferedWriter(new FileWriter(new File(
		    "dio\\log2.txt"), true)));

	    res.println(globalBestMass);

	} catch (IOException e) {
	    System.err.println("Error in input/output process...");
	} finally {
	    res.close();
	}
    }

    static void readSwarm() throws IOException {
	BufferedReader dataR = new BufferedReader(new FileReader(new File(
		"dio\\iswarm.txt")));
	try {

	    String s = new String();
	    while ((s = dataR.readLine()) != null) {
		String xS = "";
		String yS = "";
		boolean end = false;
		for (int i = 0; i < s.length(); i++) {
		    if ((s.charAt(i) == ',') || (s.charAt(i) == ' ')) {
			end = true;
		    } else {
			if (!end) {
			    xS += s.charAt(i);
			} else {
			    yS += s.charAt(i);
			}
		    }

		}
		int x = Integer.parseInt(xS);
		int y = Integer.parseInt(yS);

		DCoordinates coord = new DCoordinates(x, y);
		DBody body = new DBody(coord);
		body.bestPosition = new DCoordinates(body.position.getX(),
			body.position.getY());

		body.setVelocity(new DCoordinates(0, 0));

		bodies.add(body);
		// initiate global best position and mass
		if ((bestPosition == null)) {

		    bestPosition = new DCoordinates(body.position.getX(),
			    body.position.getY());
		    globalBestMass = body.getMass();
		}

		evaluate();

		// System.out.println(x+"   "+y);

	    }
	    bodies.get(5).setTrackable(true);
	    track.add(new DCoordinates(bodies.get(5).position.getX(), bodies
		    .get(5).position.getY()));

	} catch (IOException e) {
	    System.err.println("Error in input/output process...");
	} finally {
	    dataR.close();
	}

    }

    static void bigBang() {
	for (int i = 0; i < universeSize; i++) {
	    DCoordinates rndCoord = new DCoordinates();
	    // generate random body

	    rndCoord.generateNewCoordinates(equation.getLowBound(),
		    equation.getUpBound());

	    // rndCoord.generateNewCoordinates(-50, -45);
	    // add random body to set
	    DBody rndBody = new DBody(rndCoord);
	    rndBody.bestPosition = new DCoordinates(rndBody.position.getX(),
		    rndBody.position.getY());

	    // DCoordinates rndCoord2 = new DCoordinates();`
	    /*
	     * rndCoord2.generateNewCoordinates(equation.getLowBound(),
	     * equation.getUpBound());
	     */
	    // rndBody.setVelocity(rndCoord2);
	    rndBody.setVelocity(new DCoordinates(0, 0));

	    bodies.add(rndBody);

	    // initiate global best position and mass
	    if ((bestPosition == null)) {

		bestPosition = new DCoordinates(rndBody.position.getX(),
			rndBody.position.getY());
		globalBestMass = rndBody.getMass();
	    }
	    evaluate();

	}

	bodies.get(5).setTrackable(true);
	track.add(new DCoordinates(bodies.get(5).position.getX(),
		bodies.get(5).position.getY()));

    }

    static void init() {
	// Define equation
	// Power 1
	equation = new DPow2DEq(4, 9, 91, 1);// 11 solutions in range
	// equation = new DPow2DEq(10, 7, 97,1);//10 solutions in range
	// equation = new DPow2DEq(24, 15, 9,1);//13 solutions in range
	// equation = new DPow2DEq(19, 23, 3, 1);// 4 solutions in range

	// Power 2
	// equation=new DPow2DEq(1,1,625,2);//x^2+y^2=625 20 solutions in range
	// equation = new DPow2DEq(1, 1, 149, 2);// x^2+y^2=149 8 solutions in
	// range

	// Power 3
	// equation = new DPow2DEq(1, 1, 1008,3);//2 solutions in range

	// Power 4
	// equation = new DPow2DEq(1, 1, 1921, 4);//8 solutions in range

	// Power 5
	// equation = new DPow2DEq(1, 1, 19932, 5);//2 solutions in range

	// Define upper and lower bounds of the range
	equation.setUpBound(50);
	equation.setLowBound(equation.getUpBound() * (-1));
	// Compute max value of the function at this range (required for
	// normalization)
	int mU = equation.diffBtwSolutions(equation.getUpBound(),
		equation.getUpBound());
	int mL = equation.diffBtwSolutions(equation.getLowBound(),
		equation.getLowBound());
	if (mU < mL) {
	    max = mL;
	    // System.out.println("Value at lower left corner: " + max);
	} else {
	    max = mU;
	    // System.out.println("Value at upper right corner: " + max);
	}

	// Compute size of the universe as % from total amount of points
	int s = (Math.abs(equation.getLowBound()) + Math.abs(equation
		.getUpBound()));

	universeSize = (int) Math.round(pc * s * s);

    }

    static void enumerateAllPoints() {
	int solutions = 0;
	// For all set of points
	for (int i = equation.getLowBound(); i <= equation.getUpBound(); i++) {
	    for (int j = equation.getLowBound(); j <= equation.getUpBound(); j++) {
		// create point with coordinates
		DPoint point = new DPoint(i, j);
		// create point key
		DKey key = point.getKey();
		// if this point is not in set, the add to set

		// compute value at point
		point.computeValue(
			equation.diffBtwSolutions(point.getX(), point.getY()),
			max);
		if (point.getValue() == 1.0) {
		    solutions++;
		    System.out.println(point);
		}
		// add point
		points.put(key, point);

	    }

	}
	System.out.println("Total number of points: " + points.size());
	System.out.println("There are " + solutions + " solutions in range");

    }

    static void draw(String name, int x, int y, ArrayList<DCoordinates> track)
	    throws IOException {
	// Graph to jpg, with name defined as parameter
	DGraph.draw(name + "_Diophantine " + equation, points, 6,
		equation.getUpBound(), x, y, track);
    }

}
