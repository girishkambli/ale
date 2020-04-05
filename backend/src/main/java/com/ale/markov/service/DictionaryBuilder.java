package com.ale.markov.service;

import com.ale.markov.model.Prefix;
import com.ale.markov.model.Suffix;
import com.google.common.collect.Multiset;
import java.util.Map;

public interface DictionaryBuilder {

	Map<Prefix, Multiset<Suffix>> build(String input, int prefix);

}
