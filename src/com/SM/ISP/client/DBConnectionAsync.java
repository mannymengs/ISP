/**
 * 
 */
package com.SM.ISP.client;

import java.util.ArrayList;
import com.SM.ISP.shared.ISP;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author mannymengs
 *
 */
public interface DBConnectionAsync
{
	public void loadDB(AsyncCallback<Boolean> cb);
	public void getDBAL(AsyncCallback<ArrayList<ISP>> cb);
	public void byFirst(AsyncCallback<ArrayList<ISP>> cb);
	public void byLast(AsyncCallback<ArrayList<ISP>> cb);
	public void byTopic(AsyncCallback<ArrayList<ISP>> cb);
	public void byYear(AsyncCallback<ArrayList<ISP>> cb);
	public void search(String searchText, AsyncCallback<ArrayList<ISP>> cb);
	public void add(ISP p, AsyncCallback<Boolean> cb);
}
