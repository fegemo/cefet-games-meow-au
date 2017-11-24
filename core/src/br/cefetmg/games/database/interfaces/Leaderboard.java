package br.cefetmg.games.database.interfaces;

import java.util.Map;

import br.cefetmg.games.database.model.RankingEntry;

public interface Leaderboard {
	
	public Map<String, RankingEntry> getEntryMap();
	
	public boolean isInitialized();
	
	public boolean isOnline();
	
	public void saveEntry(RankingEntry entry);
	
	public void saveEntry(String name, int points);
	
	public void connect();

}
