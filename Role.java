/* Role.java
 * Description: This class handles the data and behaviors for roles in the board game.
 * It is important to note that this is an abstract class and is therefore inherited and instantiated
 * by its two subclasses Extra.java and Star.java.
 */
public abstract class Role {
	private String name;
	private int reqRank;
	private String quote;
	abstract void payout();
	abstract String getType();
	private boolean available;
	public Player playerOnRole;
	public Role(String name, String quote, int reqRank){
		this.name = name;
		this.quote = quote;
		this.reqRank = reqRank;
		this.available = true;
		this.playerOnRole = null;
	}

	void setRole(String name, String quote, int rank){
		this.name = name;
		this.reqRank = rank;
		this.quote = quote;
	}

	Player getPlayerOnRole(){
		return this.playerOnRole;
	}

	void setPlayerOnRole(Player rolePlayer){
		this.playerOnRole = rolePlayer;
	}

	boolean isAvailable(){
		return this.available;
	}

	void setAvailability(boolean val){
		this.available = val;
	}

	int getRank(){
		return this.reqRank;
	}

	String getName(){
		return this.name;
	}
}
