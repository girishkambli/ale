package com.ale.markov.service.impl;

import static com.ale.markov.service.impl.DictionaryBuilderImpl.EMPTY;

import com.ale.markov.model.Prefix;
import com.ale.markov.model.Suffix;
import com.ale.markov.service.TextGenerator;
import com.google.common.collect.Multiset;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class TextGeneratorImpl implements TextGenerator {

	@Override
	public String generate(Map<Prefix, Multiset<Suffix>> dictionary, int prefixLen, int outputSize) {

		if (dictionary == null || dictionary.size() == 0) {
			return "";
		}

		if (prefixLen <= 0) {
			throw new IllegalArgumentException("prefix length must be greater than zero.");
		}

		if (outputSize < 0) {
			throw new IllegalArgumentException("output size cannot be less than zero.");
		}

		StringBuilder text = new StringBuilder();

		Deque<String> prefixes = new LinkedList<>();
		IntStream.rangeClosed(1, prefixLen).forEach(i -> prefixes.add(EMPTY));

		Multiset<Suffix> suffixes = dictionary.get(Prefix.of(prefixes));
		Suffix suffix = getSuffix(suffixes);
		while (!suffix.equals(Suffix.of(EMPTY))) {
			text.append(suffix.getWord()).append(" ");

			if(outputSize > 0 && text.length() >= outputSize) {
				text = text.delete(outputSize, text.length());
				break;
			}

			prefixes.remove();
			prefixes.add(suffix.getWord());

			suffixes = dictionary.get(Prefix.of(prefixes));
			if (suffixes != null) {
				suffix = getSuffix(suffixes);
			}

		}

		return sanitize(text.toString());
	}

	private String sanitize(String text) {

		return text.trim();
	}

	private Suffix getSuffix(Multiset<Suffix> suffixes) {

		Suffix maxWeightSuffix = null;
		int maxWeight = 0;

		for (Suffix s : suffixes) {
			int weight = suffixes.count(s);
			if (weight > maxWeight) {
				maxWeight = weight;
				maxWeightSuffix = s;
			}
		}

		suffixes.remove(maxWeightSuffix);
		return maxWeightSuffix;
	}
}
