package com.github.excaliburHisSheath;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class NameGenerator2D {

	private static int[][] probabilities;
	private static final String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", " " };
	public static final String source = "tieflingFemale.txt", output = "result.txt";

	public NameGenerator2D() {
		probabilities = new int[27][27];
	}

	public void generateProbablititesBasedOnInput(String source) {
		String[] sourceNames = readNamesFromInput(source);
		for (String name : sourceNames) {
			addStringToProbability(name);
		}
	}

	private void addStringToProbability(String name) {
		char last = ' ';
		int index = 0;
		while (index < name.length()) {
			char current = name.charAt(index);
			probabilities[charIndex(last)][charIndex(current)]++;
			last = current;
			index++;
		}
		char current = ' ';
		probabilities[charIndex(last)][charIndex(current)]++;
	}
	
	public String generateName() {
		String result = "";
		char last = ' ';
		do {
			last = nextCharByLast(last);
			result += Character.toString(last);
		} while (last != ' ');
		return result;
	}

	private static char nextCharByLast(char last) {
		int total = 0;
		for (int i : probabilities[charIndex(last)]) {
			total += i;
		}
		total = (new Random()).nextInt(total);
		int index = 0, subTotal = 0;
		do {
			subTotal += probabilities[charIndex(last)][index++];
		} while (subTotal <= total);
		return (chars[--index]).charAt(0);
	}

	@SuppressWarnings({ "unused", "resource" })
	private static String[] readNamesFromInput(String source) {
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader(source)));
			LinkedList<String> list = new LinkedList<String>();
			while (s.hasNext())
				list.add(s.next());
			return list.toArray(new String[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (s != null)
			s.close();
		return null;
	}
	
	private static int charIndex(char c) {
		for (int i = 0; i < chars.length; i++) {
			if (Character.toString(c).equalsIgnoreCase(chars[i])) {
				return i;
			}
		}
		return (Integer) null;
	}
	
	@SuppressWarnings("unused")
	private static boolean contains(ArrayList<String> stuff, String value) {
		for (String s : stuff) {
			if (s.equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

}
