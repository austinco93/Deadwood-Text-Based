import java.util.*;
/* Player.java
 * Description: This class is the player class for the board game, it handles
 * all of the possible actions that the player can take as well has all of the player
 * information
 */
public class Player {
	private String name;
	private int id;
	private int credits;
	private int dollars;
	private int rank;
	private int currentBonus;
	private Room currentRoom;
	private Role currentRole;
	private boolean turnStatus;
	private boolean moveStatus;
	private boolean roleStatus;

	public Player(int id){
		this.id = id;
		this.rank = 1;
		this.currentBonus = 0;
		this.credits = 0;
		this.dollars = 0;
		this.currentRole = null;
		this.currentRoom = null;
		this.turnStatus = true;
		this.moveStatus = false;
		this.roleStatus = false;
	}

	void incDollars(int amt){
		this.dollars += amt;
	}

	void incCredits(int amt){
		this.credits += amt;
	}

	int roll(){
		Random dice = new Random();
		int roll = dice.nextInt(6)+1;
		roll = roll + this.currentBonus;
		return roll;
	}

	String[] checkTurnOptions(){
		String[] turns = new String[2];
		return turns;
	}

	boolean hasTurn(){
		return this.turnStatus;
	}

	boolean isWorking(){
		return this.roleStatus;
	}

	void setRoleStatus(boolean val){
		roleStatus = val;
	}
	boolean hasMoved(){
		return this.moveStatus;
	}

	void setMoveStatus(boolean val){
		this.moveStatus = val;
	}

	void setTurnStatus(boolean val){
		this.turnStatus = val;
	}

	Role getCurrentRole(){
		return this.currentRole;
	}
	void move(Room newRoom){
		if(newRoom != null){
			ArrayList<String> adjRooms = new ArrayList<String>();
			adjRooms = this.currentRoom.getAdjacentRooms();
			if(adjRooms.contains(newRoom.getRoomName())){
				System.out.println("You moved to "+newRoom.getRoomName()+"!");
				updateRoom(newRoom);
				setMoveStatus(true);
			} else {
				System.out.println("You can not move to that location");
			}
		} else {
		 	System.out.println("Invalid Location");
		}
	}

	void upgrade(String cashOrCred, int newRank){
		int[] dollarAr = {4,10,18,28,40};
		int[] creditAr = {5,10,15,20,25};
		if(this.rank != 6 && this.currentRoom.roomName().equals("CastingOffice") && this.rank < newRank && newRank < 7){
			if(cashOrCred.equals("$") && this.dollars >= dollarAr[newRank-2]){
				this.dollars = this.dollars - dollarAr[newRank-2];
				this.rank = newRank;
			}
			else if(cashOrCred.equals("cr") && this.credits >= creditAr[newRank-2]){
				this.credits = this.credits - creditAr[newRank-2];
				this.rank = newRank;
			} else {
				System.out.println("Not enough currency to complete request");
			}
		} else {
			System.out.println("Unable to complete request");
		}
	}

	void takeRole(String roleType, int roleNum){
			if(roleType.equals("Star")){
				if(this.currentRoom.getScene().getStarRoles().size() >= roleNum && roleNum > 0){
					if(this.currentRoom.getScene().getStarRoles().get(roleNum-1).isAvailable()){
						if(this.rank >= this.currentRoom.getScene().getStarRoles().get(roleNum-1).getRank()){
							updateRole(this.currentRoom.getScene().getStarRoles().get(roleNum-1));
							setTurnStatus(false);
						} else {
							System.out.println("Not high enough rank for this role");
						}
					} else {
						System.out.println("Role already taken");
					}
				} else {
					System.out.println("Role number out of bounds");
				}
			} else if (roleType.equals("Extra")){
					if(this.currentRoom.getRoles().size() >= roleNum && roleNum > 0){
						if(this.currentRoom.getRoles().get(roleNum-1).isAvailable()){
							if(this.currentRoom.getRoles().get(roleNum-1).getRank() <= this.rank){
								updateRole(this.currentRoom.getRoles().get(roleNum-1));
								setTurnStatus(false);
							} else{
								System.out.println("Not high enough rank for this role");
							}
						} else{
							System.out.println("Roles is already taken");
						}
					} else {
						System.out.println("Role number out of bounds");
					}
				} else {
					System.out.println("Invalid Command");
				}
	}

	void act(){
		if(this.roleStatus == true){
			int rollNum = roll();
			System.out.println("You rolled a "+rollNum+"! (bonus included).");
			if(rollNum >= this.currentRoom.getScene().getBudget()){
				System.out.println("You have successfully acted!");
				this.currentRole.payout();
				this.currentRoom.decrementShot();
				setTurnStatus(false);
			} else {
				System.out.println("Your acting attempt has failed.");
				setTurnStatus(false);
			}
		} else {
			System.out.println("You are not currently working on a role.");
		}
	}
	void updatePlayerCurrency(int newCredits, int newDollars){
		this.credits = newCredits;
		this.dollars = newDollars;
	}

	void updateRank(int newRank){
		this.rank = newRank;
	}

	void incrementBonus(){
		this.currentBonus++;
	}

	void resetBonus(){
		this.currentBonus = 0;
	}

	void updateRoom(Room newRoom){
		if(this.currentRoom != null){
			this.currentRoom.removeCurrentPlayer(this.id);
		}
		this.currentRoom = newRoom;
		this.currentRoom.addCurrentPlayer(this);
	}

	void updateRole(Role newRole){
		if(currentRole != null){
			this.currentRole.setPlayerOnRole(null);
		}
		this.currentRole = newRole;
		if(newRole != null){
			this.currentRole = newRole;
			this.roleStatus = true;
			newRole.setAvailability(false);
			newRole.setPlayerOnRole(this);
			System.out.println("You are now working on a role!");
		}
	}

	void rehearse(){
		if(isWorking()){
			if(this.currentBonus == this.currentRoom.getScene().getBudget()-1){
				System.out.println("You already have the maximum bonus necessary");
			} else {
				incrementBonus();
				setTurnStatus(false);
				System.out.println("You have successfully rehearsed! Your current bonus is "+this.currentBonus);
			}
		} else {
			System.out.println("You are not currently working on a role!!");
		}
	}

	int score(){
		return this.credits + this.dollars + (5*this.rank);
	}
	String getName(){
		return this.name;
	}
	int getCredits(){
		return this.credits;
	}

	int getDollars(){
		return this.dollars;
	}

	int getRank(){
		return this.rank;
	}

	int getid(){
		return this.id;
	}

	Room getCurrentLocation(){
		return this.currentRoom;
	}

	void displayInfo(){
		System.out.format("Player %d ($%d,%dcr)", this.id, this.dollars, this.credits);
		if(this.currentRole != null){
			System.out.print(" working on "+this.currentRole.getName());
		}
		System.out.println();
		System.out.println("Current Rank: "+this.rank);
	}
}
