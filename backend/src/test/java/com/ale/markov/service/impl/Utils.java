package com.ale.markov.service.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Utils {

	public static LinkedList<String> createPrefix(String... words) {

		LinkedList<String> prefixes = Arrays.stream(words).collect(Collectors.toCollection(
			LinkedList::new));
		return prefixes;
	}

}
