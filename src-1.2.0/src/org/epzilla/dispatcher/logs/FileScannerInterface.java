package org.epzilla.dispatcher.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface FileScannerInterface {
	public void readFile(File file,String strReq);
	public void readFile(File file);
	public void printArray(List<String> array);
}
