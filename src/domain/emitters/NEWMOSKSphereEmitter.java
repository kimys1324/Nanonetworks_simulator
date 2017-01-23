package domain.emitters;

import java.util.ArrayList;

import domain.particles.Particle;

public class NEWMOSKSphereEmitter extends MOSKSphereEmitter {

	
    int emitPeriod;

	public NEWMOSKSphereEmitter(double x, double y, double startTime,
			double endTime, double amplitude, double sphereRadius,
			double emitterRadius, double initV, boolean punctual,
			boolean concentrationEmitter, String color, double Trans_startTime,
			String message, int period, int type, int emitPeriod) {
		super(x, y, startTime, endTime, amplitude, sphereRadius, emitterRadius, initV,
				punctual, concentrationEmitter, color, Trans_startTime, message,
				period, type);

		this.emitPeriod=emitPeriod;

	}
	

    public void emit(double time, ArrayList<Particle> particlesList){
		int n = (int)getNumParticles(time);
		int deleted = 0;
		if(n > 0){
			deleted = deleteParticles(particlesList.size(), particlesList);
			if (_concentrationEmitter) addParticles(n, particlesList, type);
			else addParticles(n+deleted, particlesList, type);
		}
		else if (n < 0){
			n *= -1;
			deleteParticles(n, particlesList);
		}
	}
    
    @Override
    double getNumParticles(double time) {
        if(time >= _startTime && time < _endTime){
            if(i < Trans_Message.length()){
                if(time >= Trans_startTime && time < Trans_endTime){
                    i = (int) ((time - Trans_startTime)/period);
                    if( i >= 0 ){
                        if(time >=Trans_startTime+i*period && time<=Trans_startTime+i*period+emitPeriod)
                        {
                            if(Trans_Message.charAt(i) == "1".charAt(0))
                        	{
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

}
