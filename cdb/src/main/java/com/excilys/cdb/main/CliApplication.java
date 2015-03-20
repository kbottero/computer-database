package com.excilys.cdb.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.excilys.cdb.ui.cli.CliCommands;

public class CliApplication {
	public static void main(String args[]){
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
	}

}
