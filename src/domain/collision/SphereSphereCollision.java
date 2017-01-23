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

import domain.particles.Sphere;

public class SphereSphereCollision extends SphereCollision {


	private Sphere _sphere2;
	private int _sphere2Tag;
	
	public SphereSphereCollision(Sphere s, Sphere s2) {
		super(s);
		_sphere2 = s2;
		_sphere2Tag = s2.getTag();
	}

	public ArrayList<Sphere> getSpheresList(){
		ArrayList<Sphere> sl = new ArrayList<Sphere>();
		sl.add(_sphere);
		sl.add(_sphere2);
		return sl;
	}
	
	public void calculateTime() {
		//change reference to sphere coordinate system; sphere velocity, x and y is zero
		//velocity, x and y for sphere2 in sphere coordinates system
		double vx = _sphere2.getVx() - _sphere.getVx();
		double vy = _sphere2.getVy() - _sphere.getVy();
		double x = _sphere2.getX() - _sphere.getX();
		double y = _sphere2.getY() - _sphere.getY();
		//collision radius
		double r = _sphere2.getRadius() + _sphere.getRadius();
		//circumference - line intersection equation coefficients
		double a = vx*vx+vy*vy;
		double b = 2*(x*vx+y*vy);
		double c = x*x + y*y - r*r;
		// if solution exists
		double aux = b*b - 4*a*c;
		if(aux >= 0){
			//always choose first collision
			double aux2 = -b - Math.sqrt(aux);
			//if solution time >= 0
			if( aux2 >= 0 && a != 0){
				_time = aux2/(2*a);
				if (_time >= 1) _time = -1;
			}
		}
		
	}

	public boolean checkTag(){
		if(_sphere.getTag() == _sphereTag && _sphere2.getTag() == _sphere2Tag) return true;
		return false;
	}
	
	// TO DO this applies only for spheres with same mass...
	public void solveCollision() {
//		if(_sphere.getTag() == _sphereTag && _sphere2.getTag() == _sphere2Tag){
			
			double s1vX = _sphere.getVx();
			double s1vY = _sphere.getVy(); 
			double s2vX = _sphere2.getVx(); 
			double s2vY = _sphere2.getVy(); 
			
			
			
			double s1X = _sphere.getX() + _time*s1vX;
			double s1Y = _sphere.getY() + _time*s1vY; 
			double s2X = _sphere2.getX() + _time*s2vX; 
			double s2Y = _sphere2.getY() + _time*s2vY; 
			double deltaX = s2X - s1X;
			double deltaY = s2Y - s1Y;
			
			//change to s1 reference system
			double s2vx = _sphere2.getVx() -_sphere.getVx();
			double s2vy = _sphere2.getVy() -_sphere.getVy();
			
			double s1vfx = 0;
			double s1vfy = 0;
			
			double s2vfx = 0;
			double s2vfy = 0;
			
			if(deltaX == 0){
				s1vfx = 0;
				s1vfy = -s2vy;
				s2vfx = s2vx;
				s2vfy = 0;
			}
			else if (deltaY == 0){
				s1vfx = -s2vx;
				s1vfy = 0;
				s2vfx = 0;
				s2vfy = s2vy;			
			}
			else{
				double cotg = deltaY/deltaX;
				
				s1vfx = (s2vx+s2vy*cotg)/(1+cotg*cotg);
				s1vfy = s1vfx * cotg;
				s2vfx = s2vx - s1vfx;
				s2vfy = s2vy - s1vfy;
			}
			//
			
			_sphere.setX(s1X-_time*(s1vX + s1vfx));
			_sphere.setY(s1Y-_time*(s1vY + s1vfy));
			_sphere.setNextX(s1X+(1-_time)*(s1vX + s1vfx));
			_sphere.setNextY(s1Y+(1-_time)*(s1vY + s1vfy));
			
			_sphere2.setX(s2X-_time*(s1vX + s2vfx));
			_sphere2.setY(s2Y-_time*(s1vY + s2vfy));
			_sphere2.setNextX(s2X+(1-_time)*(s1vX + s2vfx));
			_sphere2.setNextY(s2Y+(1-_time)*(s1vY + s2vfy));
//		}
	}
	
	
	public String toString(){
		String s = "";
		s += _sphere.toString();
		s += _sphere2.toString();
		s += "tag = " + _sphereTag + "\n";
		s += "tag2 = " + _sphere2Tag + "\n";
		s += "time = " + _time + "\n";
		return s;
	}

	// GETTERS & SETTERS

	public Sphere getSphere2() {
		return _sphere2;
	}
	
}
