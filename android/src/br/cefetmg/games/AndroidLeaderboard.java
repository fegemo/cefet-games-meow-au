package br.cefetmg.games;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedHashMap;
import java.util.Map;

import br.cefetmg.games.database.OnlineRanking;
import br.cefetmg.games.database.interfaces.Leaderboard;
import br.cefetmg.games.database.model.RankingEntry;

public class AndroidLeaderboard implements Leaderboard {

    private DatabaseReference database;

    private Boolean databaseConnected = false;

    private Boolean hasNetworkConnection = false;

    private final Map<String, RankingEntry> entryMap = new LinkedHashMap<String, RankingEntry>(10);

    private FirebaseAuth mAuth;

    @Override
    public synchronized void connect() {
        Thread connectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        InetSocketAddress socketAddress = new InetSocketAddress("accounts.google.com", 443);
                        SocketChannel channel = SocketChannel.open();
                        channel.configureBlocking(false);
                        channel.connect(socketAddress);
                        while (!channel.finishConnect()) {
                            sleep(100);
                        }
                        hasNetworkConnection = true;
                    } catch (Exception e) {
                        hasNetworkConnection = false;
                    } finally {
                        sleep(5000);
                    }
                }
            }
        });
        connectThread.setDaemon(true);
        connectThread.start();

        Thread dbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isInitialized()) {
                    while (!hasNetworkConnection) {
                        sleep(1000);
                    }
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInAnonymously()
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        database = db.getReference();
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
                                        database.child(OnlineRanking.ENTRIES_KEY).orderByChild(OnlineRanking.POINTS_KEY)
                                                .limitToLast(OnlineRanking.SIZE_LIMIT).addChildEventListener(new ChildEventListener() {

                                            @Override
                                            public void onChildRemoved(DataSnapshot snapshot) {
                                                OnlineRanking.removeEntry(snapshot.getKey());
                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                                                OnlineRanking.updateEntries(snapshot.getKey(),
                                                        snapshot.getValue(RankingEntry.class));
                                            }

                                            @Override
                                            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                                                OnlineRanking.updateEntries(snapshot.getKey(),
                                                        snapshot.getValue(RankingEntry.class));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {

                                            }
                                        });
                                        DatabaseReference.goOnline();
                                    } else {
                                        database = null;
                                    }
                                }
                            });

                }
            }
        });
        dbThread.setDaemon(true);
        dbThread.start();
    }

    @Override
    public boolean isInitialized() {
        return database != null;
    }

    @Override
    public boolean isOnline() {
        return isInitialized() && databaseConnected && hasNetworkConnection;
    }

    @Override
    public void saveEntry(RankingEntry entry) {
        if (isInitialized()) {
            String key = database.child(OnlineRanking.ENTRIES_KEY).push().getKey();
            database.child(OnlineRanking.ENTRIES_KEY).child(key).setValue(entry);
        }
    }

    @Override
    public void saveEntry(String name, int points) {
        saveEntry(new RankingEntry(name, points));
    }

    private static void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public Map<String, RankingEntry> getEntryMap() {
        return entryMap;
    }

}
