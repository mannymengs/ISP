/**
 * 
 */
package com.SM.ISP.client;

import java.util.ArrayList;

import com.SM.ISP.shared.ISP;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author mannymengs
 *
 */
@RemoteServiceRelativePath("db")
public interface DBConnection extends RemoteService 
{
	public boolean loadDB();
	public ArrayList<ISP> getDBAL();
	public ArrayList<ISP> byFirst();
	public ArrayList<ISP> byLast();
	public ArrayList<ISP> byTopic();
	public ArrayList<ISP> byYear();
	public ArrayList<ISP> search(String searchText);
	public boolean add(ISP p);
}
