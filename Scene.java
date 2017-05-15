import java.util.*;
/* Scene.java
 * Description: This class is responsible for handling the data and behaviors of
 * scenes in the board game. It is mostly repsonsible for encapsulating the scene
 * card data and returning that data when necessary.
 */ 
public class Scene {
	private String name;
	private int budget;
	private ArrayList<Star> starRoles;

	public Scene(String name, int budget, ArrayList<Star> starRoles){
		this.name = name;
		this.budget = budget;
		this.starRoles = starRoles;
	}

	int getBudget(){
		return this.budget;
	}

	ArrayList<Star> getStarRoles(){
		return this.starRoles;
	}

	String getSceneName(){
		return this.name;
	}
}
