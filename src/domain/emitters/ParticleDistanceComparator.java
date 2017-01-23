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

package domain.emitters;

import java.util.Comparator;

import domain.particles.Particle;

public class ParticleDistanceComparator implements Comparator<Particle> {
	
	double _x, _y;

	public ParticleDistanceComparator(double x, double y) {
		_x = x;
		_y = y;
	}

	public int compare(Particle p1, Particle p2) {
		double d = getDistance(p1) - getDistance(p2);
		if (d > 0) return 1;
		if (d < 0) return -1;
		return 0;
	}

	private double getDistance(Particle p) {
		return (p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y);
	}

	
}
