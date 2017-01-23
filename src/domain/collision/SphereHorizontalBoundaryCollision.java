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

package domain.collision;

import java.util.ArrayList;

import domain.boundaries.HorizontalBoundary;
import domain.particles.Sphere;

public class SphereHorizontalBoundaryCollision extends SphereCollision{

	private HorizontalBoundary _boundary;
	
	public SphereHorizontalBoundaryCollision(Sphere s, HorizontalBoundary b) {
		super(s);
		_boundary = b;
	}

	public ArrayList<Sphere> getSpheresList(){
		ArrayList<Sphere> sl = new ArrayList<Sphere>();
		sl.add(_sphere);
		return sl;
	}
	
	public void calculateTime() {
		double time = -1;
		double sx = _sphere.getX();
		double sy = _sphere.getY();
		double svx = _sphere.getVx();
		double svy = _sphere.getVy();
		double r = _sphere.getRadius();
		double by = _boundary.getYMin();
		if(sy < by){
			time = (by -r - sy)/svy;
		}
		else{
			time = (by +r - sy)/svy;
		}
		
		double auxx = sx + time*svx;
		
		if(auxx >= _boundary.getXMin() && auxx <= _boundary.getXMax()){
			if (time >= 0 && time < 1) _time = time;
			else _time = -1;
		}
		else _time = -1;
	}

	public boolean checkTag(){
		if(_sphere.getTag() == _sphereTag) return true;
		return false;
	}
	
	public void solveCollision() {
//		if(_sphere.getTag() == _sphereTag){
			double auxy = _sphere.getY() + _sphere.getVy()*_time;
			double sy = 2*auxy-_sphere.getY();
			double sny = 2*auxy-_sphere.getNextY();
			_sphere.setY(sy);
			_sphere.setNextY(sny);
//		}
	}

}
