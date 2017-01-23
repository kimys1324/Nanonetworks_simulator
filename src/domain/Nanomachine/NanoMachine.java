package domain.Nanomachine;

import domain.emitters.Emitter;
import domain.emitters.MobileEmitter;
import domain.receivers.Receiver;
import domain.receivers.MobileSphericalReceiver3D;

public class NanoMachine {
	private Emitter emitter;
	private Receiver receiver;
	
	
	public NanoMachine(Emitter e, Receiver r)
	{
		this.emitter = e;
		this.receiver = r;
	}
	
	public void updatePosition()
	{
		((MobileEmitter) emitter).updatePosition();
		((MobileSphericalReceiver3D) receiver).updatePosition();;
	}
	
}
