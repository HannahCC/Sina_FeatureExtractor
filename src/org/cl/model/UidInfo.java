package org.cl.model;

import java.util.HashSet;

public class UidInfo
{
	
	/** 用户ID*/
	private String id = null;
	/** 好友ID*/
	private HashSet<String> uids=null;
	/** 下一页游标*/
	//private int next_cursor=0;
	/** 上一页游标*/
	//private int previous_cursor=0;
	/** ID总数*/
	private int total_number=0;
	
	public UidInfo()
	{
		uids=new HashSet<String>();
	}

	@Override
	public String toString()
	{
		return "UidInfo [total_number=" + total_number
				+ ", uids=" + uids + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashSet<String> getUids()
	{
		return uids;
	}

	public void setUids(HashSet<String> uids)
	{
		this.uids = uids;
	}

	public int getTotal_number()
	{
		return total_number;
	}

	public void setTotal_number(int totalNumber)
	{
		total_number = totalNumber;
	}
}
