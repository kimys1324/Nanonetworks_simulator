package domain.emitters;

import java.util.ArrayList;

import data.DataInterface;
import domain.particles.*;

public class MOSKSphereEmitter extends CSKSphereEmitter {

	int type = 0; 
	
	public MOSKSphereEmitter(double x, double y, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double Trans_startTime,
			String message, int period,int type) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,
				punctual, concentrationEmitter, color, Trans_startTime, message, period);
		this.type = type;
	}
	


    public void emit(double time, ArrayList<Particle> particlesList){
		int n = (int)getNumParticles(time);
		int deleted = 0;
		if(n > 0){
			deleted = deleteParticles(particlesList.size(), particlesList);
			if (_concentrationEmitter) addParticles(n, particlesList, type);
			else addParticles(n+deleted, particlesList, type);
		}
		else if (n < 0){
			n *= -1;
			deleteParticles(n, particlesList);
		}
	}
	
	private int getTypeEmitter() {

		return this.type;
	}
	
	public String getDescription(){
		String s = super.getDescription();
		s += "type: " + type + "\n";
		return s;
	}

	void addParticles(int numParticles, ArrayList<Particle> particlesList, int type) {

		double maxDistance = _emitterRadius*_emitterRadius;
				//add particles in influence radius
		
		double emitterArea = Math.PI*_emitterRadius*_emitterRadius;
		double squareSide = Math.sqrt(emitterArea/numParticles);
		
		int xydiv = (int)(2 * _emitterRadius/squareSide);
		xydiv++;
		squareSide = (2*_emitterRadius/xydiv);
		
		if(!_punctual && squareSide <= _sphereRadius*2){
			DataInterface.writeLineToFile(DataInterface.getErrorsFile(), "WARNING : emitting too many particles");
			squareSide = _sphereRadius*2.1;
		}
	
		int i = 0;
		double x = _x - _emitterRadius + squareSide/2;
		double y = _y - _emitterRadius + squareSide/2;
		double maxx = _x + _emitterRadius - squareSide/2;
		double maxy = _y + _emitterRadius - squareSide/2;
			
		boolean end = false;

		
		double deltaY = y-_y;
		double deltaX = x-_x;
		double vx = 0; 
		double vy = 0;

		while(x < maxx && !end){
			y = _y - _emitterRadius + squareSide/2;
			while(y < maxy && !end){
				deltaY = y-_y;
				deltaX = x-_x;
				if(deltaX*deltaX+deltaY*deltaY <= maxDistance){
					// we do not accept negative speed
					// if x == _x and y==_y, speed and angle are zero
					if(_initV > 0){
						if (y == _y){
							vx = _initV; if (x < _x) vx *= -1;
						}
						else if(x == _x){
							vy = _initV; if (y < _y) vy *= -1;
						}
						else{
							vx=Math.sqrt((_initV*_initV)/(1+(deltaY*deltaY)/(deltaX*deltaX)));
							if ((deltaX)<0) vx *= -1;
							vy =vx*deltaY/deltaX;
						}
					}
					//TODO if punctual, the initial speed must be defined, in the meantime it will be zero 
					if(_punctual) particlesList.add(new ColoredParticle(_x, _y, _sphereRadius, vx, vy, _color, type));
					else particlesList.add(new ColoredParticle(x, y, _sphereRadius, vx, vy, _color, type));
					i++;
					if(i >= numParticles) end = true;
				}
				y += squareSide;
			}
			x += squareSide;
		}
	}
	
}
