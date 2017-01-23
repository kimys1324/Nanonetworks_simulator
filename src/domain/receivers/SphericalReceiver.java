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

package domain.receivers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import domain.particles.Particle;
import domain.particles.ParticleXComparator;

public class SphericalReceiver extends ParticleReceiver {

	double _radius;
	
	public SphericalReceiver(String name, double x, double y, boolean absorb, boolean accumulate, double radius) {
		super(name, x, y, absorb, accumulate);
		_radius = radius;
	}

	public int simpleCount(ArrayList<Particle> particlesList) {
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		int count = 0;
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		
		while(it.hasNext()){
			p = it.next();
			if(p.getX() > _x+_radius) break;
			if(p.getX() >= _x-_radius){
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)<=_radius*_radius){
					count++;
					if(_absorb) {
						toDeleteList.add(p);
					}
				}
			}
		}
		for(Particle pa: toDeleteList) particlesList.remove(pa);
		return count;
	}

	public String getDescription() {
		String s = super.getDescription();
		s += "receiver radius (nm): " + _radius + "\n"; 
		return s;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeMotion() {
		// TODO Auto-generated method stub
		
	}	
}
