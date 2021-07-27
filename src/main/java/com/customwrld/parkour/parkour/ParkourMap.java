package com.customwrld.parkour.parkour;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.customlib.hologram.Hologram;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter @Setter
public class ParkourMap {

    @Getter private static final MongoCollection<Document> collection = CommonLib.getCommonLib().getMongoDatabase().getCollection("parkour");
    @Getter private static final Set<ParkourMap> maps = new CopyOnWriteArraySet<>(); //Best implementation, the set won't be written to often

    private final String name;
    private final Location spawn;
    private final List<LeaderboardUser> leaderboards;
    private final List<Checkpoint> checkpoints;

    public ParkourMap(String name, Location spawn, List<LeaderboardUser> leaderboards, List<Checkpoint> checkpoints) {
        this.name = name;
        this.spawn = spawn;
        this.leaderboards = leaderboards;
        this.checkpoints = checkpoints;

        maps.add(this);
    }

    public static void initialize() {
        ParkourMap.getMaps().clear();

        try (MongoCursor<Document> cursor = ParkourMap.getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

//                ParkourMap map = new ParkourMap();
            }
        }
    }

}
