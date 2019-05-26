package pl.thathard.drop.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import pl.thathard.drop.Drop;

@Getter
public class DataService {

  private final Drop drop;
  private final MongoClient mongoClient;
  private final MongoDatabase mongoDatabase;
  private final MongoCollection<Document> players;

  public DataService(Drop drop) {
    this.drop = drop;
    this.mongoClient = new MongoClient(new MongoClientURI(drop.getDropConfig().getMongoLink()));
    this.mongoDatabase = mongoClient.getDatabase("drop");
    this.players = mongoDatabase.getCollection("players");
  }
}
