package br.cefetmg.games.database;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineRanking {

	private static final String DATABASE_URL = "https://cefet-games-meow-au.firebaseio.com/";

	private static DatabaseReference database = null;

	private static boolean connected = false;

	public static void connect() {
		if (!isInitialized()) {
			try {
				Path configPath = Paths.get(".").resolve("service-account.json");
				FileInputStream serviceAccount = new FileInputStream(configPath.toFile());
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl(DATABASE_URL)
						.build();
				FirebaseApp.initializeApp(options);
				database = FirebaseDatabase.getInstance().getReference();
				database.child(".info/connected").addValueEventListener(new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot snapshot) {
						connected = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
					}

					@Override
					public void onCancelled(DatabaseError error) {
						connected = false;
					}
				});
				DatabaseReference.goOnline();
			} catch (Exception e) {
				database = null;
			}
		}
	}

	public static boolean isInitialized() {
		return database != null;
	}

	public static boolean isOnline() {
		return isInitialized() && connected;
	}

}
