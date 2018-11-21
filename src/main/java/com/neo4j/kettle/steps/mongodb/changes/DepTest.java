package com.neo4j.kettle.steps.mongodb.changes;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import com.mongodb.client.model.changestream.UpdateDescription;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DepTest {

  public static void main(String[] args) {
    MongoClient mongoClient = new MongoClient( new MongoClientURI("mongodb://host1:port1,host2:port2..."));

    // Select the MongoDB database and collection to open the change stream against

    MongoDatabase db = mongoClient.getDatabase("myTargetDatabase");

    MongoCollection<Document> collection = db.getCollection("myTargetCollection");

    // Create $match pipeline stage.
    List<Bson> pipeline = Collections.singletonList(Aggregates.match(Filters.or(
      Document.parse("{'fullDocument.username': 'alice'}"),
      Filters.in("operationType", Arrays.asList("delete", "insert", "replace", "update", "invalidate")))));

    // Create the change stream cursor, passing the pipeline to the
    // collection.watch() method

    MongoCursor<ChangeStreamDocument<Document>> cursor = collection.watch(pipeline).iterator();
    while(cursor.hasNext()) {
      ChangeStreamDocument<Document> changeStreamDocument = cursor.next();
      Document document = changeStreamDocument.getFullDocument();
      OperationType operationType = changeStreamDocument.getOperationType();
      BsonDocument documentKey = changeStreamDocument.getDocumentKey();
      UpdateDescription updateDescription = changeStreamDocument.getUpdateDescription();

      System.out.println( "Picked up change: "+operationType.toString()+" of key "+documentKey.getFirstKey()+" : "+updateDescription);
    }
  }

}
