package domain.particles;

public class ColoredParticle extends Sphere {

	public ColoredParticle(double x, double y, double radius, double initVx,
			double initVy, String color, int type) {
		super(x, y, radius, initVx, initVy, color);
		this.type = type;
	}
	
	public String toString(){
		String s = "";
		s += "x = " + _x + "\n";
		s += "nextX = " + _nextX + "\n";
		s += "y = " + _y + "\n";
		s += "nextY = " + _nextY + "\n";
		s += "tag = " + _tag + "\n";
		s += "color = " + _color + "\n";
		s += "type = " + type + "\n";
		return s;
	}
	public int getType() {
		return type;
	}

}
