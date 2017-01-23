package domain.emitters;

public class CSKSphereEmitter extends SphereEmitter {

	String Preamble = "";
	public static String Trans_Message;
	String Message;
	public static double Trans_startTime;
	public static double Trans_endTime;
	int period;
	int i=0;
	public static String tempMessage;
	public CSKSphereEmitter(double x, double y, double startTime, double endTime,
			double amplitude, double sphereRadius, double emitterRadius,
			double initV, boolean punctual, boolean concentrationEmitter,
			String color, double Trans_startTime, String message, int period ) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,
				punctual, concentrationEmitter, color);
		this.Message = message;
		this.Trans_startTime = Trans_startTime;
		this.period = period;
		Trans_Message = Preamble + Message;
		tempMessage = Trans_Message;
		Trans_endTime = Trans_startTime + Trans_Message.length()*period;
		// TODO Auto-generated constructor stub
	}

	@Override
	double getNumParticles(double time) {
		if(time >= _startTime && time < _endTime){
			if(i < Trans_Message.length()){
				if(time >= Trans_startTime && time < Trans_endTime){
					i = (int) ((time - Trans_startTime)/period);
					if( i >= 0 ){
					    
					        if(Trans_Message.charAt(i) == "1".charAt(0)){
					            System.out.println(i+" "+time);
					            return _amplitude;
					        }
					        else{
					            return 0;
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
	public String getDescription(){
		String s = super.getDescription();
		s += "period: " + period + "(ns)\n";
		s += "Trans_startTime: " + Trans_startTime + "\n";
		s += "message" + Message + "\n";
		return s;
	}
	


}
