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

package domain.space;

import java.text.DecimalFormat;

import data.DataInterface;
import domain.emitters.Emitter;
import domain.receivers.Receiver;

public class Simulator {
	
	String _outPath;
	double _time;
	double _endTime;	
	static double _timeStep;
	public static double currentDST;
	boolean _graphics;
	boolean _infoFile;
	Space _space;
	long _simulationSystemTime;
	double currnetDST;
	public static double nextDST;
	public static double startSpot;
	public static double endSpot;
//	static String _DFile = "_D.csv";

	public Simulator (String path, boolean graphics, boolean infoFile, double time, double timeStep){
		_time = 0;
		_endTime = time;
		_timeStep = timeStep;
		_space = null;
		_graphics = graphics;
		_infoFile = infoFile;
		if(_graphics){
			DataInterface.addWriter(DataInterface.getGraphFile());
		}
		//if(_infoFile){
			DataInterface.addWriter(DataInterface.getInfoFile());
		//}
	}
	
	public void createSpace (int xSize, int ySize, double D, double bgConcentration, double radius, boolean activeCollision, double BMFactor, double inertiaFactor, boolean boundedSpace, boolean constantBGConcentration, double constantBGConcentrationWidth, boolean infoFile){
		_space = new Space(xSize, ySize, D, bgConcentration, radius, activeCollision, BMFactor, inertiaFactor, boundedSpace, constantBGConcentration, constantBGConcentrationWidth, infoFile);
	}
	
	public void addEmitter(Emitter e) {
		_space.addEmitter(e);
	}

	public void addReceiver(Receiver r) {
		_space.addReceiver(r);
		DataInterface.addWriter(r.getFileName());
	}

	public void start() {
		// write the simulation information to file
		DataInterface.writeLineToFile(DataInterface.getSimulationFile(), simulatorToString());
		if(_graphics) DataInterface.writeLineToFile(DataInterface.getGraphFile(), _space.getXSize() + ":" + _space.getYSize());
		// for time cost information
		long iniTime = System.currentTimeMillis();
		
//		String calculatedD;
		// initial background concentration
		_space.setInitialParticles();
		for(int i = 0; i<2 ; i++)
			_space.initialParticlesMove(_timeStep);
		
		// let's make _space run the simulation!
		// which means
		// 1. emit particles
		// 2. count particles on receivers
		// 3. move particles
		// 4. solve collisions
		// 5. update positions
		
		long lastTime = System.currentTimeMillis();

		while(_time < _endTime){
			//System.out.println(".");
			if(_infoFile) 
				DataInterface.writeLineToFile(DataInterface.getInfoFile(), "time:"+ strPre(_time));
			//emission
			_space.emit(_time);
			if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "emission time:"+ (System.currentTimeMillis()-lastTime) );
			//receivers
			if(_infoFile) lastTime = System.currentTimeMillis();
			
			// Modified Sensor
			_space.updateReceivers(_time);	
			//_space.updateMachines(_time);
			
			if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "receivers counting time:"+ (System.currentTimeMillis()-lastTime) );
			//graphics
			if(_infoFile) lastTime = System.currentTimeMillis();
			if(_graphics){
				_space.spaceToGraphics(_time);
			}
			if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "graphics time:"+ (System.currentTimeMillis()-lastTime) );
			//motion
			if(_infoFile) lastTime = System.currentTimeMillis();
			_space.brownianMotionAndInertiaStep(_timeStep);
			
			
			
			//new
			//receiver motion
			_space.receiverMotion();
			
			
			
			if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "motion time:"+ (System.currentTimeMillis()-lastTime) );
			//collisions
			if(_infoFile) lastTime = System.currentTimeMillis();
			_space.checkCollisions();
			
			if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "collisions time:"+ (System.currentTimeMillis()-lastTime) );

//			//velocity file
//			line = strPre(_time) + DataInterface.getCsvSeparator() + _space.calculateVelocity();
//			DataInterface.writeLineToFile(DataInterface.getVelocityFile(), line);

			//update positions
			if(_infoFile) lastTime = System.currentTimeMillis();
			_space.updatePositions();
			
			
			//new
			//receiver update positions
			_space.receiverUpdatepositions();
			
			
			if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "update positions time:"+ (System.currentTimeMillis()-lastTime) );
			//calculate D
			if(_infoFile) lastTime = System.currentTimeMillis();
//			calculatedD = _space.calculateD(_time);
//			DataInterface.writeLineToFile(_DFile, calculatedD);
			//end
			_time += _timeStep;
		}
		_simulationSystemTime = System.currentTimeMillis() - iniTime;
		if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "execution time (s): "+ _simulationSystemTime/1000);
		DataInterface.close();
	}

	private String simulatorToString(){
		int emitter = 1;
		
		String s= "SIMULATION PARAMS" + "\n";
		s += "*****************" + "\n";
		s += "simulation time (ns): " + _endTime + "\n";
		s += "timestep (ns): " + _timeStep + "\n";
		s += "activate collisions: " + _space.getActiveCollision() + "\n";
		s += "Brownian Motion Factor: " + _space.getBMFactor() + "\n";
		s += "Inertia Factor: " + _space.getInertiaFactor() + "\n";
		s += "\n";
		s += "SPACE PARAMS" + "\n";
		s += "************" + "\n";
		s += _space.getDescription();
		s += "EMITTERS PARAMS" + "\n";
		s += "***************" + "\n";
		for(Emitter e : _space.getEmittersList()){
			s += "Emitter " + emitter + " data : \n";
			s += "type: " + e.getClass().getSimpleName() + "\n";
			s += e.getDescription();
			s += "\n";
			emitter ++;
		}
		s += "\n";
		s += "RECEIVERS PARAMS" + "\n";
		s += "****************" + "\n";
		for(Receiver r : _space.getReceiversList()){
			s += "type: " + r.getClass().getSimpleName() + "\n";
			s += r.getDescription();
			s += "\n";
		}
		return s;
	}

	static double getTimeStep() {
		return _timeStep;
	}

	//TODO move this to data layer
	private String strPre(double inValue){
		DecimalFormat twoDec = new DecimalFormat("0.00");
		twoDec.setGroupingUsed(false);
		return twoDec.format(inValue);
	}

}
