/* Star.java
 * Description: This class maintains information specific to the star role.
 * It is also responsible for handling the way that each player gets paid if they
 * are currently on an star role.
 */
public class Star extends Role {
	private static String type;
	public Star(String name, String quote, int reqRank){
		super(name, quote, reqRank);
		this.type = "Star";
	}

	void payout(){
		System.out.println("You got 2 credits!");
		this.playerOnRole.incCredits(2);
	}

	String getType(){
		return this.type;
	}
}
