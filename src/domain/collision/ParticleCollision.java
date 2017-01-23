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

public abstract class ParticleCollision implements Collision {

	double _time;
	
	ParticleCollision(){
		_time = -1;
	}
	
	public double getTime() {
		if (_time == -1)
			calculateTime();
		return _time;
	}
	
	public abstract void solveCollision();
	
	// checks whether collision really takes place. If so calculates time (in scale 0 - 1) and updates parameter _time.
	abstract void calculateTime();

	public abstract boolean checkTag();

}
