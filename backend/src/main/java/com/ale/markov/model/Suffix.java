package com.ale.markov.model;

import java.util.Objects;

public class Suffix {

	private final String word;

	private Suffix(String word) {

		this.word = word;
	}

	public static Suffix of(String word) {

		return new Suffix(word);
	}

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof Suffix)) {
			return false;
		}

		Suffix other = (Suffix) o;

		return Objects.equals(this.word, other.word);
	}

	@Override
	public int hashCode() {

		return Objects.hashCode(word);
	}

	public String getWord() {

		return word;
	}
}
