package com.infinities.keystone4j;

import java.util.Set;

import com.google.common.collect.Sets;

public class Test {

	// private static final Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String args[]) {

		Set<String> set = Sets.newHashSet();
		set.add("123");
		set.add("234");
		set.add("345");
		set.add("456");
		set.add("567");
		String tmp = test(set);

		System.out.println(tmp);

	}

	public static String test(Set<String> set) {
		for (String ref : set) {
			if ("345".equals(ref)) {
				set.remove(ref);
				return ref;
			}
		}

		return null;
	}

}
