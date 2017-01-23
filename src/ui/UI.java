package ui;
/*
	N3Sim: A Simulation Framework for Diffusion-based Molecular Communication
    Copyright (C) 2011 Iñaki Pascual - N3CAT (UPC)
    N3Cat (NaNoNetworking Center in Catalunya)
	Universitat Politècnica de Catalunya (UPC)
	Jordi Girona, 1-3, Mòdul D6 (Campus Nord)
	08034 Barcelona, Catalunya, Spain
	acabello@ac.upc.edu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/




import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import data.DataInterface;
import domain.controller.DomainInterface;

public class UI {

	static DomainInterface _d; // communication with Domain Layer
	static File _configFile;
	private static ArrayList<String> _names; // lists of variables (names list and values list)
	private static ArrayList<String> _values;
	
	
	//TODO make only 1 call to create emitter, receiver... for all types.
	//TODO integrate 3D, questions for x, y, z always together. Improve understanding of config file.
	
	/**
	 * @param args
	 */
	public static void _main(String[] args) {
		
		_d = new DomainInterface();
		_names = new ArrayList<String>();
		_values = new ArrayList<String>();
		
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
					
		_d.createSimulator(outPath, graphics, infoFile, activeCollision, BMFactor, inertiaFactor, time, timeStep, xSize, ySize, D, bgConcentration, sphereRadius, boundedSpace, constantBGConcentration, constantBGConcentrationWidth);
		
		// createEmitters
		int numEmitters = getNextInt("emitters");
		double emitterRadius;
		double x;
		double y;
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
		
		for (int i=0; i< numEmitters; i++){
			emitterRadius = getNextDouble("emitterRadius");
			x = getNextDouble("x");
			y = getNextDouble("y");
			startTime = getNextDouble("startTime");
			endTime = getNextDouble("endTime");
			initV = getNextDouble("initV");
			punctual = getNextBoolean("punctual");
			concentrationEmitter = getNextBoolean("concentrationEmitter");
			color = getNextString("color");
			
			emitterType = getNextInt("emitterType");
			switch(emitterType){
			case(1):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100; //change to amplitude per timeStep
				_d.createPulseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color);
				break;		
			case(2):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100; //change to amplitude per timeStep
				period = getNextInt("period");
				timeOn = getNextInt("timeOn");
				_d.createRectangularSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, period, timeOn, color);
				break;
			case(3):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100; //change to amplitude per timeStep
				_d.createNoiseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color);
				break;
			case(4):
				file = getNextString("file");
				scaleFactor = getNextDouble("scaleFactor");	
				int size = (int)((double)(endTime - startTime)/timeStep);
				_d.createWaveFromFileSphereEmitter(x, y, startTime, endTime, scaleFactor, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, file, size, color);
				break;
			case(5):
				double z = getNextDouble("z");
				file = getNextString("file");
				scaleFactor = getNextDouble("scaleFactor");	
				int size4 = (int)((double)(endTime - startTime)/timeStep);
				_d.createPunctualWaveFromFile3DSphereEmitter(x, y, z, startTime, endTime, scaleFactor, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, file, size4, color);
				break;
			case(6):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message"); 
				period = getNextInt("period");
				_d.createCSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, Trans_startTime, message, period);
				break;
			case(7):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message"); 
				period = getNextInt("period");
				type = getNextInt("type");
				_d.createMOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, Trans_startTime, message, period,type);
				break;
			
			case(8):
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message"); 
				period = getNextInt("period");
				type = getNextInt("type");
				emitPeriod = getNextInt("emitPeriod");
				_d.createNEWMOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, Trans_startTime, message, period,type, emitPeriod);
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
				_d.createMessageSphereEmitter3D(x, y, z, startTime, endTime,
						amplitude, sphereRadius, emitterRadius, initV,
						punctual, concentrationEmitter, color, Trans_startTime,
						message, period, type, emitPeriod,file);
				break;
				
			case(10):
				z = getNextDouble("z");
				amplitude = getNextDouble("amplitude");
				amplitude = amplitude * timeStep / 100;
				Trans_startTime = getNextDouble("transStartTime");
				message = getNextString("message");
				period = getNextInt("period");
				type = getNextInt("type");
				emitPeriod = getNextInt("emitPeriod");
				file = getNextString("file");
				double initVx = getNextDouble("initVx");
				double initVy = getNextDouble("initVy");
				double initVz = getNextDouble("initVz");
				_d.createMobileEmitter(x, y, z, startTime, endTime,
						amplitude, sphereRadius, emitterRadius, initV,
						punctual, concentrationEmitter, color, Trans_startTime,
						message, period, type, emitPeriod,file,initVx,initVy,initVz);				
				break;
				
				
			default: error("Wrong type of emitter. Emitter not created. Try again"); i--;
			}	
		}
		
		// createReceivers		
		int numReceivers = getNextInt("receivers");
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
		for (int i=0; i< numReceivers; i++){
			name = getNextString("name");
			x = getNextDouble("x");
			y = getNextDouble("y");
			absorb = getNextBoolean("absorb");
			accumulate = getNextBoolean("accumulate");
			receiverType = getNextInt("receiverType");
			switch(receiverType){
			case(1):
				side = getNextDouble("side");
				_d.createSquareReceiver(name, x, y, absorb, accumulate, side); 
				break;		
			case(2):
				rradius = getNextDouble("rradius");
				_d.createSphericalReceiver(name, x, y, absorb, accumulate, rradius);
				break;
			case(3):
				double z = getNextDouble("z");
				rradius = getNextDouble("rradius");
				_d.create3DSphericalReceiver(name, x, y, z, absorb, accumulate, rradius);
				break;
			case(4):
				rradius = getNextDouble("rradius");
				_d.createColoredSphericalReceiver(name, x, y, absorb, accumulate, rradius);
				break;

			case (5):
				z = getNextDouble("z");
				rradius = getNextDouble("rradius");
				_d.createColoredSphericalReceiver3D(name, x, y, z, absorb,
						accumulate, rradius);
				break;
			case (6):
				z = getNextDouble("z");
				rradius = getNextDouble("rradius");
				symboltime = getNextDouble("symboltime");
				_d.createMessageSphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep);
				break;
			case (7):
				z = getNextDouble("z");
				rradius = getNextDouble("rradius");
				symboltime = getNextDouble("symboltime");
				initVx = getNextDouble("initVx");
				initVy = getNextDouble("initVy");
				initVz = getNextDouble("initVz");
				initVx = initVx * 1000 * timeStep / 1000000;        // um/ms
				initVy = initVy * 1000 * timeStep / 1000000;        // um/ms
				initVz = initVz * 1000 * timeStep / 1000000;        // um/ms
				_d.createMobileSphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep, initVx, initVy, initVz);
				break;
				
			default: error("Wrong type of emitter. Emitter not created. Try again"); i--;
			}
		}
		
		
		//start simulation
		System.out.println("");
		System.out.println("Simulation starts...");
		_d.startSimulation();
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
