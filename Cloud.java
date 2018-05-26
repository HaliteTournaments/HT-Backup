package bot.HT.HT_Backup;

import java.io.File;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Cloud {

	public Cloud() {
		//Rename old storage
		if(App.cloudExec("ls").contains(Ref.previousStorageName)) {
			App.cloudExec("mv " + Ref.previousStorageName + " " + Ref.storageName);
		}
	}
	
	public String help() {
		return
		"\n CLOUD FUNCTIONS:"
		+ "\n ***>store*** <SLOT_NUMBER>: Stores a file at the specified slot number. Leave empty to fill the next empty slot."
		+ "\n ***>retrieve*** <SLOT_NUMBER>: Get the file inside the specified slot."
		+ "\n ***>remove*** <SLOT_NUMBER>: Remove the specified slot."
		+ "\n ***>show*** <SLOT_NUMBER>: Show the contents of the specified slot."
		+ "\n ***>show all***: Show the slots that are created.";
	}
	
	public String store(Message objMsg) {
		String input = objMsg.getContentRaw();
		String storeNum = objMsg.getAuthor().getId();
		
		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
		}
		
		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
			App.cloudExec("mkdir "+Ref.storageName+"/" + storeNum);
		}
		
		if(!App.cloudExec("ls "+Ref.storageName+"/" + storeNum).equals("")) {
			App.cloudExec("rm -rf "+Ref.storageName+"/"+storeNum+"/*");
			System.out.println("rm -rf "+Ref.storageName+"/"+storeNum+"/*");
		}
		
		if(objMsg.getAttachments().size() < 2) {
			String fileLink = objMsg.getAttachments().get(0).getUrl();
			String output = App.cloudExec("wget -q -P "+Ref.storageName+"/" + storeNum + " " + fileLink);
			
			if(App.cloudExec("ls " + storeNum).equals("")) {
				return "File was not stored succesfully.";
			}else {
				return "File stored succesfully in slot "+ storeNum + ".";
			}				
		}else {
			return "No file attached!";
		}
		
	}
	
	public String store(Message objMsg,String slot) {
		String input = objMsg.getContentRaw();
		String storeNum = slot;
		
		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
		}
		
		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
			App.cloudExec("mkdir "+Ref.storageName+"/" + storeNum);
		}
		
		if(!App.cloudExec("ls "+Ref.storageName+"/" + storeNum).equals("")) {
			App.cloudExec("rm -rf "+Ref.storageName+"/"+storeNum+"/*");
			System.out.println("rm -rf "+Ref.storageName+"/"+storeNum+"/*");
		}
		
		if(objMsg.getAttachments().size() < 2) {
			String fileLink = objMsg.getAttachments().get(0).getUrl();
			String output = App.cloudExec("wget -q -P "+Ref.storageName+"/" + storeNum + " " + fileLink);
			
			if(App.cloudExec("ls " + storeNum).equals("")) {
				return "File was not stored succesfully.";
			}else {
				return "File stored succesfully in slot "+ storeNum + ".";
			}
			
			
			
			
				
		}else {
			return "No file attached!";
		}
		
	}
	
	public String empty(Message objMsg) {
		String input = objMsg.getContentRaw();
		String storeNum = input.substring(6,input.length());
		storeNum = storeNum.trim();
		
		//Verify that storage is a directory
		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
		}
		//Verify that storage contains this slot.
		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
			return ("Storage " + storeNum + " does not exist.");
		}
		
		//Check if slot is empty or not.
		if(App.cloudExec("ls "+Ref.storageName+"/" + storeNum).equals("")) {
			return ("Storage " + storeNum + " is already emtpy.");
		}else {
			if(objMsg.getAttachments().size() < 2) {
				String fileInSlot = App.cloudExec("ls "+Ref.storageName+"/" + storeNum);
				String output = App.cloudExec("rm -f "+Ref.storageName+"/"+storeNum+"/*");
				return ("Storage slot " + storeNum + " succesfully emptied.");
				
			}
		}
		return "Action empty failed.";	
	}
	
	public String remove(Message objMsg){
		String input = objMsg.getContentRaw();
		String storeNum = input.substring(8,input.length());

		//Verify that "storage" is a directory
		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
		}
		
		//Verify that storage contains this slot.
		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
			return ("Storage " + storeNum + " does not exist.");
		}else {
			//Verify that slot is empty.
			if(!App.cloudExec("ls " + Ref.storageName + "/" + storeNum).equals("")) {
				return ("Storage " + storeNum + " is NOT EMPTY! Run >empty " + storeNum + " to empty the slot. After emptying the slot, you can choose to remove the slot.");
			}
			
    		String output = App.cloudExec("rm -r "+Ref.storageName+"/" + storeNum);
    		return ("Slot " + storeNum + " succesfully removed.");
		}
	}
	
	public String show(Message objMsg) {
		String input = objMsg.getContentRaw();
		String storeNum;
		try {
			storeNum = input.substring(6,input.length());
		}catch(Exception e) {
			return "No slot specified. Try >show all.";
		}
		//Verify that "storage" is a directory
		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
		}
		
		if(storeNum.equals("all")) {
			String output = App.cloudExec("ls " + Ref.storageName);
			String[] snowflakes = output.split("\n");
			output = "";
			for(int i = 0; i < snowflakes.length; i++) {
				output += snowflakes[i].trim() + "/" + App.jda.getUserById(Long.parseLong(snowflakes[i].trim())).getName() + "\n";
			}
			
			if(!output.equals("")){
				return (output);
			}else{
				return ("Storage is empty.");
			}
		}else {
    		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
    			return ("Storage slot "+storeNum+" does not exist.");
    		}else {
    			String output = App.cloudExec("ls "+Ref.storageName+"/" + storeNum);
    			if(!output.equals("")){
    				return (output);
    			}else{
    				return "Slot "+storeNum+" does not exist or is empty." 
    						+ "\nRun >show all to show slots that exist.";
    			}	
    			
    			
    		}
		}
	}
	
	public String showAll() {
		//Verify that "storage" is a directory
		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
		}
		
		String output = App.cloudExec("ls " + Ref.storageName);
		String[] snowflakes = output.split("\n");
		output = "";
		for(int i = 0; i < snowflakes.length; i++) {
			if(!App.cloudExec("ls " + snowflakes[i].trim()).equals("")) {
				output += App.jda.getUserById(Long.parseLong(snowflakes[i].trim())).getName() + "\n";
			}
		}
		
		if(!output.equals("")){
			return (output);
		}else{
			return ("There are no players you can battle against, currently.");
		}
	}
	
	public String verifyRetrieve(Message objMsg) {
		String input = objMsg.getContentRaw();
		String storeNum = input.substring(10,input.length());

		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
			return ("Storage space empty.");
		}
		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
			return ("Storage slot"+storeNum+"does not exist.");
		}
		return "Verify complete. Retrieiving file...";
	}
	
	public String userVerification(Message objMsg) {
		String storeNum = objMsg.getAuthor().getId();

		if(!App.cloudExec("ls").contains(Ref.storageName)) {
			App.cloudExec("mkdir " + Ref.storageName);
			return ("Storage space empty.");
		}
		if(!App.cloudExec("ls " + Ref.storageName).contains(storeNum)) {
			return ("Storage slot"+storeNum+"does not exist.");
		}
		
		return "Verify complete. Retrieiving file...";
	}
	
	public File sendZip() {
		String zip = App.cloudExec("zip backup.zip " + Ref.storageName + "/*/*");
		String filename = "../backup.zip";
		filename = filename.trim();
		File file = new File(filename);
		return file;
	}
	
	public void deleteZip() {
		App.cloudExec("rm backup.zip");
	}
	
	public File userRetrieve(Message objMsg) {
		String input = objMsg.getContentRaw();
		String storeNum = objMsg.getAuthor().getId();
		
		String filename = App.cloudExec("ls "+Ref.storageName+"/"+storeNum);
		filename = filename.trim();
		File file = new File("../" + Ref.storageName + "/" + storeNum + "/" + filename);
		return file;
	}
	
	public File retrieve(Message objMsg) {
		String input = objMsg.getContentRaw();
		String storeNum = input.substring(10,input.length());
		
		String filename = App.cloudExec("ls "+Ref.storageName+"/"+storeNum);
		filename = filename.trim();
		File file = new File("../" + Ref.storageName + "/" + storeNum + "/" + filename);
		return file;
	}
}
