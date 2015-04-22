package com.excilys.cdb.cli.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.cli.CliCommands;

public class CliApplication {
	public static void main(String args[]){
		
		Logger logger = LoggerFactory.getLogger(CliApplication.class);
		logger.info("Beginning of the CLI application");
		String rep = new String();
		do{
			System.out.print(CliCommands.prompt);
			try{
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    rep = bufferRead.readLine();
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
			Scanner s = new Scanner(rep);
			CliCommands.getCommands(s).execute(s);
		} while (!rep.equals(CliCommands.QUIT.getLabel()));
		logger.info("End of the CLI application");
	}

}
