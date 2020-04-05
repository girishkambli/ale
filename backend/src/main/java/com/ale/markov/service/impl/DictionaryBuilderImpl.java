package com.ale.markov.service.impl;

import com.ale.markov.model.Prefix;
import com.ale.markov.model.Suffix;
import com.ale.markov.service.DictionaryBuilder;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class DictionaryBuilderImpl implements DictionaryBuilder {

	public static final String EMPTY = "EMPTY";
	public static final String DELIMITER = " ";

	@Override
	public Map<Prefix, Multiset<Suffix>> build(String input, int prefix) {

		if (prefix <= 0) {
			throw new IllegalArgumentException("prefix length must be greater than zero.");
		}

		if (Strings.isNullOrEmpty(input)) {
			return ImmutableMap.of();
		}

		input = input.replaceAll("[\\t\\n\\r]+", " ");
		String[] words = input.split(DELIMITER);
		return build(words, prefix);
	}

	private Map<Prefix, Multiset<Suffix>> build(String[] words, int prefixLen) {

		Map<Prefix, Multiset<Suffix>> dictionary = new HashMap<>();

		Queue<String> prefixes = new LinkedList<>();
		IntStream.rangeClosed(1, prefixLen).forEach(i -> prefixes.add(EMPTY));

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			word = sanitize(word);

			Prefix prefix = Prefix.of(prefixes);
			Multiset<Suffix> suffixes = dictionary.get(prefix);
			if (suffixes == null) {
				suffixes = HashMultiset.create();
			}

			Suffix suffix = Suffix.of(word);
			suffixes.add(suffix);
			dictionary.put(prefix, suffixes);

			prefixes.remove();
			prefixes.add(word);
		}

		HashMultiset<Suffix> endSuffix = HashMultiset.create();
		endSuffix.add(Suffix.of(EMPTY));
		dictionary.put(Prefix.of(prefixes), endSuffix);

		return ImmutableMap.copyOf(dictionary);
	}

	private String sanitize(String word) {

		return word.trim();
	}
}
