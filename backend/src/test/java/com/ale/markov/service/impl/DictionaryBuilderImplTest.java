package com.ale.markov.service.impl;


import static com.ale.markov.service.impl.DictionaryBuilderImpl.EMPTY;
import static com.ale.markov.service.impl.Utils.createPrefix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.ale.markov.model.Prefix;
import com.ale.markov.model.Suffix;
import com.ale.markov.service.DictionaryBuilder;
import com.google.common.collect.Multiset;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DictionaryBuilderImplTest {

	private DictionaryBuilder dictionaryBuilder;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {

		dictionaryBuilder = new DictionaryBuilderImpl();
	}

	@Test
	public void testForEmptyInput() {

		assertThat(dictionaryBuilder.build("", 1), is(anEmptyMap()));
	}

	@Test
	public void testForInvalidPrefix() {

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("prefix length must be greater than zero.");

		dictionaryBuilder.build("", 0);
	}

	@Test
	public void testForPrefixWithLengthOne() {

		Map<Prefix, Multiset<Suffix>> dictionary = dictionaryBuilder.build("Hello World", 1);
		assertThat(dictionary, is(notNullValue()));
		assertThat(dictionary.size(), is(3));

		assertThat(dictionary.get(Prefix.of(createPrefix("Hello"))), contains(Suffix.of("World")));
		assertThat(dictionary.get(Prefix.of(createPrefix(EMPTY))), contains(Suffix.of("Hello")));
		assertThat(dictionary.get(Prefix.of(createPrefix("World"))), contains(Suffix.of(EMPTY)));
	}

	@Test
	public void testForPrefixWithLengthTwo_1() {

		Map<Prefix, Multiset<Suffix>> dictionary = dictionaryBuilder.build("Hello World", 2);
		assertThat(dictionary, is(notNullValue()));
		assertThat(dictionary.size(), is(3));

		assertThat(dictionary.get(Prefix.of(createPrefix("Hello", "World"))),
			contains(Suffix.of(EMPTY)));
		assertThat(dictionary.get(Prefix.of(createPrefix(EMPTY, EMPTY))),
			contains(Suffix.of("Hello")));
		assertThat(dictionary.get(Prefix.of(createPrefix(EMPTY, "Hello"))),
			contains(Suffix.of("World")));
	}

	@Test
	public void testForPrefixWithLengthTwo_2() {

		Map<Prefix, Multiset<Suffix>> dictionary = dictionaryBuilder
			.build("Dog jumped over the wall", 2);
		assertThat(dictionary, is(notNullValue()));
		assertThat(dictionary.size(), is(6));

		assertThat(dictionary.get(Prefix.of(createPrefix(EMPTY, EMPTY))),
			contains(Suffix.of("Dog")));
		assertThat(dictionary.get(Prefix.of(createPrefix(EMPTY, "Dog"))),
			contains(Suffix.of("jumped")));
		assertThat(dictionary.get(Prefix.of(createPrefix("Dog", "jumped"))),
			contains(Suffix.of("over")));
		assertThat(dictionary.get(Prefix.of(createPrefix("jumped", "over"))),
			contains(Suffix.of("the")));
		assertThat(dictionary.get(Prefix.of(createPrefix("over", "the"))),
			contains(Suffix.of("wall")));
		assertThat(dictionary.get(Prefix.of(createPrefix("the", "wall"))),
			contains(Suffix.of(EMPTY)));
	}

	@Test
	public void testForPrefixWithLengthTwo_9() {

		Map<Prefix, Multiset<Suffix>> dictionary = dictionaryBuilder
			.build("Dog jumped over the wall and over the moon. Dog jumped over the fence.", 2);
		assertThat(dictionary, is(notNullValue()));

	}

	@Test
	public void testForPrefixWithLengthTwo_3() {

		String text = "Blessed are the poor in spirit,\n"
			+ "for theirs is the kingdom of heaven.\n"
			+ "Blessed are those who mourn,\n"
			+ "for they will be comforted.\n"
			+ "Blessed are the meek,\n"
			+ "for they will inherit the earth.\n"
			+ "Blessed are those who hunger and thirst for righteousness,\n"
			+ "for they will be filled.\n"
			+ "Blessed are the merciful,\n"
			+ "for they will be shown mercy.\n"
			+ "Blessed are the pure in heart,\n"
			+ "for they will see God.\n"
			+ "Blessed are the peacemakers,\n"
			+ "for they will be called sons of God.";

		Map<Prefix, Multiset<Suffix>> dictionary = dictionaryBuilder
			.build(text, 2);
		assertThat(dictionary, is(notNullValue()));

		assertThat(
			dictionary.get(Prefix.of(createPrefix("Blessed", "are"))).count(Suffix.of("the")),
			is(5));
		assertThat(
			dictionary.get(Prefix.of(createPrefix("Blessed", "are"))).count(Suffix.of("those")),
			is(2));
		assertThat(dictionary.get(Prefix.of(createPrefix("they", "will"))).count(Suffix.of("be")),
			is(4));
		assertThat(dictionary.get(Prefix.of(createPrefix("will", "be"))).size(), is(4));
		assertThat(dictionary.get(Prefix.of(createPrefix("of", "God."))).size(), is(1));
		assertThat(dictionary.get(Prefix.of(createPrefix("of", "God."))).contains(Suffix.of(EMPTY)),
			is(true));

		System.out.println(new TextGeneratorImpl().generate(dictionary, 2, 200));
	}
}
