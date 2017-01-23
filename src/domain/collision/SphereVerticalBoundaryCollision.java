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

import domain.boundaries.VerticalBoundary;
import domain.particles.Sphere;

public class SphereVerticalBoundaryCollision extends SphereCollision{

	private VerticalBoundary _boundary;
	
	public SphereVerticalBoundaryCollision(Sphere s, VerticalBoundary b) {
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
		double bx = _boundary.getXMin();
		if(sx < bx){
				time = (bx -r - sx)/svx;
		}
		else{
			time = (bx +r - sx)/svx;
		}
		double auxy = sy + time*svy;
		if(auxy >= _boundary.getYMin() && auxy <= _boundary.getYMax()){
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
//		System.out.println("HELLO VERTICAL COLLISION");
//		System.out.println("time = " + _time);
//		System.out.println(_sphere.toString());
//		if(_sphere.getTag() == _sphereTag){
			double auxx = _sphere.getX() + _sphere.getVx()*_time;
			double sx = 2*auxx-_sphere.getX();
			double snx = 2*auxx-_sphere.getNextX();
			_sphere.setX(sx);
			_sphere.setNextX(snx);
//		}
	}

}