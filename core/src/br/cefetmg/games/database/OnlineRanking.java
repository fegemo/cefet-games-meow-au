package br.cefetmg.games.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import br.cefetmg.games.database.interfaces.Leaderboard;
import br.cefetmg.games.database.model.RankingEntry;

public class OnlineRanking {

	/**
	 * Deve ser setado antes de usar o ranking!
	 */
	private static Leaderboard leaderboard;

	public static final String ENTRIES_KEY = "entries";

	public static final String POINTS_KEY = "points";

	public static final String DATABASE_URL = "https://cefet-games-meow-au.firebaseio.com/";

	public static final Integer SIZE_LIMIT = 10;

	private static final Comparator<Entry<String, RankingEntry>> ENTRY_COMPARATOR = new Comparator<Entry<String, RankingEntry>>() {
		@Override
		public int compare(Entry<String, RankingEntry> o1, Entry<String, RankingEntry> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};

	public static void connect() {
		if (leaderboard != null) {
			leaderboard.connect();
		}
	}

	public static boolean isInitialized() {
		return leaderboard != null && leaderboard.isInitialized();
	}

	public static boolean isOnline() {
		return leaderboard != null && leaderboard.isOnline();
	}

	public static void saveEntry(RankingEntry entry) {
		if (leaderboard != null) {
			leaderboard.saveEntry(entry);
		}
	}

	public static void saveEntry(String name, int points) {
		saveEntry(new RankingEntry(name, points));
	}

	public static void updateEntries(String key, RankingEntry entry) {
		if (leaderboard != null) {
			synchronized (leaderboard.getEntryMap()) {
				leaderboard.getEntryMap().put(key, entry);
				List<Entry<String, RankingEntry>> mapEntries = new ArrayList<Entry<String, RankingEntry>>(
						leaderboard.getEntryMap().entrySet());
				Collections.sort(mapEntries, ENTRY_COMPARATOR);
				leaderboard.getEntryMap().clear();
				for (Entry<String, RankingEntry> mapEntry : mapEntries) {
					leaderboard.getEntryMap().put(mapEntry.getKey(), mapEntry.getValue());
				}
			}
		}
	}

	public static void removeEntry(String key) {
		if (leaderboard != null) {
			synchronized (leaderboard.getEntryMap()) {
				leaderboard.getEntryMap().remove(key);
			}
		}
	}

	public static List<RankingEntry> getEntries() {
		if (leaderboard != null) {
			synchronized (leaderboard.getEntryMap()) {
				return new ArrayList<RankingEntry>(leaderboard.getEntryMap().values());
			}
		} else {
			return new ArrayList<RankingEntry>();
		}
	}

	public static void setLeaderboard(Leaderboard leaderBoardObj) {
		if (leaderboard == null) {
			leaderboard = leaderBoardObj;
		}
	}

}
