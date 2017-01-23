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

import java.text.DecimalFormat;

import data.DataInterface;

public class Sphere extends Particle{
	double _radius;

	public Sphere(double x, double y, double radius, double initVx, double initVy, String color) {
		super( x, y, initVx, initVy, color);
		_radius = radius;
	}

	public double getMinX() {
		return _x<_nextX? _x-_radius:_nextX-_radius;
	}

	public double getMaxX() {
		return _x>_nextX? _x+_radius:_nextX+_radius;
	}

	public double getMinY() {
		return _y<_nextY? _y-_radius:_nextY-_radius;
	}

	public double getMaxY() {
		return _y>_nextY? _y+_radius:_nextY+_radius;
	}
	
	public double getRadius() {
		return _radius;
	}
	
	public double getVx() {
		return _nextX - _x;
	}
	
	public double getVy() {
		return _nextY - _y;
	}
	
	public String toString(){
		String s = "";
		s += "x = " + _x + "\n";
		s += "nextX = " + _nextX + "\n";
		s += "y = " + _y + "\n";
		s += "nextY = " + _nextY + "\n";
		s += "tag = " + _tag + "\n";
		s += "color = " + _color + "\n";
		return s;
	}
	
	public void toGraphFile(){
		String s = _color + ":" + strPre(_x) + ":" + strPre(_y) + ":" +strPre(_radius);
		DataInterface.writeLineToFile(DataInterface.getGraphFile(), s);
	}
	
	//TODO move this to data layer
	protected String strPre(double inValue){
		DecimalFormat twoDec = new DecimalFormat("0.00");
		twoDec.setGroupingUsed(false);
		return twoDec.format(inValue);
	}

	@Override
	public double getZ() {
			return 0;
	}


}
