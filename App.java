package bot.HT.HT_Backup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.*;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.lang.ProcessBuilder;

public class App extends ListenerAdapter
{
	
	static JDA jda;
	static Cloud cloud = new Cloud();
	String vegasWGET;
	boolean mode = true;
	
    public static void main( String[] args ) throws Exception
    {
    	
    	jda = new JDABuilder(AccountType.BOT).setToken(Ref.TOKEN).buildBlocking();
        //jda = new JDABuilder(AccountType.BOT).setToken(Ref.DEVTOKEN).buildBlocking();
        
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        //
        jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Dusting shelves..."));
        jda.addEventListener(new App());
        
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Update: " + Ref.version);
        //eb.setDescription("Here's what's new:");
        eb.setDescription("Updated admin list.");
        eb.setColor(Ref.HTRed);
       // eb.addField("Renamed Command >players to >submitted","This command shows who has submitted a bot. >players was the original command, but now it's >submitted, to clear up any confusions.",false);
        

        
//        eb.addField(">time","Use this command to get the UTC time. Our schedule will be based on this time.",false);
//        eb.addField(">schedule","Returns the schedule.",false);
//        eb.addField(">who [ID]","Returns the ID of a person.",false);
//        eb.addField(">id [@PERSON]","Returns the @PERSON that matches this ID. You are able to tag multiple people for this command!",false);
//        eb.addField(">define [WORD]","This function was recently added to HT Backup, but the API being used is not functional anymore. Looking for an alternative, but it'll have to wait ;)",false);
//        eb.addField("Embedded Messages","As you can see, we're now making use of embedded messages. Hopefully this will make our messages EXTRA fancy and make it easy to look at.",false);
        
        //jda.getTextChannelById(Ref.updateChId).sendMessage(eb.build()).queue();
        //jda.getTextChannelById(Ref.devChId).sendMessage(eb.build()).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
    	
    	//Objects
    	User objUser = evt.getAuthor();
    	MessageChannel objMsgCh= evt.getChannel();
    	Message objMsg = evt.getMessage();
    	Guild objGuild = evt.getGuild();
    	
    	if(mode) {
    		if(verify(false, true, objMsg,objMsgCh,objUser) && objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "toggleMode")) {
    			mode = false;
    			objMsgCh.sendMessage("Commands disabled. Bots will no longer be backed up.").queue();
    		}
    	}else {
    		if(verify(false, true, objMsg,objMsgCh,objUser) && objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "toggleMode")) {
    			mode = true;
    			objMsgCh.sendMessage("Commands enabled.").queue();
    		}else if(!verify(false, true, objMsg,objMsgCh,objUser)){
    			return;
    		}
    	}
    	
	    if(objMsg.getContentRaw().startsWith(Ref.prefix+"status")) {
	    	String input = objMsg.getContentRaw();
	    	String botName = input.substring(7);
	    	String currentDate = Ref.getTime();
	    	botName = botName.trim();
    		if(botName.equalsIgnoreCase("HTBot")) {
    			try {
    				Member HTBot = getMemberById(objGuild,"" + Ref.HTBotId);
	    			objMsgCh.sendMessage(objUser.getAsMention() + " `HTBOT STATUS: " + HTBot.getOnlineStatus() + "`").queue();
    			}catch(Exception e) {
    				Member HTBot = getMemberByName(objGuild,"HTBot");
    				objMsgCh.sendMessage(objUser.getAsMention() + " `HTBOT STATUS: " + HTBot.getOnlineStatus() + "`").queue();
    			}
    		}else if(botName.equalsIgnoreCase("Vegas")) {
    			try {
    				Member vegas = getMemberById(objGuild,"" + Ref.vegasId);
    				objMsgCh.sendMessage(objUser.getAsMention() + " `VEGAS STATUS: " + vegas.getOnlineStatus() + "`").queue();
    			}catch(Exception e) {
    				Member vegas = getMemberByName(objGuild,"Vegas");
    				objMsgCh.sendMessage(objUser.getAsMention() + " `VEGAS STATUS: " + vegas.getOnlineStatus() + "`").queue();
    			}
    		}else if(botName.equalsIgnoreCase("HT-Backup") || botName.equalsIgnoreCase("")) {
    			objMsgCh.sendMessage(objUser.getAsMention() + " `" + Ref.version + " STATUS: ONLINE [" + currentDate + "]`").queue();
    		}else{
    			objMsgCh.sendMessage("`Invalid bot. Try \">status HTBot\" or \">status Vegas\" or \">status HT-Backup\"`").queue();
    		}
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "playing")){
    		if(verify(false, true,objMsg,objMsgCh,objUser)) {
    			jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, objMsg.getContentRaw().substring(9)));
    		}
    	}	
    	else if(objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix +"invite")) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("Halite Tournaments Official Invite Link");
    		eb.setColor(Ref.HTYellow);
    		eb.setImage("https://cdn.discordapp.com/attachments/417732882828886019/446493666333294602/HaliteTournamentsPlanetsLogo.png");
    		eb.setDescription(Ref.inviteLink);
    		objMsgCh.sendMessage(eb.build()).queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix +"setInvite")) {
    		String input = objMsg.getContentRaw();
    		String link = input.substring(6);
    		Ref.inviteLink = link;
    		objMsgCh.sendMessage("inviteLink set to " + Ref.inviteLink).queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix +"disco")) {
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("♪♪ ヽ(ˇ∀ˇ )ゞ");
    		eb.setColor(Ref.HTYellow);
    		objMsgCh.sendMessage(eb.build()).queue(message -> {
    			for(int i = 0; i < 20; i++) {
    				if(i % 3 == 0) {
    					eb.setColor(Ref.HTGreen);
    				}else if(i % 3 == 1) {
    					eb.setColor(Ref.HTRed);
    				}else {
    					eb.setColor(Ref.HTYellow);
    				}
    				try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
    				message.editMessage(eb.build()).queue();
    			}
    			message.delete().queue();
    		});
    	}
	    
	    else if(objMsg.getContentRaw().startsWith(Ref.prefix+"time")) {
    		objMsgCh.sendMessage(Ref.getTime()).queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "schedule")){
    		EmbedBuilder eb = new EmbedBuilder(); 
            eb.setColor(Ref.HTRed);
    		eb.setTitle("Here is the schedule for Season III. For a graphic version, go to #season-iii");
    		eb.addField("Valid as of May 16, 2018.", "Today's Date: " + Ref.getTime(), false);
    		eb.addField("May 16","Submissions open for casual battles.",false);
    		eb.addField("June 16","New Season III Engine is released.",false);
    		eb.addField("June 20","Sign-ups close.",false);
    		eb.addField("June 30","First day of Season III Tournament",false);
    		eb.addField("July 1, 4:00 PM UTC","Second day of Season III Tournament",false);
    		eb.addField("July 2, 4:00 PM UTC","Third day of Season III Tournament",false);
    		eb.addField("July 3, 4:00 PM UTC","Results and awards released for Season III.",false);
    		objMsgCh.sendMessage(eb.build()).queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "help")){
    		objMsgCh.sendMessage("```Here are the functions you can use for HT-Backup:"
    				+ "\nNOTE: SQUARE BRACKETS, OR [] MEANS PARAMETER. DO NOT INCLUDE THEM IN ACTUAL COMMAND.\n"
    				+ "\n!submit: Submit your bot and HT-Backup will back up your file for you. ONLY usable in the channels #bots and #battles."
    				+ "\n>schedule: Get schedule for Season III."
    				+ "\n>submitted: Get all the players who have submitted."
    				+ "\n>weather [CITY_NAME]: Check the weather in a certain city. Don't add the brackets in the command."
    				+ "\n>retrieve: Use this command to retrieve your bot. This is only usable in a Direct Message/Private Channel with the bot."
    				+ "\n>status [BOT_NAME]: Returns a bot's online status."
    				+ "\n>status: Return's HT Backup's status."
    				+ "\n>define [WORD]: Returns the definition of a word."
    				+ "\n>time: Returns time in UTC."
    				+ "\n>who [ID]: Returns who this ID is."
    				+ "\n>id [@PERSON]: Return @PERSON's id. Can tag many people to get their id's. NOTE: You can tag yourself to get your id."
    				+ "\n>admin: Shows admin commands. Only admins can use this command.```").queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "admin") && Ref.adminIds.contains(objUser.getIdLong())) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
    			objMsgCh.sendMessage("```ADMIN FUNCTIONS:"
	    				+ "\nNOTE: SQUARE BRACKETS, OR [] MEANS PARAMETER. DO NOT INCLUDE THEM IN ACTUAL COMMAND."
	    				+ "\n\nFILE ACCESS:"
	    				+ "\n>show all: Shows all player that have submitted. NOTE: The slot name is only the player's id. The name is only to help identify who the player is."
	    				+ "\n>show [PLAYER_ID]: Returns the filename in a player's slot."
	    				+ "\n>retrieve [PLAYER_ID]: Returns file in a player's slot."
	    				+ "\n>remove [PLAYER_ID]: Removes a player's slot."
	    				+ "\n>empty [PLAYER_ID]: Removes all files in a player's slot."
	    				+ "\n>store [PLAYER_ID]: Stores a file in player's slot. Must have file attached to message."
	    				+ "\n>restore: Returns all backed up bots."
	    				+ "\n>getVegasFile: Returns " + Ref.vegasFile + "."
	    				+ "\n>setVegasFile [" + Ref.vegasFile.toUpperCase() + "]: Replaces the " + Ref.vegasFile + "."
	    				+ "\n\nBOT CONTROL:"
	    				+ "\n>init 0 [DOWNTIME]: Shuts down HT Backup. Sends a message informing users HT Backup will be down for DOWNTIME minutes."
	    				+ "\n>init 0: Shuts down HT Backup. Sends a message informing users HT Backup will be down for 30 minutes."
	    				+ "\n>greeting: Sends message \"I'm the bot that will backup all your bots that you submit onto this server so you don't lose them. I'll be silent most of the time, but if you want to know the weather, just type >weather <LOCATION>. May the best bot win!\""
	    				+ "\n>echo [STRING]: Sends STRING back in the same channel and deletes user's message."
	    				+ "\n\nCHANNEL CONTROL:"
	    				+ "\n>submitChannel [NEW_CHANNEL_ID]: Sets primary submission channel id."
	    				+ "\n>battleChannel [NEW_CHANNEL_ID]: Sets secondary submission channel id."
	    				+ "\n>privateChannel [NEW_CHANNEL_ID]: Sets private channel id. Ex. #halite is a private channel. NOTE: Any channel named 'halite' will be automatically private."
	    				+ "\n>getIds: Returns all the guild and channel IDs currently.```").queue();
    		}
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "who")) {
    		String input = objMsg.getContentRaw();
    		String id = input.substring(5).trim();
    		Long idLong = Long.parseLong(id);
    		String name = jda.getUserById(idLong).getAsMention();
    		
    		objMsgCh.sendMessage(id + " is " + name).queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "battleChannel ") && Ref.adminIds.contains(objUser.getIdLong())) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
    			String input = objMsg.getContentRaw();
	    		String id = input.substring(15).trim();
	    		Long idLong = Long.parseLong(id);
	    		Ref.battlesChId = idLong;
	    		objMsgCh.sendMessage("battlesChId set to " + id).queue();
    		}
    		
    		
    	}else if(objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "getIds") && Ref.adminIds.contains(objUser.getIdLong())) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
    			String output = "";
    			System.out.println(Ref.battlesChId);
    			output += "battleChId = " + Ref.battlesChId + ", name of channel is " + jda.getTextChannelById(Ref.battlesChId).getName() + "\n";
    			output += "submitChId = " + Ref.submitChId + ", name of channel is " + jda.getTextChannelById(Ref.submitChId).getName() + "\n";
    			output += "privateChId = " + Ref.submitChId + ", name of channel is " + jda.getTextChannelById(Ref.privateChId).getName() + "\n";
    			
    			output += "privateChannels= {";
    			for(Long l : Ref.privateChannels) {
    				output += "name=" + jda.getTextChannelById(l).getName() + ":id=" + l + "\n";
    			}
    			output += "}\n";
    			
    			objMsgCh.sendMessage(output).queue();
    		}
    		
    		
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "submitChannel ") && Ref.adminIds.contains(objUser.getIdLong())) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
	    		String input = objMsg.getContentRaw();
	    		String id = input.substring(14).trim();
	    		Long idLong = Long.parseLong(id);
	    		Ref.submitChId = idLong;
	    		
	    		objMsgCh.sendMessage("submitChIdset set to " + id).queue();
    		}
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "privateChannel ") && Ref.adminIds.contains(objUser.getIdLong())) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
	    		String input = objMsg.getContentRaw();
	    		String id = input.substring(15).trim();
	    		Long idLong = Long.parseLong(id);
	    		Ref.privateChId = idLong;
	    		
	    		objMsgCh.sendMessage("Set privateChId to " + id).queue();
    		}
    	}
    	else if(objMsg.getContentRaw().startsWith(Ref.prefix + "id")) {
    		List<Member> mentioned = objMsg.getMentionedMembers();
    		String output = "";
    		for(Member m : mentioned) {
    			output += m.getUser().getName() + "'s id is " + m.getUser().getId() + "\n"; 
    		}
    		objMsgCh.sendMessage(output).queue();
    	}
    	else if(objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "getVegasFile")) {
    		if(Ref.adminIds.contains(objUser.getIdLong())) {
    			File file = new File("../Vegas/" + Ref.vegasFile);
    			objMsgCh.sendFile(file).queue();
    		}
    	}else if(objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "setVegasFile")) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
	    		if(Ref.adminIds.contains(objUser.getIdLong())) {
	    			vegasWGET = objMsg.getAttachments().get(0).getUrl();
	    			objMsgCh.sendMessage(objUser.getAsMention() + " `WARNING: SETTING THIS FILE WILL OVERWRITE PREVIOUS FILE. TO CONFIRM OVERWRITE, TYPE >confirmSetVegasFile`").queue();
	    		}
    		}
    	}else if(objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "submitted")) {
    		objMsgCh.sendMessage("Getting players...").queue(message ->{
    			message.editMessage(cloud.showAll()).queue();
    		});
    	}
    	else if(objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "confirmSetVegasFile")) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
    			if(Ref.adminIds.contains(objUser.getIdLong())) {
        			if(!vegasWGET.equals("")) {
        				objMsgCh.sendMessage(objUser.getAsMention() + " `Updating " + Ref.vegasFile + "`").queue(message -> {
        					executeCommand("rm ../Vegas/" + Ref.vegasFile);
        					executeCommand("wget " + vegasWGET + " -P ../Vegas/");
        					message.editMessage("`TIMESTAMP: " + Ref.getTime() + "` Update complete. Logging action...").queue();
        					jda.getTextChannelById(Ref.logChId).sendMessage(Ref.getTime() + ", " + objUser.getAsMention() + " updated " + Ref.vegasFile).queue();
        					message.editMessage("Action logged. Message expires in 5 seconds.").queue();
        					vegasWGET = "";
        					try {
    							Thread.sleep(5000);
    						} catch (InterruptedException e) {}
        					message.delete().queue();
        				});
        			}
        		}
    		}
    	}
    	else if(objMsg.getContentRaw().startsWith(Ref.prefix + "init 0")) {
    		long timeInMin = 0;
    		try {
    			timeInMin = Long.parseLong(objMsg.getContentRaw().substring(7).trim());
    		}catch (Exception e) {
    			timeInMin = 30;
    		}
    		String currentDate = Ref.getTime(objMsg.getContentRaw().substring(7).trim());
    		if(Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
    			objMsgCh.sendMessage("HT-Backup is going offline for "+timeInMin+" minutes. It will be back online at " + currentDate).queue();
    			objMsg.delete().queue();
	    		jda.shutdown();
    		}
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix +"echo") && Ref.adminIds.contains(objUser.getIdLong())){
    		String input = objMsg.getContentRaw();
    		String echo = input.substring(6);
    		objMsgCh.sendMessage(echo).queue();
    		objMsg.delete().queue();
    	}else if(objMsg.getContentRaw().startsWith("!submit")) {
    		if(objMsgCh.getIdLong() == Ref.battlesChId || objMsgCh.getIdLong() == Ref.submitChId) {
	    		Member HTBot = getMemberById(objGuild,"" + Ref.HTBotId);
	    		if(HTBot.getOnlineStatus() == OnlineStatus.ONLINE) {
	    			objMsgCh.sendMessage("Backing up your bot...").queue(message ->{
	    				MessageChannel logCh = jda.getTextChannelById(Ref.logChId);
	    				jda.getPresence().setStatus(OnlineStatus.ONLINE);
			    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Organising files..."));
			    		String cloudMsg = cloud.store(objMsg);
			    		
			    		logCh.sendMessage("```User " + objUser.getName() + " submitted a bot called "  + objMsg.getAttachments().get(0).getFileName() + " in channel " + objMsgCh.getName() + " at " + Ref.getTime() + ""
			    				+ "\nHere is the output: " + cloudMsg + ""
			    						+ "You can retrieve this file using >retrieve " + objUser.getId() + " or see the file in the folder using >show " + objUser.getId() + "```").queue();

			    		jda.getPresence().setStatus(OnlineStatus.IDLE);
			    		
			    		String currentDate = Ref.getTime();
			    		if(cloudMsg.equals("File was not stored succesfully.")) {
			    			message.editMessage(objUser.getAsMention() + " `Your bot was not succesfully backed up. Please try again. Message will expire in 5 seconds.`").queue();
			    		}else {
			    			message.editMessage("`Your bot has been backed up at " + currentDate + ". Message will expire in 5 seconds.`").queue();
			    		}
			    			
			    		
			    		double sentTime = System.currentTimeMillis();
			    		double currTime = 0;
			    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
			    		message.delete().queue();
			    		
	    			});
	    		}else {
	    			String currentDate = Ref.getTime();
	    			MessageChannel logCh = jda.getTextChannelById(Ref.logChId);
	    			String link = objMsg.getAttachments().get(0).getUrl();
	    			logCh.sendMessage("```User " + objUser.getName() + " submitted a bot while HTBot is offline. The file was called "  + objMsg.getAttachments().get(0).getFileName() + " in channel " + objMsgCh.getName() + " at " + currentDate + ""
	    					+ "\nFile: " + link + ""
	    					+ "\nID: " + objUser.getId()).queue();
	    			objMsg.delete().queue();
	    			objMsgCh.sendMessage(objUser.getAsMention() + " `HTBot is currently offline. Please try again later. Message expires in 5 seconds.`").queue(message -> {
	    				double sentTime = System.currentTimeMillis();
			    		double currTime = 0;
			    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
			    		message.delete().queue();
	    			});
	    			
	    		}
    		}else {
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this command in #battles . Message expires in 10 seconds`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 10000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "store") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
    		String id = objMsg.getContentRaw().substring(7);
    		objMsgCh.sendMessage(cloud.store(objMsg,id)).queue();
    	}
    	else if(objMsg.getContentRaw().startsWith(Ref.prefix + "empty") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
    		objMsgCh.sendMessage(cloud.empty(objMsg)).queue();
    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Recycling paper..."));
    		
    		double sentTime = System.currentTimeMillis();
    		double currTime = 0;
    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Enjoying a snack ..."));
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "remove") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())) {
    		objMsgCh.sendMessage(cloud.remove(objMsg)).queue();
    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Throwing away garbage..."));
    		
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "show") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())) {
    		objMsgCh.sendMessage(cloud.show(objMsg)).queue();
    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Dusting shelves..."));
    		
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "retrieve")) {
    		if(Ref.adminIds.contains(objMsg.getAuthor().getIdLong()) || objMsg.isFromType(ChannelType.PRIVATE)){
    			if(objMsg.isFromType(ChannelType.PRIVATE)) {
    				objMsgCh.sendMessage(cloud.userVerification(objMsg)).queue();
    				if(cloud.userVerification(objMsg).equals("Verify complete. Retrieiving file...")) {
    					objMsgCh.sendFile(cloud.userRetrieve(objMsg)).queue(message -> {
    						
    						String currentDate = Ref.getTime();
    						
    						MessageChannel logCh = jda.getTextChannelById(Ref.logChId);
    	    				logCh.sendMessage("`User " + objUser.getName() + " retrieved bot \"" + message.getAttachments().get(0).getFileName() + "\" at " + currentDate + "`").queue();
    					});
    				}
    				
    			}else {
    				objMsgCh.sendMessage(cloud.verifyRetrieve(objMsg)).queue();
    				objMsgCh.sendFile(cloud.retrieve(objMsg)).queue();
    				jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Putting folder back..."));
    			}
    		}else if(!objMsg.isFromType(ChannelType.PRIVATE) && !Ref.adminIds.contains(objMsg.getAuthor().getIdLong())) {
    			objMsgCh.sendMessage("Please use this command in a private message. Sending you a private message for instructions now. Message expires in 10 seconds").queue(message ->{
    				objUser.openPrivateChannel().queue((channel) ->{
			    		channel.sendMessage("To retrieve your bot that has been backed up, simply reply to me by typing >retrieve").queue();
	    			});
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 10000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
		    		objMsg.delete().queue();
		    		
    			});
    		}
    	}else if(objMsg.getContentRaw().equals(Ref.prefix + "restore") && Ref.adminIds.contains(objUser.getIdLong())) {
    		cloud.deleteZip();
    		objMsgCh.sendFile(cloud.sendZip()).queue(message ->{
    			message.editMessage("`Files sent and zip has been cleared in directory.`").queue();
    		});
    		
    	}
    	else if(objMsg.getContentRaw().equals(Ref.prefix + "greeting") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
    		String mention = objGuild.getPublicRole().getAsMention();
    		objMsgCh.sendMessage(mention.substring(1)).queue();
    		objMsgCh.sendMessage("Hello "+mention.substring(0, mention.length())+"! I'm the bot that will backup all your bots that you submit onto this server so you don't lose them. I'll be silent most of the time, but if you want to know the weather, just type >weather <LOCATION>. May the best bot win!").queue();
    		objMsg.delete().queue();
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "weather")) {	
    		objMsgCh.sendMessage("Getting weather...").queue(message ->{
    			weatherAPI weather = new weatherAPI();
	    		String input = objMsg.getContentRaw();
	    		String city = "";
	    		String output = "";
	    		int reportNum = 0;
    			if(input.indexOf(",") == -1 && input.indexOf("#") == -1) {
	    			city = input.substring(9,input.length());
	    			try {
						output = weather.getWeather(city, "", reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}else if(input.indexOf(",") != -1 && input.indexOf("#") == -1){
	    			city = input.substring(9,input.indexOf(","));
	    			String country = input.substring(input.indexOf(",") + 1,input.length());
	    			try {
						output = weather.getWeather(city, country, reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}else if(input.indexOf(",") != -1 && input.indexOf("#") != -1){
	    			city = input.substring(9,input.indexOf(","));
	    			String country = input.substring(input.indexOf(",") + 1,input.indexOf("#"));
	    			reportNum = Integer.parseInt(input.substring(input.indexOf("#") + 1,input.length()));
	    			
	    			try {
						output = weather.getWeather(city, country, reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}else if(input.indexOf(",") == -1 && input.indexOf("#") != -1) {
	    			city = input.substring(9,input.indexOf("#"));
	    			reportNum = Integer.parseInt(input.substring(input.indexOf("#") + 1,input.length()));
	    			try {
						output = weather.getWeather(city, "", reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}
	    		objMsgCh.sendMessage(output).queue();
	    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Looking outside..."));
	    		message.delete().queue();
    		});
    		
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "define")) {
    		
    		objMsgCh.sendMessage("Getting definition...").queue(message ->{
    			String input = objMsg.getContentRaw();
	    		String  word = "";
	    		word = input.substring(8);
	    		try {
					objMsgCh.sendMessage(Dictionary.define(word)).queue();
				} catch (Exception e) {
					e.printStackTrace(System.out);
					objMsgCh.sendMessage("" + e).queue();
				}
	    		message.delete().queue();
    		});
    		
    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "urban")) {
    		if(!verify(true,true,objMsg,objMsgCh,objUser)) {
    			objMsg.delete().queue();
    			objMsgCh.sendMessage(objUser.getAsMention() + " `Please use this in a private channel. Message expires in 5 seconds.`").queue(message ->{
    				double sentTime = System.currentTimeMillis();
		    		double currTime = 0;
		    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
		    		message.delete().queue();
    			});
    		}else {
    			objMsgCh.sendMessage(urbanDict(objMsg.getContentRaw())).queue();
    		}
    		
    	}
    }
    
    public static String executeCommand(String command) {
    	//Build command 
    	Process process = null;
		try {
			process = new ProcessBuilder(new String[] {"bash", "-c", command})
			    .redirectErrorStream(true)
			    .directory(new File("."))
			    .start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //Read output
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, previous = null;
        
        String output = "";
        try {
			while ((line = br.readLine()) != null)
			    if (!line.equals(previous)) {
			        previous = line;
			        out.append(line).append('\n');
			        output += line + "\n";
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return output;
    }
    
    public static String cloudExec(String command) {
    	//Build command 
    	Process process = null;
		try {
			process = new ProcessBuilder(new String[] {"bash", "-c", command})
			    .redirectErrorStream(true)
			    .directory(new File("../"))
			    .start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //Read output
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, previous = null;
        
        String output = "";
        try {
			while ((line = br.readLine()) != null)
			    if (!line.equals(previous)) {
			        previous = line;
			        out.append(line).append('\n');
			        output += line + "\n";
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return output;
    }
    
    public Member getMemberByName(Guild guild,String name) {
    	List<Member> members = guild.getMembers();
		int memberIndex = -1;
		for(Member member : members) {
			if(member.getEffectiveName().equals(name)) {
				memberIndex = members.indexOf(member);
			}
		}
		if(memberIndex == -1) {
			return null;
		}else {
			return members.get(memberIndex);
		}
    }
    
    public Member getMemberById(Guild guild,String id) {
    	List<Member> members = guild.getMembers();
		int memberIndex = -1;
		for(Member member : members) {
			if(member.getUser().getId().equals(id)) {
				memberIndex = members.indexOf(member);
			}
		}
		if(memberIndex == -1) {
			return null;
		}else {
			return members.get(memberIndex);
		}
    }
    
    public String getFlag(String command, String input) {
    	String cmdWithPrefix = Ref.prefix + command;
    	String flag = input.substring(cmdWithPrefix.length(), input.length());
    	flag = flag.trim();
		System.out.println(flag + " flagged for command \"" + command + "\"");
		return flag;
    }
    
    public String urbanDict(String input) {
		String  word = "";
		int defNum = 0;
		if(input.indexOf("#") != -1) {
			 word = input.substring(7,input.indexOf("#"));
			 defNum = Integer.parseInt(input.substring(input.indexOf("#") + 1));
		}else {
			try {
				word = input.substring(7);
			}catch(Exception e) {
				return ("Do you think this command is magical?! I can't read your mind! Add an input.");
			}
		}
		word = word.replace(" ", "+");
		try {
			return (Dictionary.getUrban(word,defNum));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ("" + e);
		}
    }
    
    public boolean verify(boolean priv, boolean admin, Message objMsg, MessageChannel objMsgCh, User objUser) {
    	if(priv) {
    		if(!(Ref.privateChannels.contains(objMsgCh.getIdLong()) || Ref.privateGuilds.contains(objMsg.getGuild().getIdLong()) || objMsgCh.getName().equals("halite"))) {
    			return false;
    		}
    	}
    	if(admin) {
    		if(!Ref.adminIds.contains(objUser.getIdLong())) {
    			return false;
    		}
    	}
    	return true;
    	
    }
}
