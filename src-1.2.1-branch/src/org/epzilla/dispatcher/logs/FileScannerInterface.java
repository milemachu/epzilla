package org.epzilla.dispatcher.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * File Scanner Interface class
 * Author: Chathura
 * Date: Mar 1, 2010
 * Time: 10:20:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface FileScannerInterface {
	public void readFile(File file,String strReq);
	public void readFile(File file);
}
