package com.freader.bookmodel;

import java.util.ArrayList;
import java.util.HashMap;

public interface PagedBookListener {
	void callback(ArrayList<CharSequence> pages, HashMap<Integer, Integer> hm);
}
