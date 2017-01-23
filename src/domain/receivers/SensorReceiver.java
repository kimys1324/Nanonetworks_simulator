package domain.receivers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import ui.UIver2;
import domain.emitters.CSKSphereEmitter;
import domain.emitters.ParticleEmitter;
import domain.emitters.SensorEmitter;
import domain.particles.Particle;
import domain.particles.ParticleXComparator;
import domain.space.Simulator;
import domain.space.Space;

public class SensorReceiver extends ColoredSphericalReceiver3D {

	BigDecimal nextX;
	BigDecimal nextY;
	BigDecimal nextZ;
	BigDecimal initVx = new BigDecimal(0);
	BigDecimal initVy = new BigDecimal(0);
	BigDecimal initVz = new BigDecimal(0);
 
	private double nextSymboltime;
	private double thisSymboltime;
	private boolean timeSwitch;
	private boolean start = false;
	private double symbolTime;
	private boolean messageArrived = false;
	private String message;
	private String sendmessage;
	private int threshold;
	public static double timeStep;
	int cmax[] = { -1, -1, -1, -1, -1 };
	int cmaxInstance = 0;
	int[] cmaxBuffer = { -1, -1, -1, -1, -1 };
	int _timestep = 0;
	double totalCmax = 0;
	double avgCmax = 0;
	public static double distance = 0;
	static double dynamicSymboltime = 0;
	int _dataField[] = { 0, 0, 0, 0, 0 };
	int dataClock = 0;
	int totalClock = 0;
	double temp1, temp2, temp3;
	double distanceField[] = { -1, -1, -1, -1, -1 };
	public static double velocity = 0;
	static int generalClock = 0;
	private int signal1;
	private int signal2;
	private int signal3;
	private int signal4;
	private int signal5;
	int firstTime = 0;
	int secondTime = 0;
	int thirdTime = 0;
	int fourthTime = 0;
	int lastTime = 0;
	double ISI_con[] = { -1, -1, -1, -1 };
	double totalISI[] = { -1, -1, -1, -1 };
	double avgSet[] = { -1, -1, -1, -1, -1 };
	FileOutputStream outStream;
	double startTime;
	double totalTransmitTime;
	double Trans_endTime;
	private boolean sensor_check;
	private String outPath;
	
	public SensorReceiver(String name, double x, double y, double z,
			boolean absorb, boolean accumulate, double radius,
			double symboltime, double timeStep, double initVx, double initVy,
			double initVz, String outPath, String sendmessage) {
		super(name, x, y, z, absorb, accumulate, radius);
		// TODO Auto-generated constructor stub

		nextX = new BigDecimal(String.valueOf(x));
		nextY = new BigDecimal(String.valueOf(y));
		nextZ = new BigDecimal(String.valueOf(z));
		this.initVx = new BigDecimal(String.valueOf(initVx));
		this.initVy = new BigDecimal(String.valueOf(initVy));
		this.initVz = new BigDecimal(String.valueOf(initVz));

		this.symbolTime = symboltime;
		this.nextSymboltime = symboltime;
		this.timeSwitch = false;
		this.thisSymboltime = -1;
		this.message = "message : ";
		this.threshold = 80;
		this.timeStep = timeStep;
		this.sensor_check = false;
		this.outPath = outPath;
		this.sendmessage = sendmessage;
	}

	HashMap<Integer, Integer> complexcount(ArrayList<Particle> particlesList) {
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		HashMap<Integer, Integer> count = new HashMap<>();
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		int sum = 0;
		int cmaxSize = cmaxBuffer.length;
		int maxFinder = 0;
		double amplitude = ParticleEmitter.getAmplitude();
		double diffCoef = Space.getDiffCoef();
		while (it.hasNext()) {
			p = it.next();

			// System.out.println("particle x location"+p.getX());
			// System.out.println("particle z location"+p.getZ());
			if (p.getX() > _x + _radius)
				break;
			if (p.getX() >= _x - _radius) {
				if ((p.getX() - _x) * (p.getX() - _x) + (p.getY() - _y)
						* (p.getY() - _y) + (p.getZ() - _z) * (p.getZ() - _z) <= _radius
						* _radius) {

					// System.out.println("particle z location"+p.getZ());
					if (count.containsKey(new Integer(p.getType()))) {
						count.put(new Integer(p.getType()),
								new Integer(count.get(p.getType()) + 1));
					} else {
						count.put(new Integer(p.getType()), 1);
					}
					if (_absorb) {
						toDeleteList.add(p);
					}
				}
			}
		}

		for (Particle pa : toDeleteList)
			particlesList.remove(pa);

		if (count.containsKey(2))
			sum = new Integer(count.get(new Integer(2)));

		if (this.messageArrived == false && sum > 0) {
			if (sum > threshold)
				this.message += "*1*";
			this.thisSymboltime = 0;
			this.nextSymboltime = this.symbolTime;
			this.messageArrived = true;
		}

		if (thisSymboltime >= nextSymboltime) {
			if (this.timeSwitch == false && this.start == true) {
				this.message += "*0*";
			}
			System.out.println(this.message);
			this.thisSymboltime = nextSymboltime;
			this.nextSymboltime = this.thisSymboltime + symbolTime;
			this.timeSwitch = false;
		}

		if (this.timeSwitch == false && sum > threshold) {
			message += "1";
			this.timeSwitch = true;
			this.start = true;
		}

		this.thisSymboltime = this.thisSymboltime + timeStep;
		// System.out.println("thisSymboltime: "+ this.thisSymboltime);

		// System.out.println("_timestep: " + _timestep);
		if (_timestep < cmaxSize) {
			cmaxBuffer[_timestep] = sum;
			// System.out.println("cmaxSize: "+cmaxSize + "	" +
			// "cmaxBuffer[0]: " + cmaxBuffer[0]);
		} else {
			for (int i = 0; i < cmaxSize - 1; i++) {
				cmaxBuffer[i] = cmaxBuffer[i + 1];
			}
			cmaxBuffer[cmaxSize - 1] = sum;
		}

		for (int k = 0; k < cmaxSize; k++) {
			if (maxFinder < cmaxBuffer[k]) {
				maxFinder = cmaxBuffer[k];
			}
		}
		// System.out.println("maxFinder: " + maxFinder);

		if (maxFinder == cmaxBuffer[2] && maxFinder >= cmaxInstance) {
			for (int j = 0; j < cmaxSize; j++) {
				cmax[j] = cmaxBuffer[j];
			}
			cmaxInstance = maxFinder;
			// System.out.println("cmaxInstance: " + cmaxInstance);
		}

		if (cmax[0] != -1 && cmax[1] != -1 && cmax[2] != -1 && cmax[3] != -1) {

			// System.out.println("cmax[0]: " + cmax[0] + "	" + "cmax[1]: " +
			// cmax[1] + "	" + "cmax[2]:" + cmax[2] + "	" + "cmax[3]:" + cmax[3]
			// + "	" + "camx[4]: " + cmax[4]);

			totalCmax = cmax[0] + cmax[1] + cmax[2] + cmax[3] + cmax[4];

		}

		avgCmax = totalCmax / cmaxSize;

		// System.out.println("error checking 1");
		// System.out.println("check the avgCmax : " + avgCmax);

		totalClock++;

		if (totalClock * timeStep < symbolTime * cmaxSize) {
			_dataField[dataClock] = cmaxInstance;
		}

		// System.out.println("error checking 2");

		// System.out.println("dataField[0]: " + _dataField[0] + "	" +
		// "dataField[1]: "+ _dataField[1]+ "	" +
		// "dataField[2]: "+_dataField[2]+ "	" + "dataField[3]: "+_dataField[3]
		// + "	" + "dataField[4]: " +_dataField[4]);

		_timestep++;

		if (_timestep * timeStep > symbolTime) {
			_timestep = 0;
			cmaxInstance = 0;

			for (int l = 0; l < cmaxSize; l++) {
				cmaxBuffer[l] = -1;
				cmax[l] = -1;
			}

			if (signal1 == 0 && signal2 == 0 && signal3 == 0 && signal4 == 0
					&& signal5 == 0)
				signal1 += 1;
			else if (signal1 == 1 && signal2 == 0 && signal3 == 0
					&& signal4 == 0 && signal5 == 0)
				signal2 += 1;
			else if (signal1 == 1 && signal2 == 1 && signal3 == 0
					&& signal4 == 0 && signal5 == 0)
				signal3 += 1;
			else if (signal1 == 1 && signal2 == 1 && signal3 == 1
					&& signal4 == 0 && signal5 == 0)
				signal4 = 1;
			else if (signal1 == 1 && signal2 == 1 && signal3 == 1
					&& signal4 == 1 && signal5 == 0)
				signal5 += 1;
			else
				signal1 += 1;

			if (signal1 == 1 && signal2 == 0 && signal3 == 0 && signal4 == 0
					&& signal5 == 0 && avgCmax >= threshold) {
				firstTime = generalClock;
				avgSet[0] = avgCmax;

			}

			if (signal1 == 1 && signal2 == 1 && signal3 == 0 && signal4 == 0
					&& signal5 == 0 && avgCmax >= threshold && avgSet[0] != -1) {
				secondTime = generalClock;
				avgSet[1] = avgCmax;
			}

			if (signal1 == 1 && signal2 == 1 && signal3 == 1 && signal4 == 0
					&& signal5 == 0 && avgCmax >= threshold && avgSet[1] != -1) {
				thirdTime = generalClock;
				avgSet[2] = avgCmax;
			}

			if (signal1 == 1 && signal2 == 1 && signal3 == 1 && signal4 == 1
					&& signal5 == 0 && avgCmax >= threshold && avgSet[2] != -1) {
				fourthTime = generalClock;
				avgSet[3] = avgCmax;
			}

			if (signal1 == 1 && signal2 == 1 && signal3 == 1 && signal4 == 1
					&& signal5 == 1 && avgCmax >= threshold && avgSet[3] != -1) {
				lastTime = generalClock;
				avgSet[4] = avgCmax;

				ISI_con[0] = (amplitude / Math.pow(4 * Math.PI * diffCoef
						* ((lastTime * timeStep) / 1000000), 1.5))
						* Math.pow(Math.E,
								-(distanceField[0] * distanceField[0])
										/ (4 * diffCoef * lastTime * timeStep))
						* Math.pow(50 / 617.860, 3);
				ISI_con[1] = (amplitude / Math
						.pow(4
								* Math.PI
								* diffCoef
								* (((lastTime * timeStep) - symbolTime) / 1000000),
								1.5))
						* Math.pow(
								Math.E,
								-(distanceField[1] * distanceField[1])
										/ (4 * diffCoef * ((lastTime * timeStep) - symbolTime)))
						* Math.pow(50 / 617.860, 3);
				ISI_con[2] = (amplitude / Math
						.pow(4
								* Math.PI
								* diffCoef
								* (((lastTime * timeStep) - (2 * symbolTime)) / 1000000),
								1.5))
						* Math.pow(
								Math.E,
								-(distanceField[2] * distanceField[2])
										/ (4 * diffCoef * ((lastTime * timeStep) - (2 * symbolTime))))
						* Math.pow(50 / 617.860, 3);
				ISI_con[3] = (amplitude / Math
						.pow(4
								* Math.PI
								* diffCoef
								* (((lastTime * timeStep) - (3 * symbolTime)) / 1000000),
								1.5))
						* Math.pow(
								Math.E,
								-(distanceField[3] * distanceField[3])
										/ (4 * diffCoef * ((lastTime * timeStep) - (3 * symbolTime))))
						* Math.pow(50 / 617.860, 3);

				totalISI[3] = ISI_con[0] + ISI_con[1] + ISI_con[2] + ISI_con[3];

				ISI_con[0] = (amplitude / Math.pow(4 * Math.PI * diffCoef
						* ((fourthTime * timeStep) / 1000000), 1.5))
						* Math.pow(
								Math.E,
								-(distanceField[0] * distanceField[0])
										/ (4 * diffCoef * fourthTime * timeStep))
						* Math.pow(50 / 617.860, 3);
				ISI_con[1] = (amplitude / Math.pow(4 * Math.PI * diffCoef
						* (((fourthTime * timeStep) - symbolTime) / 1000000),
						1.5))
						* Math.pow(
								Math.E,
								-(distanceField[1] * distanceField[1])
										/ (4 * diffCoef * ((fourthTime * timeStep) - symbolTime)))
						* Math.pow(50 / 617.860, 3);
				ISI_con[2] = (amplitude / Math
						.pow(4
								* Math.PI
								* diffCoef
								* (((fourthTime * timeStep) - (2 * symbolTime)) / 1000000),
								1.5))
						* Math.pow(
								Math.E,
								-(distanceField[2] * distanceField[2])
										/ (4 * diffCoef * ((fourthTime * timeStep) - (2 * symbolTime))))
						* Math.pow(50 / 617.860, 3);

				totalISI[2] = ISI_con[0] + ISI_con[1] + ISI_con[2];

				ISI_con[0] = (amplitude / Math.pow(4 * Math.PI * diffCoef
						* ((thirdTime * timeStep) / 1000000), 1.5))
						* Math.pow(Math.E,
								-(distanceField[0] * distanceField[0])
										/ (4 * diffCoef * thirdTime * timeStep))
						* Math.pow(50 / 617.860, 3);
				ISI_con[1] = (amplitude / Math.pow(4 * Math.PI * diffCoef
						* (((thirdTime * timeStep) - symbolTime) / 1000000),
						1.5))
						* Math.pow(
								Math.E,
								-(distanceField[1] * distanceField[1])
										/ (4 * diffCoef * ((thirdTime * timeStep) - symbolTime)))
						* Math.pow(50 / 617.860, 3);

				totalISI[1] = ISI_con[0] + ISI_con[1];

				ISI_con[0] = (amplitude / Math.pow(4 * Math.PI * diffCoef
						* ((secondTime * timeStep) / 1000000), 1.5))
						* Math.pow(
								Math.E,
								-(distanceField[0] * distanceField[0])
										/ (4 * diffCoef * secondTime * timeStep))
						* Math.pow(50 / 617.860, 3);

				totalISI[0] = ISI_con[0];

				distanceField[0] = Math.pow(3 / (2 * Math.PI * Math.E), 0.5)
						* Math.pow(amplitude / avgSet[0], 0.33) * (50 / 617.86)
						* 1000;
				distanceField[1] = Math.pow(3 / (2 * Math.PI * Math.E), 0.5)
						* Math.pow(amplitude / (avgSet[1] - totalISI[0]), 0.33)
						* (50 / 617.86) * 1000;
				distanceField[2] = Math.pow(3 / (2 * Math.PI * Math.E), 0.5)
						* Math.pow(amplitude / (avgSet[2] - totalISI[1]), 0.33)
						* (50 / 617.86) * 1000;
				distanceField[3] = Math.pow(3 / (2 * Math.PI * Math.E), 0.5)
						* Math.pow(amplitude / (avgSet[3] - totalISI[2]), 0.33)
						* (50 / 617.86) * 1000;
				distanceField[4] = Math.pow(3 / (2 * Math.PI * Math.E), 0.5)
						* Math.pow(amplitude / (avgSet[4] - totalISI[3]), 0.33)
						* (50 / 617.86) * 1000;

				velocity = ((distanceField[0] - distanceField[4])
						/ (((lastTime - firstTime) * timeStep)) * 1000);
				distance = distanceField[4];
				dynamicSymboltime = (1.9288 / diffCoef) * (distance * distance);

				// System.out.println("distanceField[0]: " + distanceField[0] +
				// "	" + "distanceField[1]: "+ distanceField[1]+ "	" +
				// "distanceField[2]: "+distanceField[2]+ "	" +
				// "distanceField[3]: "+distanceField[3] + "	" +
				// "distanceField[4]: " +distanceField[4]);
				System.out.println("velocity: " + velocity + "	" + "distance: "
						+ distance + "	" + "dynamic symbol time: "
						+ dynamicSymboltime);

				try {
					outStream = new FileOutputStream(outPath + "/sensor"
							+ ".txt", true);
					String _write = "Test Case\n"
							+ "initialDistance : "
							+ Double.toString(distanceField[0])
							+ "\n"
							+ "remainDistance : "
							+ Double.toString(distanceField[4])
							+ "\n"
							+ "velocity : "
							+ Double.toString(velocity)
							+ "\n"
							+ "movedDistance : "
							+ Double.toString(distanceField[0]
									- distanceField[4])
							+ "\n"
							+ "5 symboltime length : "
							+ Double.toString((lastTime - firstTime) * timeStep)
							+ "\n\n";
					byte[] strByte = _write.getBytes();
					outStream.write(strByte);
					outStream.close();
				} catch (IOException e) {
					System.err.println(e); // ������ �ִٸ� �޽��� ���
					System.exit(1);
				}

				// System.out.println("check error sensorReceiver");
				CalTotalTransmitTime();
				/*
				SensorEmitter.Trans_endTime = _timestep * timeStep
						+ totalTransmitTime;
				SensorEmitter.Trans_startTime = timeStep * generalClock;
				System.out.println("TransStartTimeFromSensorReceiver: "
						+ generalClock * timeStep + "	" + "trans_endTime: "
						+ Trans_endTime);
				 
				
				SensorEmitter.dynamicSymboltime = dynamicSymboltime;
				SensorEmitter.distance = distance;
				SensorEmitter.tempD = distance;
				SensorEmitter.velocity = velocity;
				*/
				this.set_sensor_true();
				UIver2.sensor_start_signal = 1;
				startTime = timeStep * generalClock;
			}
			dataClock++;

			if (dataClock == 5) {
				dataClock = 0;
			}

		}

		generalClock++;

		return count;
	}
	
	public double getTrans_endTime()
	{
		double trans_endTime = _timestep * timeStep
				+ totalTransmitTime;
	
		return trans_endTime;
	}
	
	public double getTrans_startTime()
	{
		double trans_startTime = timeStep * generalClock;
	
		return trans_startTime;
	}
	
	public double getDynamicSymboltime()
	{
		return this.dynamicSymboltime;
	}
	
	public double getDistance()
	{
		return this.distance;
	}
	
	public double getTempD()
	{
		return distance;
	}
	public double getVelocity()
	{
		return velocity;
	}
	
	public void updatePosition() {
		/*
		 * if(_x!=nextX.doubleValue() || _y!=nextY.doubleValue() ||
		 * _z!=nextZ.doubleValue() ){ System.out.println("prevX: " + _x +
		 * " nextX: "+ nextX); System.out.println("prevY: " + _y + " nextY: "+
		 * nextY); System.out.println("prevZ: " + _z + " nextZ: "+ nextZ); }
		 */
		_x = nextX.doubleValue();
		_y = nextY.doubleValue();
		_z = nextZ.doubleValue();

	}

	public void makeMotion() {
		nextX = nextX.add(initVx);
		nextY = nextY.add(initVy);
		nextZ = nextZ.add(initVz);

	}

	public void CalTotalTransmitTime() {
		double temp = dynamicSymboltime;
		double currentD;
		double ST = dynamicSymboltime;
		int i = 0;

		currentD = distance;

		while (i < this.message.length()) {
			currentD = currentD - velocity * ST * 0.001;
			ST = 1.9288 * currentD * currentD;
			temp += ST;

			i++;
		}

		totalTransmitTime = temp;
	}

	public double getStartTime() {
		// TODO Auto-generated method stub
		return startTime;
	}

	public double getTimeStep() {
		// TODO Auto-generated method stub
		return timeStep;
	}

	public double getTotalTrnasmitTime() {
		// TODO Auto-generated method stub
		return Trans_endTime;
	}

	public void set_sensor_false()
	{
		this.sensor_check = false;
	}
	
	public void set_sensor_true()
	{
		this.sensor_check=true;
	}
	
	public boolean get_sensorcheck()
	{
		return this.sensor_check;
	}
}
