package com.neo4j.kettle.steps.mongodb.changes;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class MongoDbChangesData
  extends BaseStepData
  implements StepDataInterface
{
  public RowMetaInterface outputRowMeta;
  public MongoClient mongoClient;
  public MongoDatabase db;
  public MongoCollection<Document> collection;
  public List<Bson> pipeline;
  public MongoCursor<ChangeStreamDocument<Document>> cursor;
  
  public MongoDbChangesData() {}
}