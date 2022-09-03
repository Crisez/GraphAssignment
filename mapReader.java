/*
 * Zach Crise
 * CIT360
 * 12/3/19
 * 
 * Objective: Create a test class for the updated WDGraph to read the map.dat file 
 * and read its contents to fill a graph to perform cases on Dijkstra's algorithm 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class mapReader {

	public static <T> void main(String[] args) {
		try {
			String numVSTR;
			String numESTR;
			String numCSTR;
			
			int numE;
			int numV;
			int numC;
			int vert1;
			int vert2;
			String[] tempArray;
			
			File airportMap = new File("map.dat");
			Scanner reader = new Scanner(airportMap);
			WDGraph<String> graph = new WDGraph<String>();
			ArrayList pathS = new ArrayList();
			
			//first line from the file is the number of vertices, so parse it to an int and and use it to set up the location array
				numVSTR = reader.nextLine();
				numV = Integer.parseInt(numVSTR);
				location[] localVert = new location[numV];
				
				for(int i = 0; i < numV; i ++) {
					//take all of the vertices and split them by , so they can be stored by the location class for proper use. 
					tempArray = reader.nextLine().split(",");
					//add the new location to the array with the ID, latitude, longitude, zero, and name
					localVert[i] = new location(Integer.parseInt(tempArray[0]), Double.parseDouble(tempArray[2]), Double.parseDouble(tempArray[1]), Double.parseDouble(tempArray[3]), tempArray[4]);
					
					//add the location name to the graph
					graph.addVertex(localVert[i].getName());
					
				}
				
				//get and parse the number of edges
				numESTR = reader.nextLine();
				numE = Integer.parseInt(numESTR);
	
				for(int i = 0; i < numE; i++) {
					//split and parse the index of both vertices of the edge
					tempArray = reader.nextLine().split(" ");
					
					vert1 = Integer.parseInt(tempArray[0]);
					vert2 = Integer.parseInt(tempArray[1]);
					
					//if the number at the end of the line is a 2 then that means it is bidirectional so make two edges else make one
					//add the name of both vertices and then put in the distance by calling the distance method with the lat and lon of both vertices
					if(Integer.parseInt(tempArray[2]) == 2) {
						graph.addEdge(localVert[vert1].getName(), localVert[vert2].getName(), distance(localVert[vert1].getLat(), localVert[vert1].getLon(), localVert[vert2].getLat(), localVert[vert2].getLon()));
						graph.addEdge(localVert[vert2].getName(), localVert[vert1].getName(), distance(localVert[vert1].getLat(), localVert[vert1].getLon(), localVert[vert2].getLat(), localVert[vert2].getLon()));
					}
					else {
						graph.addEdge(localVert[vert1].getName(), localVert[vert2].getName(), distance(localVert[vert1].getLat(), localVert[vert1].getLon(), localVert[vert2].getLat(), localVert[vert2].getLon()));
					}
					
				}
				
				//prep the directions file for the output
				FileWriter file = new FileWriter("directions.dat", true);
				PrintWriter printToFile = new PrintWriter(file, true);
				
				//get and parse the number of cases
				numCSTR = reader.nextLine();
				numC = Integer.parseInt(numCSTR);
				
				for(int i = 0; i < numC; i++) {
					//split and parse the index of both vertices of the case
					tempArray = reader.nextLine().split(" ");
					
					vert1 = Integer.parseInt(tempArray[0]);
					vert2 = Integer.parseInt(tempArray[1]);
					
					//get the arraylist containing directions of the shortest path and the total distance by using the names of both vertices
					pathS = graph.shortestPath(localVert[vert1].getName(), localVert[vert2].getName());
					
					//print the output and write it to the file directions.dat
					
					//small overview stating to follow directions to get from point a to point b
					System.out.println("Follow the directions below to travel from Point " + localVert[vert1].getID() + ": " 
					+ localVert[vert1].getName() + " to Point " + localVert[vert2].getID() + ": " + localVert[vert2].getName() + ". Total distance needed to travel is given at the end of the directions.");
					System.out.println("");
					//print the arraylist of the shortest path and total distance
					System.out.println(pathS);
					//spacing for formatting
					System.out.println("");
					System.out.println("");
					System.out.println("");
					
					//small overview stating to follow directions to get from point a to point b
					printToFile.println("Follow the directions below to travel from Point " + localVert[vert1].getID() + ": " 
					+ localVert[vert1].getName() + " to Point " + localVert[vert2].getID() + ": " + localVert[vert2].getName() + ". Total distance needed to travel is given at the end of the directions.");
					printToFile.println("");
					//print the arraylist of the shortest path and total distance
					printToFile.println(pathS);
					//spacing for formatting
					printToFile.println("");
					printToFile.println("");
					printToFile.println("");
				}
			
			printToFile.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("map.dat could not be found");
		} catch(IOException e) {
			System.out.println("An IOException has occurred");
		}
		
		
	}
	
	/**
	 * Code to calculate distance from https://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates
	 * used in distance and degToRad
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		double latR = degToRad(lat2-lat1);
		double lonR = degToRad(lon2-lon1);
		double latR1 = degToRad(lat1);
		double latR2 = degToRad(lat2);
		
		
		double a = Math.sin(latR/2) * Math.sin(latR/2) + Math.sin(lonR/2) * Math.sin(lonR/2) * Math.cos(latR1) * Math.cos(latR2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		//3959 miles is the earth's radius * 5280 to make it 20903520feet
		return 20903520 * c;
	}
	
	public static double degToRad(double deg) {
		return deg * Math.PI/180;
		
	}
}



