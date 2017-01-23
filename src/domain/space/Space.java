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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import data.DataInterface;
import domain.boundaries.HorizontalBoundary;
import domain.boundaries.VerticalBoundary;
import domain.collision.Collisions;
import domain.emitters.Emitter;
import domain.emitters.MessageSpehereEmitter3D;
import domain.emitters.SensorEmitter;
import domain.particles.Particle;
import domain.particles.ParticleMinXComparator;
import domain.particles.ParticleXComparator;
import domain.particles.Sphere;
import domain.particles.Sphere3D;
import domain.receivers.Receiver;
import domain.receivers.SensorReceiver;

public class Space {

	static // TODO Descomprimir aqueta classe

	double _D; // diffusion coefficient (nm^2/ns)
	double _bgConcentration; // initial concentration rate. Number spheres per 10000 nm2
	double _radius; // radius of the spheres of the initial concentration
	double _xSize;
	double _ySize;
	boolean _activeCollision;
	double _BMFactor;
	double _inertiaFactor;
	boolean _boundedSpace;
	boolean _constantBGConcentration;
	double _constantBGConcentrationWidth;
	boolean _infoFile;
	
	ArrayList<Emitter> _emittersList;
	ArrayList<Receiver> _receiversList;
	ArrayList<Particle> _particlesList;
	ArrayList<HorizontalBoundary> _horizontalBoundariesList;
	ArrayList<VerticalBoundary> _verticalBoundariesList;
	
	Space (int xSize, int ySize, double D, double bgConcentration, double radius, boolean activeCollision, double BMFactor,
	double inertiaFactor, boolean boundedSpace, boolean constantBGConcentration, double constantBGConcentrationWidth, boolean infoFile){
		_xSize = (double)xSize;
		_ySize = (double)ySize;
		_activeCollision = activeCollision;
		_inertiaFactor = inertiaFactor;
		_BMFactor = BMFactor;
		_boundedSpace = boundedSpace;
		_constantBGConcentration = constantBGConcentration;
		_constantBGConcentrationWidth = constantBGConcentrationWidth;
		_infoFile = infoFile;
		_D = D;
		_bgConcentration = bgConcentration; //background concentration (particles/10000 nm2)
		_radius = radius;
		_emittersList = new ArrayList<Emitter>();
		_receiversList = new ArrayList<Receiver>();
		_particlesList = new ArrayList<Particle>();
		_verticalBoundariesList = new ArrayList<VerticalBoundary>();
		_horizontalBoundariesList = new ArrayList<HorizontalBoundary>();		
		if(_boundedSpace){
			VerticalBoundary left = new VerticalBoundary(0, 0, ySize);
			VerticalBoundary right = new VerticalBoundary(xSize, 0, ySize);
			_verticalBoundariesList.add(left);
			_verticalBoundariesList.add(right);
			HorizontalBoundary bottom = new HorizontalBoundary(0,0, xSize);
			HorizontalBoundary top = new HorizontalBoundary(0, ySize, xSize);
			_horizontalBoundariesList.add(bottom);
			_horizontalBoundariesList.add(top);
		}		
	}
	
	void addEmitter(Emitter e){
		_emittersList.add(e);
	}
	
	void addReceiver(Receiver r){
		_receiversList.add(r);
	}

//	void brownianMotionStep(double timeStep){
//		double k = Math.sqrt(2*_D*timeStep);
//		for(Particle p : _particlesList){
//			p.makeBMStep(k);
//		}
//	}
	
	void brownianMotionAndInertiaStep(double timeStep){
		double k = Math.sqrt(2*_D*timeStep);
		for(Particle p : _particlesList){
			p.makeBMAndInertiaStep(k, _BMFactor, _inertiaFactor);
		}
	}
	
	void emit(double time) {
		for(Emitter e : _emittersList)
			{
			e.emit(time, _particlesList);
			//MessageSpehereEmitter3D a = (MessageSpehereEmitter3D) e;
			//System.out.println(a.getNewThings());
			}
		if(_infoFile) DataInterface.writeLineToFile(DataInterface.getInfoFile(), "number of particles : " + _particlesList.size());
//		for(Particle p : _particlesList){
//			System.out.println(p.toString());
//		}
	}

	void updatePositions() {

		// check no particles go out of space
		if(_boundedSpace){	
			for(Particle p : _particlesList){
				if (p.getNextX() < 0 || p.getNextX() > _xSize || p.getNextY() < 0 || p.getNextY() > _ySize){
					DataInterface.writeLineToFile(DataInterface.getErrorsFile(), "ERROR : Particle out of bounds!");
				}
			}
		}
		
		// update positions
		for(Particle p : _particlesList)
			p.updatePosition();
		
		// if simulating infinite space, delete particles according Ficks Law		
		if(_constantBGConcentration){
			ArrayList<Particle> auxToDeleteList = new ArrayList<Particle>();
			double maxX = _xSize-_constantBGConcentrationWidth;
			double maxY = _ySize-_constantBGConcentrationWidth;
	
			Random r = new Random();
			for(Particle p : _particlesList){
				if(p.getX() < _constantBGConcentrationWidth || p.getX() > maxX ||p.getY() < _constantBGConcentrationWidth || p.getY() > maxY){
					auxToDeleteList.add(p);
				}
			}
			
			Particle pa;
			int n = numParticlesToDelete();
			if( n > auxToDeleteList.size()) n = auxToDeleteList.size();
			int i=0;
		
			while(i < n){
				pa = auxToDeleteList.remove((int)(r.nextDouble()*auxToDeleteList.size()));
				_particlesList.remove(pa);
				i++;
			}
		}
	}
	
	//new
	//implements receivers' updated position
	void receiverUpdatepositions() {
		
		for(Receiver r : _receiversList){
			r.updatePosition();
		}
	}
	
	//new
	//implements receivers' motion
		public void receiverMotion() {
			for(Receiver r : _receiversList){
			r.makeMotion();
		}
	}

	private int numParticlesToDelete(){

		int n1 = 0; //number of particles in first boundary area
		int n2 = 0; //number of particles in second boundary area
		
		double area1 = _xSize*_ySize - (_xSize - 2*_constantBGConcentrationWidth)*(_ySize - 2*_constantBGConcentrationWidth);
		double area2 = _xSize*_ySize - (_xSize - 4*_constantBGConcentrationWidth)*(_ySize - 4*_constantBGConcentrationWidth);
		
		double maxX2 = _xSize-2*_constantBGConcentrationWidth;
		double maxY2 = _ySize-2*_constantBGConcentrationWidth;
		
		double maxX1 = _xSize-_constantBGConcentrationWidth;
		double maxY1 = _ySize-_constantBGConcentrationWidth;
		
		for(Particle p : _particlesList){
			
			if(p.getX() < 2*_constantBGConcentrationWidth || p.getX() > maxX2 ||p.getY() < 2*_constantBGConcentrationWidth || p.getY() > maxY2){
				n2++;
				if(p.getX() < _constantBGConcentrationWidth || p.getX() > maxX1 ||p.getY() < _constantBGConcentrationWidth || p.getY() > maxY1){
					n1++;
				}
			}
		}
		
		double CR1 = ((double)(n1))/area1;
		double CR2 = ((double)(n2-n1))/(area2-area1);
		if(CR2 <= CR1) return 0;
		double CRFinal = CR1 - (CR2-CR1);
		if (CRFinal*10000 <= _bgConcentration) return 0;
		// TODO do not use Simulator.getTimeStep
		double timeStep = (double)(Simulator.getTimeStep());
		double nFick = 2*(double)(_xSize+_ySize)*timeStep*_D*(CR2-CR1)/_constantBGConcentrationWidth;
		return (int)nFick;
	}

	void updateReceivers(double time) {
		Collections.sort(_particlesList, new ParticleXComparator());
		for(int j=0; j<_receiversList.size() ; j++){
			
			Receiver r = _receiversList.get(j);
			Emitter e = _emittersList.get(j);
			
			
			if(r.isColored()>0){

				HashMap<Integer,Integer> numParticles = r.ccount(_particlesList);
				
				if(r.isColored()>1)
				{

					if(((SensorReceiver)r).get_sensorcheck())
					{
						((SensorEmitter)e).setTrans_endTime(((SensorReceiver)r).getTrans_endTime());
						((SensorEmitter)e).setTrans_startTime(((SensorReceiver)r).getTrans_startTime());
						((SensorEmitter)e).setDynamicSymbolTime(((SensorReceiver)r).getDynamicSymboltime());
						((SensorEmitter)e).setDistance(((SensorReceiver)r).getDistance());
						((SensorEmitter)e).setTempD(((SensorReceiver)r).getTempD());
						((SensorEmitter)e).setVelocity(((SensorReceiver)r).getVelocity());
						((SensorReceiver)r).set_sensor_false();
					}
				}
				
				String line = strPre(time);
				Iterator<Integer> iteratorKey = numParticles.keySet( ).iterator( );
				int key = -1;
				if(iteratorKey.hasNext()) key = iteratorKey.next();
				int i = 0;
				while(true && key != -1){
					if(numParticles.containsKey(i)){
						line += DataInterface.getCsvSeparator() + numParticles.get(key); 
						if(iteratorKey.hasNext()){
							key = iteratorKey.next();
						}
						else break;
					}
					else{
						line += DataInterface.getCsvSeparator() + (int)0;
					}
					i++;
				}	
				
			
				DataInterface.writeLineToFile(r.getFileName(), line);	
								
			}
			else{
				int numParticles;
				numParticles = r.count(_particlesList);
				DataInterface.writeLineToFile(r.getFileName(), strPre(time) + DataInterface.getCsvSeparator() + numParticles);
			}
		}
	}


	// COLLISIONS - see package collisions
	
	void checkCollisions() {
		Collections.sort(_particlesList, new ParticleMinXComparator());

		if(_activeCollision || _boundedSpace)
			Collisions.modifySpace(this);
	}

	// INITIAL / FINAL TASKS

	void setInitialParticles() {
		if(_bgConcentration > 0){
			int numParticles = (int)((_bgConcentration*(double)(_xSize)*(double)(_ySize))/10000);
//			System.out.println("numParticles : "+ numParticles);
			double Area = _xSize * _ySize;
			double squareSide = Math.sqrt(Area/numParticles);
//			System.out.println("squareSide : "+ squareSide);	
			if(squareSide <= _radius) System.out.println("WARNING : initial concentration too high!");
			
			int xdiv = (int)((double)(_xSize)/squareSide);
			int ydiv = (int)((double)(_ySize)/squareSide);
			
			while(xdiv*ydiv<numParticles){
				xdiv++;
				if(xdiv*ydiv<numParticles) ydiv++;
			}

			double deltax = (double)(_xSize)/(double)(xdiv);
			double deltay = (double)(_ySize)/(double)(ydiv);
//			System.out.println("deltax : "+ deltax);	
//			System.out.println("deltay : "+ deltay);	
			
			
			
			int i = 0;
			double x = deltax/2;
			double y = deltay/2;
			boolean end = false;
			
			while(x < _xSize && !end){
				y = deltay/2;
				while(y < _ySize  && !end){
					_particlesList.add(new Sphere(x, y, _radius, 0, 0, Particle.getInitialParticlesColor()));
					
//					System.out.println("particle : "+ i +" - "+ x+" - " + y);
					y += deltay;
					i++;
					if(i >= numParticles) end = true;
				}
				x += deltax;
			}
//			System.out.println("particlesList size : "+ _particlesList.size());	
			
		}			
	}
	
	
	void initialParticlesMove(double timeStep){	
		if (_particlesList != null){
			brownianMotionAndInertiaStep(timeStep);
			checkCollisions();
			updatePositions();
		}
	}
	
	String getDescription(){
		
		String s = "Bounded Space: " + _boundedSpace + "\n";
		s += "Constant Concentration Rate Boundaries: " + _constantBGConcentration + "\n";
		s += "Constant Concentration Rate Width: " + _constantBGConcentrationWidth + "\n";
		if(_boundedSpace){
			s += "space x size (nm): " + _xSize + "\n";
			s += "space y size (nm): " + _ySize + "\n";
		}	
		s += "diffusion constant (nm2/ns): " + _D + "\n";
		s += "initial concentration (particles per 10000nm2): " + _bgConcentration + "\n";
		s += "particles radius (nm): " + _radius + "\n";
		s += "\n";
		return s;
	}
	
//	public void cleanDataStructures() {
//		_collisionsQueue.clear();
//		_realCollisions = 0;
//	}

	//GETTERS AND SETTERS
	
	public int getXSize() {
		return (int)_xSize;
	}
	
	public int getYSize() {
		return (int)_ySize;
	}

	public ArrayList<Emitter> getEmittersList(){
		return _emittersList;
	}

	public ArrayList<Receiver> getReceiversList(){
		return _receiversList;
	}

	public boolean getActiveCollision() {
		return _activeCollision;
	}

	public double getBMFactor() {
		return _BMFactor;
	}
	
	public double getInertiaFactor() {
		return _inertiaFactor;
	}

	public boolean getBoundedSpace() {
		return _boundedSpace;
	}
	
	public boolean getconstantBGConcentration() {
		return _constantBGConcentration;
	}

	public double getconstantBGConcentrationWidth() {
		return _constantBGConcentrationWidth;
	}

	public boolean getInfoFile() {
		return _infoFile;
	}
	

	public ArrayList<Particle> getParticleList() {
		return _particlesList;
	}
	

	public ArrayList<HorizontalBoundary> getHorizontalBoundaries() {
		// TODO Auto-generated method stub
		return _horizontalBoundariesList;
	}

	public ArrayList<VerticalBoundary> getVerticalBoundariesList() {
		// TODO Auto-generated method stub
		return _verticalBoundariesList;
	}

// AUXILIARS METHODS
	
	public void spaceToGraphics(double time) {
		int aux = (int)(time*100);
		time = ((double)(aux))/100;
		DataInterface.writeLineToFile(DataInterface.getGraphFile(), "time:" + strPre(time));
		for(Particle p : _particlesList ){
			((Sphere)p).toGraphFile();
		}
	}


//	public String calculateD(int time) {
//	String s = "";
//	double dx = 0;
//	double dy = 0;
//	int x;
//	int y;
//	for(Particle p : _particlesList){
//		dx += p.calculateDx(time);
//		dy += p.calculateDy(time);
//	}
//	dx = dx / (double) _particlesList.size();
//	dy = dy / (double) _particlesList.size();
//	dx = dx*100;
//	x = (int)dx;
//	dx = ((double)x)/100.0;
//	dy = dy*100;
//	y = (int)dy;
//	dy = ((double)y)/100.0;
//	s+= time + ":" + dx + ":" +dy;
//	return s;
//}

//  just for debugging purposes
//	public double calculateVelocity() {
//	
//		double v = 0;
//		for(Particle p : _particlesList){
//			v += p.calculateVelocity();
//		}
//		v = v / (double)_particlesList.size();
//		return v;
//	}
	
	//TODO move this to data layer
	private String strPre(double inValue){
		DecimalFormat twoDec = new DecimalFormat("0.00");
		twoDec.setGroupingUsed(false);
		return twoDec.format(inValue);
	}

	public static double getDiffCoef() {
		// TODO Auto-generated method stub
		return _D;
	}

}
