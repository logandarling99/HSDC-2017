import java.util.Arrays;

import com.sailsim.app.*;

public class ShipControls {
 

	
	public static void main(String[] args){	
		NavInterfaceClient simAPI = new NavInterfaceClient();
		new ShipControls().NavClient(simAPI);
		new ShipControls().triangles(simAPI);
		new ShipControls().aikappa(simAPI);
	}
	
	void NavClient(NavInterfaceClient simAPI){

		String serverHost = "sim.sailsim.org";
		int serverPort = 20170;
		String username = "SSArk";
		String password = "unmarked";
		/** Attempt to establish a connection with the server */
		boolean connected = false;
		try {
			connected = simAPI.connect(serverHost, serverPort, username, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/** Check whether the connection was successful */
		if (connected) {
				 System.out.println("Connection established.");
			}
			else {
			 System.err.println("Something went wrong…");
			};				
			}
	
	void aikappa(NavInterfaceClient simAPI){
		 String[] msg;
	        try {
	            double windHeading, boatHeading, sailAngle, absoAngle, goalAngle;

	            simAPI.send("windHeading");
	            msg = simAPI.receive();
	            System.out.println(msg[1]);
	            windHeading = Double.parseDouble(msg[1]);

	            simAPI.send("sailAngle");
	            msg = simAPI.receive();
	            System.out.println(msg[1]);
	            sailAngle = Double.parseDouble(msg[1]);

	            simAPI.send("boatHeading");
	            msg = simAPI.receive();
	            System.out.println(msg[1]);
	            boatHeading = Double.parseDouble(msg[1]);

	            absoAngle = boatHeading + sailAngle;
	            System.out.printf("\n\n%s", absoAngle);
	            goalAngle = windHeading - absoAngle;
	            System.out.printf("\n\n%s", goalAngle);
	            goalAngle %= 90;
	            System.out.printf("\n\n%s", goalAngle);
	            simAPI.send("sailAngle " + Double.toString(goalAngle - 20));
	            msg = simAPI.receive();
	            simAPI.send("anchor False");

	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }

	
	void triangles (NavInterfaceClient simAPI){
		String boat[];
		String goal[];
		String msg[];
		String bx;
		String by;
		String gx;
		String gy;
		Double TWOPI = 6.2831853071795865;
		Double RAD2DEG = 57.2957795130823209;
		try{
			simAPI.send("boatPosition");
			boat = simAPI.receive();
			System.out.printf("\n\n%s = boat", boat[1]);
			String[] parts = boat[1].split(" ");
			bx = parts[0];
			by = parts[1];
			System.out.printf("\n\n%s = boat x", bx);
			System.out.printf("\n\n%s = boat y", by);
			Double x1 = Double.parseDouble(bx);
			Double y1 = Double.parseDouble(by);
			
			simAPI.send("goalPosition");
			goal = simAPI.receive();
			System.out.printf("\n\n%s = goal", goal[1]);
			String[] parts1 = goal[1].split(" ");
			gx = parts1[0];
			gy = parts1[1];
			System.out.printf("\n\n%s = goal x", gx);
			System.out.printf("\n\n%s = goal y", gy);
			Double x2 = Double.parseDouble(gx);
			Double y2 = Double.parseDouble(gy);
			
			double theta = Math.atan2((x2-x1), (y2 - y1));
			if (theta < 0.0) theta += TWOPI;
		    theta = RAD2DEG * theta;
			System.out.printf("\n\n%s = theta", theta);
			
			simAPI.send("boatHeading");
			msg = simAPI.receive();
			System.out.printf("\n\n%s = boatheading", msg[1]);
			simAPI.send("boatHeading", Double.toString(theta));
			simAPI.receive();
		}
		catch(Exception e){
		System.out.println("Please no");
		}
	}

}
