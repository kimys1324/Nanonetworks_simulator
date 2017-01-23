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

package domain.particles;

import java.util.Random;

public class Sphere3D extends Sphere{

	double _z;
	double _initVz;
	double _nextZ;
	double _previousVz;
	static Random _randomGaussianZ = new Random(System.currentTimeMillis()/7);
	
	public Sphere3D(double x, double y, double z, double radius, double initVx, double initVy, double initVz, String color) {
		super(x, y, radius, initVx, initVy, color);
		_z = z;
		_previousVz = 0;
	}

	public String toString(){
		String s = "";
		s += "x = " + _x + "\n";
		s += "nextX = " + _nextX + "\n";
		s += "y = " + _y + "\n";
		s += "nextY = " + _nextY + "\n";
		s += "z= " + _z + "\n";
		s += "nextZ = " + _nextZ + "\n";
		s += "tag = " + _tag + "\n";
		s += "color = " + _color + "\n";
		return s;
	}
	
	public void makeBMAndInertiaStep(double k, double BMFactor, double inertiaFactor) {

		_nextX = _x + inertiaFactor*_previousVx + BMFactor*k*_randomGaussianX.nextGaussian();
		_nextY = _y + inertiaFactor*_previousVy + BMFactor*k*_randomGaussianY.nextGaussian();
		_nextZ = _z + inertiaFactor*_previousVz + BMFactor*k*_randomGaussianZ.nextGaussian();
	}
	
	public void updatePosition() {
		_previousVx = _nextX - _x;
		_previousVy = _nextY - _y;
		_previousVz = _nextZ - _z;
		_x = _nextX;
		_y = _nextY;
		_z = _nextZ;
	}
	
	public double getZ() {
		return _z;
	}
}
