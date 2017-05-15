import java.util.*;
/* Room.java
 * This class is responsible for storing the data of each room in the board game as well
 * as paying out all of the players in the room who are currently on roles when the room
 * is ready to close
 */
public class Room {
	private String name;
	private Scene currentScene;
	private int numShots;
	private ArrayList<Extra> extraRoles;
	private ArrayList<String> adjacentRooms;
	private ArrayList<Player> currentPlayers;
	private boolean isActive;
	private int origShots;

	public Room(String roomName, int numS, ArrayList<Extra> roles, ArrayList<String> adjRooms){
		this.extraRoles = roles;
		this.numShots = numS;
		this.origShots = numS;
		this.name = roomName;
		this.adjacentRooms = adjRooms;
		this.currentPlayers = new ArrayList<Player>();
		this.isActive = true;
		this.currentScene = null;
	}

	public Room(String roomName, ArrayList<String> adjRooms){
		this.name = roomName;
		this.adjacentRooms = adjRooms;
		this.currentPlayers = new ArrayList<Player>();
	}

	String getRoomName(){
		return this.name;
	}
	ArrayList<String> getAdjacentRooms(){
		return this.adjacentRooms;
	}

	ArrayList<Player> getCurrentPlayers(){
		return this.currentPlayers;
	}

	void addCurrentPlayer(Player addPlayer){
		this.currentPlayers.add(addPlayer);
	}

	void removeCurrentPlayer(int playerid){
		for(int i = 0; i < this.currentPlayers.size(); i++){
			if(this.currentPlayers.get(i).getid() == playerid){
				this.currentPlayers.remove(i);
			}
		}
	}

	ArrayList<Extra> getRoles(){
		return this.extraRoles;
	}

	void resetRoom(){
		if(!this.name.equals("Trailers") && !this.name.equals("CastingOffice")){
			this.isActive = true;
			this.numShots = this.origShots;
			this.currentScene = null;
			for(Extra extra : this.extraRoles){
				extra.setPlayerOnRole(null);
				extra.setAvailability(true);
			}
		}
	}

	void wrapUpRoom(){
			/*check bonus*/
			boolean bonus = false;
			for(Player actor : this.currentPlayers){
				if(actor.isWorking()){
					if(actor.getCurrentRole().getType().equals("Star")){
						bonus = true;
					}
				}
			}

			if(bonus == true){
				/*payout star players*/
				int numRolls = this.currentScene.getBudget();
				ArrayList<Integer> rolls = new ArrayList<Integer>();
				Random dice = new Random();
				for(int i = 0; i < numRolls; i++){
					int roll = dice.nextInt(6)+1;
					rolls.add(roll);
				}
				Collections.sort(rolls);
				Collections.reverse(rolls);
				System.out.println(numRolls+" dice were rolled for the bonus.");
				System.out.print("The following rolls were:");
				for(int i = 0; i < rolls.size(); i++){
					System.out.print(" "+rolls.get(i));
				}
				System.out.println();
				ArrayList<Star> starRoles = this.currentScene.getStarRoles();
				int starSize = starRoles.size();
				for(int i = 0; i < rolls.size(); i++){
					int starInd = (i % starSize);
					Star currentstar = starRoles.get(starSize-starInd-1);
					System.out.println("Star "+(starSize-starInd)+" "+currentstar.getName()+" received "+rolls.get(i)+" dollars.");
					Player starPlayer = currentstar.getPlayerOnRole();
					if(starPlayer != null){
						starPlayer.incDollars(rolls.get(i));
					}
				}

				/*payout extra players*/
				int extraSize = this.extraRoles.size();
				for(int i = 0; i < extraSize; i++){
						System.out.println("Extra "+(i+1)+" "+this.extraRoles.get(i).getName()+" received "+this.extraRoles.get(i).getRank()+" dollars.");
						Player extraPlayer = this.extraRoles.get(i).getPlayerOnRole();
						if(extraPlayer != null){
							extraPlayer.incDollars(this.extraRoles.get(i).getRank());
						}
				}

			}else{
				System.out.println("There was no star roles so there is no bonus.");
			}

			/*close out room*/
			for(Player players : this.currentPlayers){
				players.setRoleStatus(false);
				players.updateRole(null);
				players.resetBonus();
			}
			for(Extra extraRole : this.extraRoles){
				extraRole.setAvailability(false);
			}
			for(Star starRole : this.currentScene.getStarRoles()){
				starRole.setAvailability(false);
			}
			/* set Room to inactive */
			this.isActive = false;
	}

	Boolean isActive(){
		return this.isActive;
	}

	void decrementShot(){
		this.numShots--;
	}

	Scene getScene(){
		return this.currentScene;
	}

	void setScene(Scene randomScene){
		this.currentScene = randomScene;
	}

	int getShotNum(){
		return this.numShots;
	}

	String roomName(){
		return this.name;
	}

	void displayInfo(){
		System.out.print("In "+this.name);
		if(this.currentScene != null){
			System.out.print(" shooting "+this.currentScene.getSceneName()+"with a budget of "+this.currentScene.getBudget());
		}
		System.out.println();
		System.out.print("Current Players in Room (by ID): ");
		for(int i = 0; i < this.currentPlayers.size(); i++){
			System.out.print(this.currentPlayers.get(i).getid()+" ");
		}
		System.out.println();
		System.out.println("Shot counters left: "+this.numShots);
	}

	void displayAdjRooms(){
		for(int i=0; i < adjacentRooms.size(); i++){
			System.out.println(adjacentRooms.get(i));
		}
	}

	void displayRoles(){
		if(!this.name.equals("Trailers") && !this.name.equals("CastingOffice")){
			for(int i=0; i < extraRoles.size(); i++){
				int roleNum = i+1;
				System.out.print("Extra "+roleNum+": "+extraRoles.get(i).getName()+ " Required Rank: "+extraRoles.get(i).getRank());
				System.out.print(" Availability: ");
				if(extraRoles.get(i).isAvailable()){
					System.out.print("Yes");
				} else {
					System.out.print("No");
				}
				System.out.println();
			}

			for(int i=0; i< currentScene.getStarRoles().size(); i++){
				int roleNum = i+1;
				System.out.print("Star "+roleNum+": "+currentScene.getStarRoles().get(i).getName()+" Required Rank: "+ currentScene.getStarRoles().get(i).getRank());
				System.out.print(" Availability: ");
				if(currentScene.getStarRoles().get(i).isAvailable()){
					System.out.print("Yes");
				} else {
					System.out.print("No");
				}
				System.out.println();
			}
		}
	}
}
