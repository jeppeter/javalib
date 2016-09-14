package com.github.jeppeter.jsonext;


public class JsonExtNotParsedException extends Exception {
	public JsonExtNotParsedException(String expr) {
		super(expr);
	}

}