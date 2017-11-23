package br.cefetmg.games.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RankingEntry {
	public String user;
	public Integer points;
	
	public RankingEntry() {
		
	}
	
	public RankingEntry(String user, Integer points) {
		this.user = user;
		this.points = points;
	}
}
