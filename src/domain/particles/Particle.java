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

public abstract class Particle {
	
	private static final String _initialParticlesColor = "purple";
	static Random _randomGaussianX = new Random(System.currentTimeMillis());
	static Random _randomGaussianY = new Random(System.currentTimeMillis()/3);
	int _tag;
	double _x;
	double _y;
	double _nextX;
	double _nextY;
	double _previousVx;
	double _previousVy;
	String _color;
	int type = 0;
	
	
	public Particle(double x, double y, double initVx, double initVy, String color) {
		_x = x;
		_y = y;
		_nextX = x;
		_nextY = y;
		_color = color;
		_tag = 0;
		_previousVx = initVx;
		_previousVy = initVy;

	}

	public void makeBMAndInertiaStep(double k, double BMFactor, double inertiaFactor) {
		double auxx = BMFactor*k*_randomGaussianX.nextGaussian();
		double auxy = BMFactor*k*_randomGaussianY.nextGaussian();
		_nextX = _x + inertiaFactor*_previousVx + auxx;
		_nextY = _y + inertiaFactor*_previousVy + auxy;

	}
	
	public void updatePosition() {
		_previousVx = _nextX - _x;
		_previousVy = _nextY - _y;
		_x = _nextX;
		_y = _nextY;

	}

	public double getX() {
		return _x;
	}

	public double getY() {
		return _y;
	}
	
	public abstract double getZ();
	
	public double getNextX() {
		return _nextX;
	}

	public double getNextY() {
		return _nextY;
	}
	
	public int getTag() {
		return _tag;
	}
	
	public void setX(double x) {
		_x = x;
	}

	public void setY(double y) {
		_y = y;
	}

	public void setNextX(double nextX) {
		_nextX = nextX;
	}

	public void setNextY(double nextY) {
		_nextY = nextY;
	}
	
	public void incrementTag() {
		_tag++;
	}

	public abstract double getMinX();

	public abstract double getMaxX();

	public abstract double getMinY();

	public abstract double getMaxY();

	public static String getInitialParticlesColor() {
		return Particle._initialParticlesColor;
	}
	public int getType() {
		return type;
	}
}
