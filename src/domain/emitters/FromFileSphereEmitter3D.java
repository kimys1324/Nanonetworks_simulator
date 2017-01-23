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
import java.util.Collections;
import java.util.Iterator;

import domain.particles.Particle;
import domain.particles.ParticleXComparator;
import domain.particles.Sphere3D;


//TODO currently this receiver does not admit absorving particles

// emitter to be used only with
// punctual=true 
// initVx=initVy=0

public class FromFileSphereEmitter3D extends FromFileSphereEmitter{

	double _z;
	
	public FromFileSphereEmitter3D(double x, double y, double z,
			double startTime, double endTime, double amplitude,
			double sphereRadius, double emitterRadius, double initV, boolean punctual, boolean concentrationEmitter, String filename,
			int size, String color) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV, punctual, concentrationEmitter,
				filename, size, color);
		_z = z;
	}
	
	public String getDescription(){
		String s = super.getDescription();
		s += "scale factor: " + _amplitude + "\n";
		s += "wave file" + _filename + "\n";
		s += "z location (nm): " + _z + "\n";
		return s;
	}
	
	void addParticles(int numParticles, ArrayList<Particle> particlesList) {

		for(int i=0; i< numParticles; i++){
				particlesList.add(new Sphere3D(_x, _y, _z, _sphereRadius, 0, 0, 0, _color));
		}
	}
	
	int deleteParticles(int numParticles, ArrayList<Particle> particlesList){
	
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		int count = 0;
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		
		
		while(it.hasNext()){
			p = it.next();
			if(p.getX() > _x+_emitterRadius) break;
			if(p.getX() >= _x-_emitterRadius){
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)+(p.getZ()-_z)*(p.getZ()-_z)<=_emitterRadius*_emitterRadius){
					toDeleteList.add(p);
				}
			}
		}
		// delete closer particles, so we sort them by distance to emitter
		Collections.sort(toDeleteList, new ParticleDistanceComparator(_x, _y));
		while(count < numParticles && count < toDeleteList.size()){
			particlesList.remove(toDeleteList.get(count));
			count++;
		}
		 return count;
	}
}
