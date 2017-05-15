/* Extra.java
 * Description: This class maintains information specific to the extra role.
 * It is also responsible for handling the way that each player gets paid if they
 * are currently on an extra role.
 */
public class Extra extends Role{
	private static String type;
	public Extra(String name, String quote, int reqRank){
		super(name, quote, reqRank);
		this.type = "Extra";
	}

	void payout(){
		System.out.println("You got $1!");
		this.playerOnRole.incDollars(1);
	}

	String getType(){
		return this.type;
	}
}
