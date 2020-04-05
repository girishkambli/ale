package com.ale.markov.service;

import com.ale.markov.model.Prefix;
import com.ale.markov.model.Suffix;
import com.google.common.collect.Multiset;
import java.util.Map;

public interface TextGenerator {

	String generate(Map<Prefix, Multiset<Suffix>> dictionary, int prefixSize, int outputSize);

}
