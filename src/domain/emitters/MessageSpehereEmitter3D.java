package domain.emitters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import ui.FileUI;
import ui.UI;
import ui.UIver2;
import data.DataInterface;
import domain.particles.ColoredParticle3D;
import domain.particles.Particle;
import domain.particles.ParticleXComparator;
import domain.particles.Sphere;
import domain.particles.Sphere3D;

public class MessageSpehereEmitter3D extends NEWMOSKSphereEmitter{

	double _z;
	
	File wavefile ; 
	
	private ArrayList<String> _names = new ArrayList<String>(); // lists of variables (names list and values list)
	private ArrayList<String> _values = new ArrayList<String>();
	
	
	public MessageSpehereEmitter3D(double x, double y, double z, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double Trans_startTime,
			String message, int period, int type, int emitPeriod,String filename) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,
				punctual, concentrationEmitter, color, Trans_startTime, message,
				period, type, emitPeriod);
		
		this._z = z;
		try {
			readFile(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
		
		this.emitPeriod = getNextInt("emitperiod");
		this._amplitude = getNextInt("amplitude");
		this.Trans_Message = this.Preamble+getNextString("message");
		this.Trans_endTime = Trans_startTime + Trans_Message.length()*period;
		
		
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
	
	public void addParticles(int numParticles, ArrayList<Particle> particlesList, int type) {

		for(int i=0; i< numParticles; i++){
			ColoredParticle3D t = new ColoredParticle3D(_x, _y, _z, _sphereRadius, 0, 0, 0, _color, type);	
			//System.out.println("Z location : "+t.getZ());
			particlesList.add(t);
		}
	}
	
    public double getNumParticles(double time) {
        if(time >= _startTime && time < _endTime){
            if(i < Trans_Message.length()){
            	//System.out.println("check error1");
                if(time >= Trans_startTime && time < Trans_endTime){
                	//System.out.println("check error2");
                    i = (int) ((time - Trans_startTime)/period);
                    if( i >= 0 ){
                    	//System.out.println("check error3");
                        if(time >=Trans_startTime+i*period && time<Trans_startTime+i*period+emitPeriod)
                        {
                        	System.out.println("check error4");
                            if(Trans_Message.charAt(i) == "1".charAt(0))
                        	{
                            	System.out.println("check error5");
                                System.out.println(i+" "+time);
                                return _amplitude;
                            }
                            else{
                                return 0;
                            }
                    }
                    }
                    else{
                        if(i!=0) i++;
                        return 0;
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
	
}
