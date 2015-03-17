package com.excilys.cdb.cli;

import java.util.ArrayList;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class Page <E> {
	
	private ArrayList<E> elements;
	private int currPage;
	private int pageSize;
	private int size;
	
	public Page(ArrayList<E> elements){
		this.elements = elements;
		currPage = 0;
		pageSize = 20;
		size = elements.size() / pageSize;
		if (elements.size() % size != 0) {
			++size;
		}
	}
	
	public Page(ArrayList<E> elements, int pageSize){
		this.elements = elements;
		currPage = 0;
		if (pageSize < elements.size()) {
			this.pageSize = pageSize;
			size = elements.size() / pageSize;
			if (elements.size() % size != 0) {
				++size;
			}
		} else {
			this.pageSize = elements.size();
			size = 1;
		}
	}
	
	public boolean nextPage() {
		if (currPage != size) {
			for (int i=currPage*pageSize;i<(currPage+1)*pageSize && i < elements.size() ;++i) {
				Object object = elements.get(i);
				if (object instanceof Computer) {
					Computer computer = (Computer) object;
					System.out.print(computer.getId()+"\t");
					System.out.print(computer.getName()+"\t");
					if (computer.getConstructor() != null) {
						System.out.print(computer.getConstructor().getName()+"\t");
					} else {
						System.out.print("null\t");
					}
					System.out.print(computer.getIntroductionDate()+"\t");
					System.out.println(computer.getDiscontinuedDate());
				} else if (object instanceof Company) {
					Company company = (Company) object;
					System.out.print(company.getId()+"\t");
					System.out.println(company.getName());
				}
				
//				for (Field f : elements.get(0).getClass().getFields()) {
//					System.out.println(f.getName());
//					try {
//						System.out.println(f.get(elements.get(i))+"\t");
//					} catch (IllegalArgumentException e) {
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					}
//				}
			}
			System.out.println("Page "+(currPage+1)+ "/"+size);
			++currPage;
			if (currPage == size ) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
