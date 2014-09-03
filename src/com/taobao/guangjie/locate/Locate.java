package com.taobao.guangjie.locate;

import java.util.List;
import java.util.Set;

import com.taobao.guangjie.datatypes.WifiRecord;

public interface Locate {
	public void setThreshold(int t);
	public Set<String> locate(List<WifiRecord> records);
	public String locateOne(List<WifiRecord> records);
}
