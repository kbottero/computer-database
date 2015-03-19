package com.excilys.cdb.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CliApplication {
	public static void main(String args[]){
		String rep = new String();
		do{
			System.out.print(">");
			try{
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    rep = bufferRead.readLine();
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
			Scanner s = new Scanner(rep);
			CliCommands.getCommands(s).execute(s);
		} while (!rep.equals("quit"));
	}

}
