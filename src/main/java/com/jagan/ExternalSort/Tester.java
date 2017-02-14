package com.jagan.ExternalSort;

import com.jagan.ExternalSort.utils.Sort;

public class Tester {
	
	public static void main(String[] args){
		try {
			Sort.externalSort("C:/Users/Acer/Documents/workspace/Sandbox/products.txt", "C:/Users/Acer/Documents/workspace/Sandbox/output.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
