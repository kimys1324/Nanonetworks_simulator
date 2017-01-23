/*
	N3Sim: A Simulation Framework for Diffusion-based Molecular Communication
    Copyright (C) 2011 I챰aki Pascual - N3CAT (UPC)
    N3Cat (NaNoNetworking Center in Catalunya)
	Universitat Polit챔cnica de Catalunya (UPC)
	Jordi Girona, 1-3, M챵dul D6 (Campus Nord)
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

public abstract class ParticleEmitter implements Emitter {
	
	
	double _x;
	double _y;
	double _startTime;
	double _endTime;
	static double _amplitude;
	double _emitterRadius;
	double _initV;
	boolean _punctual;
	boolean _concentrationEmitter;
	
	protected String _color;
	
	ParticleEmitter(double x, double y, double startTime, double endTime, double amplitude, double emitterRadius, double initV, boolean punctual, boolean concentrationEmitter, String color){
		_x = x;
		_y = y;
		_startTime = startTime;
		_endTime = endTime;
		_amplitude = amplitude;
		_emitterRadius = emitterRadius;
		_initV = initV;
		_punctual = punctual;
		_concentrationEmitter = concentrationEmitter;
		_color = color;
	}
	
	abstract double getNumParticles(double time);
	
	public void emit(double time, ArrayList<Particle> particlesList){
		int n = (int)getNumParticles(time);
		int deleted = 0;
		if(n > 0){
			deleted = deleteParticles(particlesList.size(), particlesList);
			if (_concentrationEmitter) addParticles(n, particlesList);
			else addParticles(n+deleted, particlesList);
		}
		else if (n < 0){
			n *= -1;
			deleteParticles(n, particlesList);
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
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)<=_emitterRadius*_emitterRadius){
					toDeleteList.add(p);
				}
			}
		}
		// delete closer particles, so we sort them by distance to emitter
		Collections.sort(toDeleteList, new ParticleDistanceComparator(_x, _y));
		//해단 emitter에서 가까운 순으로 sort한 뒤에 count만큼 가까운 애들 삭제
		while(count < numParticles && count < toDeleteList.size()){
			particlesList.remove(toDeleteList.get(count));
			count++;
		}
		 return count;
}

	abstract void addParticles(int numParticles, ArrayList<Particle> particlesList);

	public String getDescription(){
		String s;
		s = "influence radius (nm): " + _emitterRadius + "\n";
		s += "x location (nm): " + _x + "\n";
		s += "y location (nm): " + _y + "\n";
		s += "start time (ns): " + _startTime + "\n";
		s += "end time (ns): " + _endTime + "\n";
		s += "initial speed (m/s): " + _initV + "\n";
		s += "punctual: " + _punctual + "\n";
		s += "concentration emitter: " + _concentrationEmitter + "\n";
		return s;
	}

	public static double getAmplitude() {
		// TODO Auto-generated method stub
		return _amplitude;
	}
}
