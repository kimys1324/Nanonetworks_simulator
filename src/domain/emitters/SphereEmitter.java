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

package domain.emitters;

import java.util.ArrayList;
import data.DataInterface;
import domain.particles.Particle;
import domain.particles.Sphere;

public abstract class SphereEmitter extends ParticleEmitter{

	double _sphereRadius;

	SphereEmitter(double x, double y, double startTime, double endTime,
		double amplitude, double sphereRadius, double emitterRadius, double initV, boolean punctual, boolean concentrationEmitter, String color) {
		super(x, y, startTime, endTime, amplitude, emitterRadius, initV, punctual, concentrationEmitter, color);
		_sphereRadius = sphereRadius;
	}
	
	//TODO improve!
	//TODO use hexagonal deploy of particles instead of square to be able to emit more particles
	void addParticles(int numParticles, ArrayList<Particle> particlesList) {

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
					if(_punctual) particlesList.add(new Sphere(_x, _y, _sphereRadius, vx, vy, _color));
					else particlesList.add(new Sphere(x, y, _sphereRadius, vx, vy, _color));
					i++;
					if(i >= numParticles) end = true;
				}
				y += squareSide;
			}
			x += squareSide;
		}
	}
	
	public String getDescription(){
		return super.getDescription();
	}
}
