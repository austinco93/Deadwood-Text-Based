import java.util.*;
import java.io.*;
/* Board.java
 * Description: This class is responsible for handling all of the game data was well as
 * providing methods for handling the flow of the game. These methods will be execute in
 * Deadwood.java
 */
public class Board {
	private static int dayNum;
	private static int finRooms;
	private static int dayTotal;
	private static ArrayList<Room> rooms;
	private static ArrayList<Scene> scenes;
	private static Queue<Player> players;

	public Board(){
		this.rooms = new ArrayList<Room>();
		this.scenes = new ArrayList<Scene>();
		this.players = new LinkedList<Player>();
		this.finRooms = 0;
		this.dayNum = 1;
		this.dayTotal = 0;
	}
	/* f1 = roomSetup.txt f2 = adjSetup.txt f3 = sceneSetup.txt */
	void setupBoard(String f1, String f2, String f3){
		ArrayList<Extra> roles = new ArrayList<>(); //Role

    	ArrayList<String> adjRooms = new ArrayList<>();

		Scanner roomScanner = null;
		Scanner adjScanner = null;

		/*setup scanners*/
		try{
			roomScanner = new Scanner(new File(f1));
		} catch(FileNotFoundException e){
			System.exit(2);
		}

		try{
			adjScanner = new Scanner(new File(f2));
		} catch(FileNotFoundException e){
			System.exit(2);
		}

		/* add room info and adj room info */
		while(roomScanner.hasNextLine() && adjScanner.hasNextLine()){
			String roomInfo = roomScanner.nextLine();
			String adjInfo = adjScanner.nextLine();

			/* scan each line*/
			Scanner roomInfoScan = new Scanner(roomInfo);
			Scanner adjInfoScan = new Scanner(adjInfo);
			String roomName1 = roomInfoScan.next();
			String roomName2 = adjInfoScan.next();

			if(roomName1.equals("Trailers") || roomName1.equals("CastingOffice")){
				while(adjInfoScan.hasNext()){
					String adjRoom = adjInfoScan.next();
					adjRooms.add(adjRoom);
				}
				rooms.add(new Room(roomName1,adjRooms));
				adjRooms = new ArrayList<>();
			} else {
				int numShots = roomInfoScan.nextInt();
				while(roomInfoScan.hasNext()){
					int reqRank = roomInfoScan.nextInt();
					String roleName = roomInfoScan.next();
					String quote = roomInfoScan.next();
					roleName = convertString(roleName);
					quote = convertString(quote);
					roles.add(new Extra(roleName, quote, reqRank));

					while(adjInfoScan.hasNext()){
						String adjRoom = adjInfoScan.next();
						adjRooms.add(adjRoom);
					}
				}
				rooms.add(new Room(roomName1, numShots, roles, adjRooms));
				roles = new ArrayList<>();
				adjRooms = new ArrayList<>();
			}
		}

		/*setup Scene cards*/
		ArrayList<Star> starRoles = new ArrayList<>();

		Scanner sceneScanner = null;
		try{
			sceneScanner = new Scanner(new File(f3));
		} catch (FileNotFoundException e){
			System.exit(2);
		}

		while(sceneScanner.hasNextLine()){
			String sceneLine = sceneScanner.nextLine();
			Scanner sceneInfo = new Scanner(sceneLine);
			String sceneName = sceneInfo.next();
			sceneName = convertString(sceneName);
			int budget = sceneInfo.nextInt();
			while(sceneInfo.hasNext()){
				int reqRank = sceneInfo.nextInt();
				String roleName = sceneInfo.next();
				roleName = convertString(roleName);
				String quote = sceneInfo.next();
				quote = convertString(quote);
				starRoles.add(new Star(roleName, quote, reqRank));
			}
			scenes.add(new Scene(sceneName, budget, starRoles));
			starRoles = new ArrayList<>();
		}
	}

	String convertString(String word){
		String returnWord = word;
		if(word.contains("+")){
			String[] split_word = returnWord.split("\\+");
			StringBuilder builder = new StringBuilder();
			for(String w : split_word){
				builder.append(w);
				builder.append(" ");
			}
			returnWord = builder.toString();
		}
		return returnWord;
	}

	public void initGame(){
		Scanner term = new Scanner(System.in);
		int playerNum = 0;
		LinkedList<Player> playerOrder = new LinkedList<Player>();
		System.out.println("Starting Deadwood!\n");
		System.out.println("Please Enter Number of Players (2- 8)");
		boolean correctVal = false;
		while(correctVal == false){
			try{
				playerNum = term.nextInt();
				if(playerNum < 2 || playerNum > 8){
					System.out.println("Please enter number in the range specified");
				} else {
					correctVal = true;
				}
			} catch (InputMismatchException e){
				System.out.println("Please enter number in the range specified");
			}
		}
		for(int i= 0; i < playerNum; i++){
			playerOrder.add(new Player(i+1));
		}
		Collections.shuffle(playerOrder);
		setPlayers(playerOrder);
		setDayTotal(playerNum);
		resetPlayerLocation();
	}

	public Room getRoom(String roomName){
		Room returnRoom = null;
		for(int i = 0; i < rooms.size(); i++){
			if(rooms.get(i).getRoomName().equals(roomName)){
				returnRoom = rooms.get(i);
			}
		}

		return returnRoom;
	}

	public void dayReset(){
		this.finRooms = 0;
		this.dayNum++;

		resetPlayerStatus();
		resetPlayerLocation();
		for(Room room: this.rooms){
			room.resetRoom();
		}
		distributeScenes();
	}

	public void resetPlayerStatus(){
		for(Player player :this.players){
			player.resetBonus();
			player.updateRole(null);
			player.setRoleStatus(false);
		}
	}

	public void resetPlayerLocation(){
		for(Player player: this.players){
			player.updateRoom(getRoom("Trailers"));
		}
	}

	public boolean endDayCheck(){
		if(this.finRooms == 9){
			System.out.println("Day is over, resetting player positions");
			return true;
		} else {
			return false;
		}
	}

	public void scorePlayers(){
		System.out.println("Game is over. Thank you for playing!");
		Player winner = players.remove();
		players.add(winner);
		for(Player player: this.players){
				if(winner.score() < player.score()){
					winner = player;
				}
		}

		System.out.println("The winner is player "+winner.getid()+"!");
	}

	public void setPlayers(LinkedList<Player> players){
		this.players = players;
	}

	public void setDayTotal(int numPlayers){
		if(numPlayers < 4){
			this.dayTotal = 3;
		} else {
			this.dayTotal = 4;
		}

		if(numPlayers == 5){
			for(Player player : this.players){
				player.incCredits(2);
			}
		}

		if(numPlayers == 6){
			for(Player player : this.players){
				player.incCredits(4);
			}
		}

		if(numPlayers == 7 || numPlayers ==8){
			for(Player player : this.players){
				player.updateRank(2);
			}
		}
	}

	public void distributeScenes(){
		/* Assign Scenes to Rooms */
		Random rand = new Random();
		for(Room room: this.rooms){
			if(!room.roomName().equals("Trailers") && !room.roomName().equals("CastingOffice")){
				int index = rand.nextInt(this.scenes.size());
				Scene randomScene = scenes.get(index);
				room.setScene(randomScene);
				scenes.remove(index);
			}
		}
	}

	public void incrementDay(){
		this.dayNum++;
	}

	public int getDayNum(){
		return this.dayNum;
	}

	public int getDayTotal(){
		return this.dayTotal;
	}

	public void displayInfo(){
		System.out.println("Current Day is: "+this.dayNum);
		System.out.println("Number of rooms left to complete is: "+(10-this.finRooms));
	}

	public void takeTurn(){
		Scanner turnScanner = new Scanner(System.in);
		Player current = players.remove();
		System.out.println();
		System.out.println("Player "+current.getid()+"! It's now your turn!");
		System.out.println("What would you like to do?");
		while(current.hasTurn()){
			turnScanner = new Scanner(System.in);
			String[] commandargs = null;
			String command = "";
			System.out.print("> ");
			command = turnScanner.nextLine();
			commandargs = command.split(" ");
			if(commandargs.length > 0){
				command = commandargs[0];
			}

			switch(command){
			case "exit":
				current.setTurnStatus(false);
				this.dayNum = this.dayTotal+1;
				break;
			case "menu":
				System.out.println("-------------------------Command Options-------------------------------");
				System.out.println("who ----------------------Player Information");
				System.out.println("where --------------------Current Room Information");
				System.out.println("move [room] --------------Move to specified room");
				System.out.println("work [Star/Extra] [#]-----Specify which role to work (list rooms first)");
				System.out.println("upgrade [$/cr] [rank]-----Upgrade command");
				System.out.println("rehearse -----------------Reharse command");
				System.out.println("act ----------------------Act command");
				System.out.println("list [rooms/roles] -------Lists adjacent rooms or roles in room");
				System.out.println("end ----------------------Ends player turn");
				System.out.println("exit ---------------------Immediately ends game");
				System.out.println("menu ---------------------Displays menu options");
				System.out.println("info ---------------------Displays board information");
				System.out.println("-----------------------------------------------------------------------");
				break;
			case "who":
				current.displayInfo();
				break;
			case "info":
				displayInfo();
				break;
			case "where":
				current.getCurrentLocation().displayInfo();
				break;
			case "end":
				current.setTurnStatus(false);
				break;
			case "move":
				if(commandargs.length == 2){
					String location = commandargs[1];
					if(!current.hasMoved()){
							if(current.isWorking()){
								System.out.println("You cannot move, you are current working on a role");
							} else{
								current.move(getRoom(location));
							}
					} else {
						System.out.println("You have already moved");
					}
					break;
				} else {
					System.out.println("Wrong arguments");
					break;
				}
			case "list":
				if(commandargs.length == 2){
					String subcommand = commandargs[1];
					if(subcommand.equals("rooms")){
						current.getCurrentLocation().displayAdjRooms();
						break;
					}
					if(subcommand.equals("roles")){
						current.getCurrentLocation().displayRoles();
						break;
					}
				} else {
					System.out.println("Wrong arguments");
					break;
				}
			case "work":
				if(commandargs.length == 3){
					if(!current.isWorking()){
						String location = current.getCurrentLocation().getRoomName();
						if(!location.equals("Trailers") && !location.equals("CastingOffice")){
							int roleNum = Integer.parseInt(commandargs[2]);
							current.takeRole(commandargs[1], roleNum);
							break;
						} else {
							System.out.println("You are not in the proper location to work");
							break;
						}
					}else{
						System.out.println("You are already working on a role");
						break;
					}
				}
			case "upgrade":
				if(commandargs.length == 3){
					int requestedRank = Integer.parseInt(commandargs[2]);
					current.upgrade(commandargs[1], requestedRank);
					break;
				} else {
					System.out.println("Wrong arguments");
					break;
				}
			case "rehearse":
				current.rehearse();
				break;
			case "act":
				current.act();
				String roomName = current.getCurrentLocation().getRoomName();
				if(!roomName.equals("Trailers") && !roomName.equals("CastingOffice")){
				if(current.getCurrentLocation().getShotNum()==0 && current.getCurrentLocation().isActive()){
					System.out.println("This room has fullfilled all of its shots and is now closing.");
					current.getCurrentLocation().wrapUpRoom();
					this.finRooms++;
				}
				}
				break;
			default:
				System.out.println("Invalid Command");
			}
		}
		current.setTurnStatus(true);
		current.setMoveStatus(false);
		players.add(current);
	}
}
