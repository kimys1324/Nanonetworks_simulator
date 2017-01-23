package domain.receivers;
import java.math.BigDecimal;

public class MobileSphericalReceiver3D extends MessageSphericalReceiver3D {


	BigDecimal nextX;
	BigDecimal nextY;
	BigDecimal nextZ;
	BigDecimal initVx=new BigDecimal(0);
	BigDecimal initVy=new BigDecimal(0);
	BigDecimal initVz=new BigDecimal(0);
	
	
	public MobileSphericalReceiver3D(String name, double x, double y,
			double z, boolean absorb, boolean accumulate, double radius,
			double symboltime, double timeStep, double initVx, double initVy, double initVz) {
		super(name, x, y, z, absorb, accumulate, radius, symboltime, timeStep);
		// TODO Auto-generated constructor stub
		
		nextX=new BigDecimal(String.valueOf(x));
		nextY=new BigDecimal(String.valueOf(y));
		nextZ=new BigDecimal(String.valueOf(z));
		this.initVx = new BigDecimal(String.valueOf(initVx));
		this.initVy = new BigDecimal(String.valueOf(initVy));
		this.initVz = new BigDecimal(String.valueOf(initVz));
	}
	
	public void updatePosition(){
		/*if(_x!=nextX.doubleValue() || _y!=nextY.doubleValue() || _z!=nextZ.doubleValue() ){
			System.out.println("prevX: " + _x + " nextX: "+ nextX);
			System.out.println("prevY: " + _y + " nextY: "+ nextY);
			System.out.println("prevZ: " + _z + " nextZ: "+ nextZ);
		}*/
		_x=nextX.doubleValue();
		_y=nextY.doubleValue();
		_z=nextZ.doubleValue();
		
	}
	
	public void makeMotion(){
		nextX = nextX.add(initVx);		
		nextY = nextY.add(initVy);
		nextZ = nextZ.add(initVz);
		
	}
	

}
