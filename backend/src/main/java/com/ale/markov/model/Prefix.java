package com.ale.markov.model;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Prefix {

	private final Queue<String> words;

	private Prefix(Queue<String> words) {

		this.words = new LinkedList<>(words);
	}

	public static Prefix of(Queue<String> words) {

		return new Prefix(words);
	}

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof Prefix)) {
			return false;
		}

		Prefix other = (Prefix) o;
		return Objects.equals(this.words, other.words);
	}

	@Override
	public int hashCode() {

		return Objects.hashCode(words);
	}

	@Override
	public String toString() {

		return words.toString();
	}
}
