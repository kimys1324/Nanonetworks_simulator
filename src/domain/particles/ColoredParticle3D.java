package domain.particles;

import java.util.Random;

public class ColoredParticle3D extends ColoredParticle{

	double _z;
	double _nextZ;
	double _previousVz;
	static Random _randomGaussianZ = new Random(System.currentTimeMillis()/7);
	
	public ColoredParticle3D(double x, double y, double z, double radius, double initVx,
			double initVy, double initVz, String color, int type) {
		super(x, y, radius, initVx, initVy, color, type);
		// TODO Auto-generated constructor stub
		
		this._z=z;
		this._previousVz = initVz;
	}
	
	public String toString()
	{
		String s = "";
		s += "x = " + _x + "\n";
		s += "nextX = " + _nextX + "\n";
		s += "y = " + _y + "\n";
		s += "nextY = " + _nextY + "\n";
		s += "z= " + _z + "\n";
		s += "nextZ = " + _nextZ + "\n";
		s += "tag = " + _tag + "\n";
		s += "color = " + _color + "\n";
		s += "type = " + type + "\n";
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
