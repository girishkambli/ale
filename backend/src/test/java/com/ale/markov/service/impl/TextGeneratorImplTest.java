package com.ale.markov.service.impl;

import static com.ale.markov.service.impl.DictionaryBuilderImpl.EMPTY;
import static com.ale.markov.service.impl.Utils.createPrefix;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.ale.markov.model.Prefix;
import com.ale.markov.model.Suffix;
import com.ale.markov.service.TextGenerator;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import java.util.LinkedList;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TextGeneratorImplTest {

	private TextGenerator textGenerator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {

		textGenerator = new TextGeneratorImpl();
	}

	@Test
	public void testForEmptyDictionary() {

		assertThat(textGenerator.generate(ImmutableMap.of(), 1, 200), is(""));
	}

	@Test
	public void testForInvalidPrefixLength() {

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("prefix length must be greater than zero.");

		textGenerator
			.generate(ImmutableMap.of(Prefix.of(new LinkedList<>()), HashMultiset.create()), 0,
				200);
	}

	@Test
	public void testForPrefixWithLengthOne() {

		Multiset<Suffix> suffixes1 = HashMultiset.create();
		suffixes1.add(Suffix.of("Hello"));

		Multiset<Suffix> suffixes2 = HashMultiset.create();
		suffixes2.add(Suffix.of("World"));

		Multiset<Suffix> suffixes3 = HashMultiset.create();
		suffixes3.add(Suffix.of(EMPTY));

		Map<Prefix, Multiset<Suffix>> dictionary = ImmutableMap.<Prefix, Multiset<Suffix>>builder()
			.put(Prefix.of(createPrefix(EMPTY)), suffixes1)
			.put(Prefix.of(createPrefix("Hello")), suffixes2)
			.put(Prefix.of(createPrefix("World")), suffixes3)
			.build();

		assertThat(textGenerator.generate(dictionary, 1, 200), is(equalTo("Hello World")));
	}

	@Test
	public void testForPrefixWithLengthTwo() {

		Multiset<Suffix> suffixes1 = HashMultiset.create();
		suffixes1.add(Suffix.of("Hello"));

		Multiset<Suffix> suffixes2 = HashMultiset.create();
		suffixes2.add(Suffix.of("World"));

		Multiset<Suffix> suffixes3 = HashMultiset.create();
		suffixes3.add(Suffix.of(EMPTY));

		Map<Prefix, Multiset<Suffix>> dictionary = ImmutableMap.<Prefix, Multiset<Suffix>>builder()
			.put(Prefix.of(createPrefix(EMPTY, EMPTY)), suffixes1)
			.put(Prefix.of(createPrefix(EMPTY, "Hello")), suffixes2)
			.put(Prefix.of(createPrefix("Hello", "World")), suffixes3)
			.build();

		assertThat(textGenerator.generate(dictionary, 2, 200), is(equalTo("Hello World")));
	}

}