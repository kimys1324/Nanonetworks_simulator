package domain.emitters;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;

import domain.particles.ColoredParticle3D;
import domain.particles.Particle;

import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import ui.FileUI;
import ui.UI;
import ui.UIver2;
import data.DataInterface;
import domain.particles.ParticleXComparator;
import domain.particles.Sphere;
import domain.particles.Sphere3D;
import domain.receivers.SensorReceiver;
import domain.space.Simulator;

public class SensorEmitter extends NEWMOSKSphereEmitter{
	
	
	double _z;
	public   double dynamicSymboltime;
	public   double distance;
	public   double velocity;
	double nextDST;
	double currentDST;
	double tempST;
	double startSpot;
	double endSpot;
	double timeStep;
	int check=0;
	File wavefile ; 
	double DST[] = new double[Trans_Message.length()];
	public   double tempD;
	int step=1;
	public   double Trans_startTime;
	public   double Trans_endTime;
	public   String Message;
	
	private ArrayList<String> _names = new ArrayList<String>(); // lists of variables (names list and values list)
	private ArrayList<String> _values = new ArrayList<String>();
	String tempTransmit_Messge;
	
	BigDecimal nextX;
	BigDecimal nextY;
	BigDecimal nextZ;
	BigDecimal initVx=new BigDecimal(0);
	BigDecimal initVy=new BigDecimal(0);
	BigDecimal initVz=new BigDecimal(0);
	
	public SensorEmitter(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double Trans_startTime,
			String message, int period, int type, int emitPeriod,
			String filename, double initVx, double initVy, double initVz, double timestep) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,
				punctual, concentrationEmitter, color, Trans_startTime, message,
				period, type, emitPeriod);
		this._z = z;
		try {
			readFile(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("0-message.length():" + message.length());
		Message=message;
		System.out.println("0-Message.length():" + Message.length());
		Trans_Message = this.Preamble+Message;
		System.out.println("1-Trans_Message.length():" + Trans_Message.length());
		DST = new double[message.length()];
		nextX=new BigDecimal(String.valueOf(x));
		nextY=new BigDecimal(String.valueOf(y));
		nextZ=new BigDecimal(String.valueOf(z));
		this.initVx = new BigDecimal(String.valueOf(initVx));
		this.initVy = new BigDecimal(String.valueOf(initVy));
		this.initVz = new BigDecimal(String.valueOf(initVz));
		this.timeStep = timestep; 
		// TODO Auto-generated constructor stub
	}
	
	public String getDescription()
	{
		String s = super.getDescription();
		
		s+="z location (nm)"+this._z+"\n";
		
		return s;
	}
	public void readFile(String f) throws FileNotFoundException
	{	
		wavefile = new File(f);
		if(!wavefile.exists()){
			error("ERROR : File " + wavefile + " does not exist. Program terminates.");
			System.exit(1);
		}
		
		// read all lines 		
		Scanner scanner = new Scanner(wavefile);
	    String line = "";
		while(scanner.hasNext()){
			line = scanner.nextLine();
			// do not read if blank line or begins with #
			while (line.startsWith("#") || line.equals("")){
		    	line = scanner.nextLine();
			}
		    line.replaceAll("\\s", ""); //TODO this doesn't work !?!?!?!
			processLine(line, _names, _values);
		}
		scanner.close();
		
		this.period = getNextInt("symboltime");
		this.period = (int) dynamicSymboltime;
		
		this.emitPeriod = getNextInt("emitperiod");
		this._amplitude = getNextInt("amplitude");
		//this.Trans_endTime = SensorReceiver.getTotalTrnasmitTime();
		
		
	}
	
	private void processLine(String line, ArrayList<String> names, ArrayList<String>values)
	{
		String name = "";
		String value = "";
		Scanner scanner = new Scanner(line);
	    scanner.useDelimiter("=");
	    name = scanner.next();
	    value = scanner.next();
	    		    scanner.close();
	    		    

	    names.add(name);
	    values.add(value);
	}

	private int getNextInt(String name){
		String foundName = _names.remove(0);
		String strValue =_values.remove(0);
		int value = 0;
		
		if(foundName.equalsIgnoreCase(name)){
			try {
				value = Integer.parseInt(strValue);
			} catch (Exception e) {
				error("ERROR : UI : parameter " + name + " , expected to be int, found " + strValue +". Program terminates.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return value;
	}
	
	private double getNextDouble(String name){
		String foundName = _names.remove(0);
		String strValue =_values.remove(0);
		strValue = strValue.replace(',', '.');
		double value = 0;
		
		if(foundName.equalsIgnoreCase(name)){
			try {
				value = Double.parseDouble(strValue);
			} catch (Exception e) {
				error("ERROR : UI : parameter " + name + " , expected to be double, found " + strValue +". Program terminates.");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return value;
	}
	
	private  String getNextString(String name){
		String foundName = _names.remove(0);
		String value = _values.remove(0);
		if(foundName.equalsIgnoreCase(name)) return value;
		else {
			error("ERROR : UI : expected parameter " + name + " , found " + foundName +". Program terminates.");
			System.exit(1);
		}
		return null;
	}
	
	protected void error(String errorMsg) {
		DataInterface.writeLineToFile(DataInterface.getErrorsFile(), errorMsg);
		System.out.println(errorMsg);
	}

	public double getNumParticles(double time) {
		if(i < this.Message.length() && UIver2.sensor_start_signal == 1){
			if(time >= _startTime && time < _endTime){
				if(Trans_startTime <= time && time < Trans_endTime){
					if(step == 1){
						startSpot = Trans_startTime;
						endSpot = Trans_startTime + calDST(step-1);
					}
					if(startSpot <= time && time < endSpot){
						if(time <= startSpot + emitPeriod && this.Message.charAt(i) == "1".charAt(0)){
							System.out.println("emitted!!******************************");
							System.out.println("timestep: " + time);
							System.out.println("Message.length(): " + this.Message.length());
							return _amplitude;
						}
						else if(endSpot - emitPeriod <= time && time < endSpot){
							startSpot = endSpot;
							endSpot += calDST(step);
							step++; i++;
						}
					}
				}
			}
		}
		
		return 0;
    }

	public int deleteParticles(int numParticles, ArrayList<Particle> particlesList)
	{
		Collections.sort(particlesList, new ParticleXComparator());
		Iterator<Particle> it = particlesList.iterator();
		Particle p = null;
		int count = 0;
		ArrayList<Particle> toDeleteList = new ArrayList<Particle>();
		
		
		while(it.hasNext()){
			p = it.next();
			if(p.getX() > _x+_emitterRadius) break;
			if(p.getX() >= _x-_emitterRadius){
				if((p.getX()-_x)*(p.getX()-_x)+(p.getY()-_y)*(p.getY()-_y)+(p.getZ()-_z)*(p.getZ()-_z)<=_emitterRadius*_emitterRadius){
					toDeleteList.add(p);
				}
			}
		}
		// delete closer particles, so we sort them by distance to emitter
		Collections.sort(toDeleteList, new ParticleDistanceComparator(_x, _y));
		while(count < numParticles && count < toDeleteList.size()){
			particlesList.remove(toDeleteList.get(count));
			count++;
		}
		 return count;
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
	public void addParticles(int numParticles, ArrayList<Particle> particlesList, int type) {

		for(int i=0; i< numParticles; i++){
			ColoredParticle3D t = new ColoredParticle3D(_x, _y, _z, _sphereRadius, initVx.doubleValue(), initVy.doubleValue(), initVz.doubleValue(), _color, type);	
			//System.out.println("Z location : "+t.getZ());
			particlesList.add(t);
		}
	}
	
	public double calDST(int step){		
		if(velocity < 0){
			System.out.println("velocity is negative!");
			System.exit(0);
		}
		
		if(step == 0)
			DST[step] = 1.9288*distance*distance;
		else{
			tempD = tempD - velocity*DST[step-1]*0.001;
			DST[step] = 1.9288*(tempD)*(tempD);
		}
		return DST[step];
	}
	
	public void setTrans_endTime(double n)
	{
		this.Trans_endTime = n;
	}
	
	public void setTrans_startTime(double n)
	{
		this.Trans_startTime = n;
	}
	
	public void setDynamicSymbolTime(double n)
	{
		this.dynamicSymboltime = n;
	}
	
	public void setDistance(double n)
	{
		this.distance = n;
	}
	
	public void setTempD(double n)
	{
		this.tempD = n;
	}
	public void setVelocity(double n)
	{
		this.velocity = n;
	}
	
}
