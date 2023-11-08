package com.oracle.fn;

import org.junit.Rule;
import org.junit.Test;

import com.fnproject.fn.testing.FnResult;
import com.fnproject.fn.testing.FnTestingRule;

public class SpeechFunctionTest {

	@Rule
	public final FnTestingRule testing = FnTestingRule.createDefault();

	@Test
	public void shouldReturnGreeting() {
		testing.givenEvent().enqueue();
		testing.thenRun(SpeechFunction.class, "handleRequest");

		FnResult result = testing.getOnlyResult();
//		assertEquals("Hello, world!", result.getBodyAsString());
	}

}