package br.cefetmg.games.database;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.cefetmg.games.database.model.RankingEntry;

public class OnlineRanking {

	private static final int THREAD_SLEEP_TIME = 5000;

	private static final String ENTRIES_KEY = "entries";

	private static final String POINTS_KEY = "points";

	private static final String DATABASE_URL = "https://cefet-games-meow-au.firebaseio.com/";

	private static DatabaseReference database = null;

	private static Boolean databaseConnected = false;

	private static Boolean hasNetworkConnection = false;

	public static final Integer SIZE_LIMIT = 10;

	private static final Map<String, RankingEntry> ENTRIES = new LinkedHashMap<String, RankingEntry>(10);

	private static final Comparator<Entry<String, RankingEntry>> ENTRY_COMPARATOR = new Comparator<Entry<String, RankingEntry>>() {
		@Override
		public int compare(Entry<String, RankingEntry> o1, Entry<String, RankingEntry> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};

	public static synchronized void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						InetSocketAddress socketAddress = new InetSocketAddress("accounts.google.com", 443);
						SocketChannel channel = SocketChannel.open();
						channel.configureBlocking(false);
						channel.connect(socketAddress);
						while (!channel.finishConnect()) {
							sleep(1000);
						}
						hasNetworkConnection = true;
					} catch (Exception e) {
						hasNetworkConnection = false;
					} finally {
						sleep(THREAD_SLEEP_TIME);
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (!isInitialized()) {
						while (!hasNetworkConnection) {
							sleep(THREAD_SLEEP_TIME);
						}
						Path configPath = Paths.get("./data").resolve("service-account.json");
						FileInputStream serviceAccount = new FileInputStream(configPath.toFile());
						FirebaseOptions options = new FirebaseOptions.Builder()
								.setCredentials(GoogleCredentials.fromStream(serviceAccount))
								.setDatabaseUrl(DATABASE_URL).build();
						FirebaseApp.initializeApp(options);
						database = FirebaseDatabase.getInstance().getReference();
						database.child(".info/connected").addValueEventListener(new ValueEventListener() {

							@Override
							public void onDataChange(DataSnapshot snapshot) {
								databaseConnected = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
							}

							@Override
							public void onCancelled(DatabaseError error) {
								databaseConnected = false;
							}
						});
						database.child(ENTRIES_KEY).orderByChild(POINTS_KEY).limitToLast(SIZE_LIMIT)
								.addChildEventListener(new ChildEventListener() {

									@Override
									public void onChildRemoved(DataSnapshot snapshot) {
										synchronized (ENTRIES) {
											ENTRIES.remove(snapshot.getKey());
										}
									}

									@Override
									public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

									}

									@Override
									public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
										updateEntries(snapshot.getKey(), snapshot.getValue(RankingEntry.class));
									}

									@Override
									public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
										updateEntries(snapshot.getKey(), snapshot.getValue(RankingEntry.class));
									}

									@Override
									public void onCancelled(DatabaseError error) {

									}
								});
						DatabaseReference.goOnline();
					}
				} catch (Exception e) {
					database = null;
				}
			}
		}).start();
	}

	public static boolean isInitialized() {
		return database != null;
	}

	public static boolean isOnline() {
		return isInitialized() && databaseConnected && hasNetworkConnection;
	}

	public static void saveEntry(RankingEntry entry) {
		if (isInitialized()) {
			String key = database.child(ENTRIES_KEY).push().getKey();
			database.child(ENTRIES_KEY).child(key).setValueAsync(entry);
		}
	}

	public static void saveEntry(String name, int points) {
		saveEntry(new RankingEntry(name, points));
	}

	private static void updateEntries(String key, RankingEntry entry) {
		synchronized (ENTRIES) {
			ENTRIES.put(key, entry);
			List<Entry<String, RankingEntry>> mapEntries = new ArrayList<Entry<String, RankingEntry>>(
					ENTRIES.entrySet());
			Collections.sort(mapEntries, ENTRY_COMPARATOR);
			ENTRIES.clear();
			for (Entry<String, RankingEntry> mapEntry : mapEntries) {
				ENTRIES.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
	}

	public static List<RankingEntry> getEntries() {
		synchronized (ENTRIES) {
			return new ArrayList<RankingEntry>(ENTRIES.values());
		}
	}

	private static void sleep(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {

		}
	}

}
