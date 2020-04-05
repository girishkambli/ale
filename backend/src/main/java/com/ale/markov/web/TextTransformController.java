package com.ale.markov.web;

import com.ale.markov.service.DictionaryBuilder;
import com.ale.markov.service.TextGenerator;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "v1")
public class TextTransformController {

	private final DictionaryBuilder dictionaryBuilder;

	private final TextGenerator textGenerator;

	@Autowired
	public TextTransformController(DictionaryBuilder dictionaryBuilder,
		TextGenerator textGenerator) {

		this.dictionaryBuilder = dictionaryBuilder;
		this.textGenerator = textGenerator;
	}

	@PostMapping(path = "transform")
	public String transform(@RequestParam("file") MultipartFile file,
		@RequestParam("prefixSize") int prefixSize,
		@RequestParam("outputSize") int outputSize) throws IOException {

		return textGenerator
			.generate(dictionaryBuilder.build(new String(file.getBytes()), prefixSize), prefixSize, outputSize);
	}

}
