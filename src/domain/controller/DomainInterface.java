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

package domain.controller;

import data.DataInterface;
import domain.emitters.*;
import domain.receivers.ColoredSphericalReceiver;
import domain.receivers.ColoredSphericalReceiver3D;
import domain.receivers.MobileSphericalReceiver3D;
import domain.receivers.ParticleReceiver;
import domain.receivers.Receiver;
import domain.receivers.SphericalReceiver;
import domain.receivers.SphericalReceiver3D;
import domain.receivers.SquareReceiver;
import domain.receivers.MessageSphericalReceiver3D;
import domain.space.Simulator;

public final class DomainInterface {

	Simulator _simulator;

	public void createSimulator(String path, boolean graphics, boolean infoFile, boolean activeCollision, double BMFactor, double inertiaFactor, double time, double timeStep, int xSize, int ySize, double D, double bgConcentration, double radius, boolean boundedSpace, boolean constantBGConcentration, double constantBGConcentrationWidth){
		_simulator = new Simulator(path, graphics, infoFile, time, timeStep);
		_simulator.createSpace(xSize, ySize, D, bgConcentration, radius, activeCollision, BMFactor, inertiaFactor, boundedSpace, constantBGConcentration, constantBGConcentrationWidth, infoFile);
	}
	
	public void createPulseSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String color){
		ParticleEmitter e = new PulseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color);
		_simulator.addEmitter(e);
	}
	
    public void createRectangularSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, int period, int timeOn,String color){
		ParticleEmitter e = new RectangularSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, period, timeOn, color);
		_simulator.addEmitter(e);
	}
    
    public void createNoiseSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String color){
		ParticleEmitter e = new NoiseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color);
		_simulator.addEmitter(e);
	}
	
    public void createWaveFromFileSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String file, int size, String color){
		ParticleEmitter e = new FromFileSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, file, size, color);
		_simulator.addEmitter(e);
	}
    
 	public void createPunctualWaveFromFile3DSphereEmitter(double x, double y, double z,
			double startTime, double endTime, double amplitude,
			double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String file, int size4,
			String color) {
		ParticleEmitter e = new FromFileSphereEmitter3D(x, y, z, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, file, size4, color);
		_simulator.addEmitter(e);
		
	}
 	// CSK emitter
	public void createCSKSphereEmitter(double x, double y, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period) {
		ParticleEmitter e = new CSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period);
		_simulator.addEmitter(e);
	}
 	
	//MOSK emitter
	public void createMOSKSphereEmitter(double x, double y, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type) {
		ParticleEmitter e = new MOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period,type);
		_simulator.addEmitter(e);
		
	}
	
	//NEW emitter
	public void createNEWMOSKSphereEmitter(double x, double y, double startTime,
            double endTime, double amplitude, double sphereRadius,
            double emitterRadius, double initV, boolean punctual,
            boolean concentrationEmitter, String color,double trans_startTime,
            String message, int period, int type, int emitPeriod ) {
        ParticleEmitter e = new NEWMOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod);
        _simulator.addEmitter(e);
    }
	
	public void createMessageSphereEmitter3D(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type, int emitPeriod, String file)
	{
		ParticleEmitter e = new MessageSpehereEmitter3D(x, y, z,startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod, file);
		_simulator.addEmitter(e);
	}

	public void createMobileEmitter(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type, int emitPeriod,
			String filename, double initVx, double initVy, double initVz)
	{
		ParticleEmitter e= new MobileEmitter(x, y, z,startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod, filename, initVx, initVy, initVz);
		_simulator.addEmitter(e);
	}
    public void createSquareReceiver(String name, double x, double y, boolean absorb, boolean accumulate, double side){
    	ParticleReceiver r = new SquareReceiver(name, x, y, absorb, accumulate, side);
    	_simulator.addReceiver(r);
	}
    
    public void createSphericalReceiver(String name, double x, double y, boolean absorb, boolean accumulate, double radius){
    	ParticleReceiver r = new SphericalReceiver(name, x, y, absorb, accumulate, radius);
    	_simulator.addReceiver(r);
	}
	
    public void create3DSphericalReceiver(String name, double x, double y,
			double z, boolean absorb, boolean accumulate, double rradius) {
    	ParticleReceiver r = new SphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius);
    	_simulator.addReceiver(r);
	}
	
    public void createColoredSphericalReceiver3D(String name, double x, double y, double z,
			boolean absorb, boolean accumulate, double radius)
    {	ParticleReceiver r = new ColoredSphericalReceiver3D(name, x, y, z, absorb, accumulate, radius);
  	 	_simulator.addReceiver(r);
    	
    }
    // Message Spherical Receiver
    public void createMessageSphericalReceiver3D(String name, double x, double y, double z,
 			boolean absorb, boolean accumulate, double radius, double symbolTime, double timeStep)
     {	ParticleReceiver r = new MessageSphericalReceiver3D(name, x, y, z, absorb, accumulate, radius, symbolTime, timeStep);
   	 	_simulator.addReceiver(r);
     	
     }
    public void startSimulation() {
		_simulator.start();
	}

	public void logError(String errorMsg) {
		DataInterface.writeLineToFile(DataInterface.getErrorsFile(), errorMsg);		
	}
	//Colored Receiver
	public void createColoredSphericalReceiver(String name, double x, double y,
			boolean absorb, boolean accumulate, double radius) {
		ParticleReceiver r = new ColoredSphericalReceiver(name, x, y, absorb, accumulate, radius);
    	_simulator.addReceiver(r);
		
	}

	public void createMobileSphericalReceiver3D(String name, double x,
			double y, double z, boolean absorb, boolean accumulate,
			double rradius, double symboltime, double timeStep, double initVx,
			double initVy, double initVz) {
		// TODO Auto-generated method stub
		ParticleReceiver r = new MobileSphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep, initVx, initVy, initVz);
   	 	_simulator.addReceiver(r);
     	
	}
	

}
