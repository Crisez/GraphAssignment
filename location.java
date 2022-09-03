/*
 * Zach Crise
 * CIT360
 * 12/4/19
 * 
 * Objective: Store location information such as latitude, longitude, ID, and name
 * 
 * 
 */
public class location<T> {
	private int vertexID;
	private double latitude;
	private double longitude;
	private double num0;
	private String vertexName;
	
	//store the id, latitude, longitude, the zero, and the name of the location
	location(int id, double lat, double lon, double zero, String name){
		this.vertexID = id;
		this.latitude = lat;
		this.longitude = lon;
		this.num0 = zero;
		this.vertexName = name;
	}
	
	//getters
	public int getID() {
		return vertexID;
	}
	public double getLat(){
		return latitude;
	}
	public double getLon() {
		return longitude;
	}
	public String getName() {
		return vertexName;
		
	}
	
}
