package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import data.DataInterface;
import domain.controller.DomainInterface;
import domain.controller.MachineDomainInterface;

public class UIver2 {

	static MachineDomainInterface _md; // communication with Domain Layer
	static File _configFile;
	private static ArrayList<String> _names; // lists of variables (names list and values list)
	private static ArrayList<String> _values;
	public static int sensor_start_signal;
	
	
	//TODO make only 1 call to create emitter, receiver... for all types.
	//TODO integrate 3D, questions for x, y, z always together. Improve understanding of config file.
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		_md = new MachineDomainInterface();
		_names = new ArrayList<String>();
		_values = new ArrayList<String>();
		sensor_start_signal=0;
		
		// if no argument is received, read values from file or standard input
		// else read from config file
		if(args == null || args.length == 0){
			//obsolete
			// ConsoleUI.readFromStdIn(_names, _values);
		}
		else{ 
			try {
				FileUI.readFromFile(_names, _values, args);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// create simulator and space
			// simulator params
		String outPath = getNextString("outPath");
		//create Data Interface asap so we can log errors
		DataInterface.init(outPath);
		boolean graphics = getNextBoolean("graphics");
		boolean infoFile = getNextBoolean("infoFile");
		boolean activeCollision = getNextBoolean("activeCollision");
		double BMFactor = getNextDouble("BMFactor");
		double inertiaFactor = getNextDouble("inertiaFactor");
		double time = getNextDouble("time");
		double timeStep = getNextDouble("timeStep");
			// space params
		boolean boundedSpace = getNextBoolean("boundedSpace");
		boolean constantBGConcentration = getNextBoolean("constantBGConcentration");
		double constantBGConcentrationWidth = getNextDouble("constantBGConcentrationWidth");
		int xSize = getNextInt("xSize");
		int ySize = getNextInt("ySize");
		double D = getNextDouble("D");
		double bgConcentration = getNextDouble("bgConcentration");
		double sphereRadius = getNextDouble("sphereRadius");
					
		_md.createSimulator(outPath, graphics, infoFile, activeCollision, BMFactor, inertiaFactor, time, timeStep, xSize, ySize, D, bgConcentration, sphereRadius, boundedSpace, constantBGConcentration, constantBGConcentrationWidth);
		
		//create Cells
		
		//Emitter Part
		int numCells = getNextInt("cells");
		double cellRadius;
		double x;
		double y;
		double z=0;
		double startTime;
		double endTime;
		double initV;
		boolean punctual;
		boolean concentrationEmitter;
		String color;
		int emitterType;
		double amplitude;
		int period;
		int timeOn;
		String file;
		double scaleFactor;
		double Trans_startTime;
		String message;
		int type;
		int emitPeriod;
		
		//Receiver Part
		String name;
		int receiverType;
		boolean absorb;
		boolean accumulate;
		double side;
		double rradius;
		double symboltime;
		double initVx;
		double initVy;
		double initVz;
		
		for (int i=0 ; i< numCells ; i++)
		{
			int index=i;
			//Add Emitter Part
			cellRadius = getNextDouble("cellRadius");
			x = getNextDouble("x");
			y = getNextDouble("y");
			startTime = getNextDouble("startTime");
			endTime = getNextDouble("endTime");
			initV = getNextDouble("initV");
			punctual = getNextBoolean("punctual");
			concentrationEmitter = getNextBoolean("concentrationEmitter");
			color = getNextString("color");
			emitterType = getNextInt("emitterType");
			message = null;
			switch(emitterType){
			case(1):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100; //change to amplitude per timeStep
				_md.createPulseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, color);
				break;		
			case(2):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100; //change to amplitude per timeStep
				period = getNextInt("period");
				timeOn = getNextInt("timeOn");
				_md.createRectangularSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, period, timeOn, color);
				break;
			case(3):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100; //change to amplitude per timeStep
				_md.createNoiseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, color);
				break;
			case(4):
				file = getNextString("file");
				scaleFactor = getNextDouble("scaleFactor");	
				int size = (int)((double)(endTime - startTime)/timeStep);
				_md.createWaveFromFileSphereEmitter(x, y, startTime, endTime, scaleFactor, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, file, size, color);
				break;
			case(5):
				z = getNextDouble("z");
				file = getNextString("file");
				scaleFactor = getNextDouble("scaleFactor");	
				int size4 = (int)((double)(endTime - startTime)/timeStep);
				_md.createPunctualWaveFromFile3DSphereEmitter(x, y, z, startTime, endTime, scaleFactor, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, file, size4, color);
				break;
			case(6):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message"); 
				period = getNextInt("period");
				_md.createCSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, color, Trans_startTime, message, period);
				break;
			case(7):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message"); 
				period = getNextInt("period");
				type = getNextInt("type");
				_md.createMOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, color, Trans_startTime, message, period,type);
				break;
							
			case(8):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message"); 
				period = getNextInt("period");
				type = getNextInt("type");
				emitPeriod = getNextInt("emitPeriod");
				_md.createNEWMOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, cellRadius, initV,punctual, concentrationEmitter, color, Trans_startTime, message, period,type, emitPeriod);
				break;
				
			case (9):
				z = getNextDouble("z");
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message");
				period = getNextInt("period");
				type = getNextInt("type");
				emitPeriod = getNextInt("emitPeriod");
				file = getNextString("file");
				_md.createMessageSphereEmitter3D(x, y, z, startTime, endTime,
						amplitude, sphereRadius, cellRadius, initV,
						punctual, concentrationEmitter, color, Trans_startTime,
						message, period, type, emitPeriod,file);				
				break;
				
			case (10):
				z = getNextDouble("z");
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message");
				period = getNextInt("period");
				type = getNextInt("type");
				emitPeriod = getNextInt("emitPeriod");
				file = getNextString("file");
				initVx = getNextDouble("initVx");
				initVy = getNextDouble("initVy");
				initVz = getNextDouble("initVz");
				_md.createMobileEmitter(x, y, z, startTime, endTime,
						amplitude, sphereRadius, cellRadius, initV,
						punctual, concentrationEmitter, color, Trans_startTime,
						message, period, type, emitPeriod,file,initVx,initVy,initVz);				
				break;
				
			case (11):
				z = getNextDouble("z");
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message");
				period = getNextInt("period");
				type = getNextInt("type");
				emitPeriod = getNextInt("emitPeriod");
				file = getNextString("file");
				initVx = getNextDouble("initVx");
				initVy = getNextDouble("initVy");
				initVz = getNextDouble("initVz");
				_md.createSensorEmitter(x, y, z, startTime, endTime,
						amplitude, sphereRadius, cellRadius, initV,
						punctual, concentrationEmitter, color, Trans_startTime,
						message, period, type, emitPeriod,file,initVx,initVy,initVz, timeStep);				
				break;
				
			default: error("Wrong type of emitter. Emitter not created. Try again"); i--;
			}
			
			
			//Receiver Part
			
			name = getNextString("name");  //outstream file name
			absorb = getNextBoolean("absorb");
			accumulate = getNextBoolean("accumulate");
			receiverType = getNextInt("receiverType");
			rradius = cellRadius;
			switch(receiverType){
			case(1):
				side = getNextDouble("side");
				_md.createSquareReceiver(name, x, y, absorb, accumulate, side); 
				break;		
			case(2):
				_md.createSphericalReceiver(name, x, y, absorb, accumulate, rradius);
				break;
			case(3):
				_md.create3DSphericalReceiver(name, x, y, z, absorb, accumulate, rradius);
				break;
			case(4):
				_md.createColoredSphericalReceiver(name, x, y, absorb, accumulate, rradius);
				break;

			case (5):
				_md.createColoredSphericalReceiver3D(name, x, y, z, absorb,
						accumulate, rradius);
				break;
			case (6):
				symboltime = getNextDouble("symboltime");
				_md.createMessageSphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep);
				break;
			case (7):
				symboltime = getNextDouble("symboltime");
				initVx = getNextDouble("initVx");
				initVy = getNextDouble("initVy");
				initVz = getNextDouble("initVz");
				initVx = initVx * 1000 * timeStep / 1000000;        // um/ms
				initVy = initVy * 1000 * timeStep / 1000000;        // um/ms
				initVz = initVz * 1000 * timeStep / 1000000;        // um/ms
				_md.createMobileSphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep, initVx, initVy, initVz);
				break;
			case (8):
				symboltime = getNextDouble("symboltime");
				initVx = getNextDouble("initVx");
				initVy = getNextDouble("initVy");
				initVz = getNextDouble("initVz");
				initVx = initVx * 1000 * timeStep / 1000000;        // um/ms
				initVy = initVy * 1000 * timeStep / 1000000;        // um/ms
				initVz = initVz * 1000 * timeStep / 1000000;        // um/ms
				_md.createSensorReceiver(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep, initVx, initVy, initVz, outPath, message);
				break;
				
				
			default: error("Wrong type of emitter. Emitter not created. Try again"); i--;
			}
			
			_md.addMachine();			
		}
		
		
		//start simulation
		System.out.println("");
		System.out.println("Simulation starts...");
		_md.startSimulation();
		System.out.println("");
		System.out.println("Simulation finished!");
	}
	
	private static String getNextString(String name){
		String foundName = _names.remove(0);
		String value = _values.remove(0);
		if(foundName.equalsIgnoreCase(name)) return value;
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return null;
	}
	
	private static boolean getNextBoolean(String name){
		String foundName = _names.remove(0);
		String strValue =_values.remove(0);
		boolean value = false;
		
		if(foundName.equalsIgnoreCase(name)){
			if(strValue.equalsIgnoreCase("y") || strValue.equalsIgnoreCase("yes")){
				value = true;
			}
			else if(strValue.equalsIgnoreCase("n") || strValue.equalsIgnoreCase("no")){
				value = false;
			}
			else{	
				try {
					value = Boolean.parseBoolean(strValue);
				} catch (Exception e) {
					error("ERROR : UI : parameter " + name + " , expected to be boolean, found " + strValue +". Program terminates.");
					e.printStackTrace();
					System.exit(1);
				}
			}
			return value;
		}
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return value;
	}
	
	private static int getNextInt(String name){
		String foundName = _names.remove(0);
		String strValue =_values.remove(0);
		int value = 0;
		
		if(foundName.equalsIgnoreCase(name)){
			try {
				value = Integer.parseInt(strValue);
			} catch (Exception e) {
				error("ERROR : UI : parameter " + name + " , expected to be int, found " + strValue +". Program terminates.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return value;
	}
	
	private static double getNextDouble(String name){
		String foundName = _names.remove(0);
		String strValue =_values.remove(0);
		strValue = strValue.replace(',', '.');
		double value = 0;
		
		if(foundName.equalsIgnoreCase(name)){
			try {
				value = Double.parseDouble(strValue);
			} catch (Exception e) {
				error("ERROR : UI : parameter " + name + " , expected to be double, found " + strValue +". Program terminates.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return value;
	}
	
	protected static void error(String errorMsg) {
		DataInterface.writeLineToFile(DataInterface.getErrorsFile(), errorMsg);
		System.out.println(errorMsg);
	}
}
