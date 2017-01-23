package domain.controller;

import data.DataInterface;
import domain.emitters.CSKSphereEmitter;
import domain.emitters.Emitter;
import domain.emitters.FromFileSphereEmitter;
import domain.emitters.FromFileSphereEmitter3D;
import domain.emitters.MOSKSphereEmitter;
import domain.emitters.MessageSpehereEmitter3D;
import domain.emitters.MobileEmitter;
import domain.emitters.NEWMOSKSphereEmitter;
import domain.emitters.NoiseSphereEmitter;
import domain.emitters.ParticleEmitter;
import domain.emitters.PulseSphereEmitter;
import domain.emitters.RectangularSphereEmitter;
import domain.emitters.SensorEmitter;
import domain.receivers.ColoredSphericalReceiver;
import domain.receivers.ColoredSphericalReceiver3D;
import domain.receivers.MessageSphericalReceiver3D;
import domain.receivers.MobileSphericalReceiver3D;
import domain.receivers.ParticleReceiver;
import domain.receivers.Receiver;
import domain.receivers.SensorReceiver;
import domain.receivers.SphericalReceiver;
import domain.receivers.SphericalReceiver3D;
import domain.receivers.SquareReceiver;
import domain.space.Simulator;

public class MachineDomainInterface{

	Simulator _simulator;
	ParticleEmitter te;
	Receiver tr;

	public void createSimulator(String path, boolean graphics, boolean infoFile, boolean activeCollision, double BMFactor, double inertiaFactor, double time, double timeStep, int xSize, int ySize, double D, double bgConcentration, double radius, boolean boundedSpace, boolean constantBGConcentration, double constantBGConcentrationWidth){
		_simulator = new Simulator(path, graphics, infoFile, time, timeStep);
		_simulator.createSpace(xSize, ySize, D, bgConcentration, radius, activeCollision, BMFactor, inertiaFactor, boundedSpace, constantBGConcentration, constantBGConcentrationWidth, infoFile);
	}
	
	public void createPulseSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String color){
		ParticleEmitter e = new PulseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color);
		te=e;
	}
	
    public void createRectangularSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, int period, int timeOn,String color){
		ParticleEmitter e = new RectangularSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, period, timeOn, color);
		te=e;
	}
    
    public void createNoiseSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String color){
		ParticleEmitter e = new NoiseSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color);
		te=e;
	}
	
    public void createWaveFromFileSphereEmitter(double x, double y, double startTime, double endTime, double amplitude, double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String file, int size, String color){
		ParticleEmitter e = new FromFileSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, file, size, color);
		te=e;
	}
    
 	public void createPunctualWaveFromFile3DSphereEmitter(double x, double y, double z,
			double startTime, double endTime, double amplitude,
			double sphereRadius, double emitterRadius, double initV,boolean punctual, boolean concentrationEmitter, String file, int size4,
			String color) {
		ParticleEmitter e = new FromFileSphereEmitter3D(x, y, z, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, file, size4, color);
		te=e;
		
	}
 	// CSK emitter
	public void createCSKSphereEmitter(double x, double y, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period) {
		ParticleEmitter e = new CSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period);
		te=e;
	}
 	
	//MOSK emitter
	public void createMOSKSphereEmitter(double x, double y, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type) {
		ParticleEmitter e = new MOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period,type);
		te=e;
		
	}
	
	//NEW emitter
	public void createNEWMOSKSphereEmitter(double x, double y, double startTime,
            double endTime, double amplitude, double sphereRadius,
            double emitterRadius, double initV, boolean punctual,
            boolean concentrationEmitter, String color,double trans_startTime,
            String message, int period, int type, int emitPeriod ) {
        ParticleEmitter e = new NEWMOSKSphereEmitter(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod);
        te=e;
    }
	
	public void createMessageSphereEmitter3D(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type, int emitPeriod, String file)
	{
		ParticleEmitter e = new MessageSpehereEmitter3D(x, y, z,startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod, file);
		te=e;
	}
	
	public void createMobileEmitter(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type, int emitPeriod,
			String filename, double initVx, double initVy, double initVz)
	{
		ParticleEmitter e= new MobileEmitter(x, y, z,startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod, filename, initVx, initVy, initVz);
		te=e;
	}
	
	public void createSensorEmitter(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double trans_startTime,
			String message, int period, int type, int emitPeriod,
			String filename, double initVx, double initVy, double initVz, double timestep)
	{
		ParticleEmitter e= new SensorEmitter(x, y, z,startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,punctual, concentrationEmitter, color, trans_startTime, message, period , type, emitPeriod, filename, initVx, initVy, initVz, timestep);
		te=e;
	}
	
    public void createSquareReceiver(String name, double x, double y, boolean absorb, boolean accumulate, double side){
    	ParticleReceiver r = new SquareReceiver(name, x, y, absorb, accumulate, side);
    	tr=r;
	}
    
    public void createSphericalReceiver(String name, double x, double y, boolean absorb, boolean accumulate, double radius){
    	ParticleReceiver r = new SphericalReceiver(name, x, y, absorb, accumulate, radius);
    	tr=r;
	}
	
    public void create3DSphericalReceiver(String name, double x, double y,
			double z, boolean absorb, boolean accumulate, double rradius) {
    	ParticleReceiver r = new SphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius);
    	tr=r;
	}
	
    public void createColoredSphericalReceiver3D(String name, double x, double y, double z,
			boolean absorb, boolean accumulate, double radius)
    {	ParticleReceiver r = new ColoredSphericalReceiver3D(name, x, y, z, absorb, accumulate, radius);
  	 	tr=r;
    	
    }
    // Message Spherical Receiver
    public void createMessageSphericalReceiver3D(String name, double x, double y, double z,
 			boolean absorb, boolean accumulate, double radius, double symbolTime, double timeStep)
     {	ParticleReceiver r = new MessageSphericalReceiver3D(name, x, y, z, absorb, accumulate, radius, symbolTime, timeStep);
   	 	tr=r;
     	
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
    	tr=r;
		
	}

	public void createMobileSphericalReceiver3D(String name, double x,
			double y, double z, boolean absorb, boolean accumulate,
			double rradius, double symboltime, double timeStep, double initVx,
			double initVy, double initVz) {
		// TODO Auto-generated method stub
		ParticleReceiver r = new MobileSphericalReceiver3D(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep, initVx, initVy, initVz);
   	 	tr=r;
     	
	}
	public void createSensorReceiver(String name, double x,
			double y, double z, boolean absorb, boolean accumulate,
			double rradius, double symboltime, double timeStep, double initVx,
			double initVy, double initVz,String outPath, String sendmessage) {
		// TODO Auto-generated method stub
		ParticleReceiver r = new SensorReceiver(name, x, y, z, absorb, accumulate, rradius, symboltime, timeStep, initVx, initVy, initVz, outPath, sendmessage);
   	 	tr=r;
     	
	}

	
	public void addMachine()
	{
		_simulator.addEmitter(te);
		_simulator.addReceiver(tr);
	}
	
}
