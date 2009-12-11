package org.epzilla;

public class Host {
	
	private int hostId;
	private boolean isLeader;
	private String ipAddress;
	private String leaderIpAddress;
	
	public int getHostId() {
		return hostId;
	}
	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	public boolean isLeader() {
		return isLeader;
	}
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getLeaderIpAddress() {
		return leaderIpAddress;
	}
	public void setLeaderIpAddress(String leaderIpAddress) {
		this.leaderIpAddress = leaderIpAddress;
	}
	
	

}
