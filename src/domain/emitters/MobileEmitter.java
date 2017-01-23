package domain.emitters;

import java.math.BigDecimal;
import java.util.ArrayList;

import domain.particles.ColoredParticle3D;
import domain.particles.Particle;

public class MobileEmitter extends MessageSpehereEmitter3D{

	BigDecimal nextX;
	BigDecimal nextY;
	BigDecimal nextZ;
	BigDecimal initVx=new BigDecimal(0);
	BigDecimal initVy=new BigDecimal(0);
	BigDecimal initVz=new BigDecimal(0);
	
	public MobileEmitter(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double Trans_startTime,
			String message, int period, int type, int emitPeriod,
			String filename, double initVx, double initVy, double initVz) {
		super(x, y, z, startTime, endTime, amplitude, sphereRadius, emitterRadius,
				initV, punctual, concentrationEmitter, color, Trans_startTime, message,
				period, type, emitPeriod, filename);
		

		nextX=new BigDecimal(String.valueOf(x));
		nextY=new BigDecimal(String.valueOf(y));
		nextZ=new BigDecimal(String.valueOf(z));
		this.initVx = new BigDecimal(String.valueOf(initVx));
		this.initVy = new BigDecimal(String.valueOf(initVy));
		this.initVz = new BigDecimal(String.valueOf(initVz));
		// TODO Auto-generated constructor stub
	}

	public void updatePosition(){
		/*if(_x!=nextX.doubleValue() || _y!=nextY.doubleValue() || _z!=nextZ.doubleValue() ){
			System.out.println("prevX: " + _x + " nextX: "+ nextX);
			System.out.println("prevY: " + _y + " nextY: "+ nextY);
			System.out.println("prevZ: " + _z + " nextZ: "+ nextZ);
		}*/
		_x=nextX.doubleValue();
		_y=nextY.doubleValue();
		_z=nextZ.doubleValue();
		
	}
	
	public void makeMotion(){
		nextX = nextX.add(initVx);		
		nextY = nextY.add(initVy);
		nextZ = nextZ.add(initVz);
		
	}
	public void addParticles(int numParticles, ArrayList<Particle> particlesList, int type) {

		for(int i=0; i< numParticles; i++){
			ColoredParticle3D t = new ColoredParticle3D(_x, _y, _z, _sphereRadius, initVx.doubleValue(), initVy.doubleValue(), initVz.doubleValue(), _color, type);	
			//System.out.println("Z location : "+t.getZ());
			particlesList.add(t);
		}
	}
	
}
