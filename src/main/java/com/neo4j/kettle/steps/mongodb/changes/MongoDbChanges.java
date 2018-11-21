package com.neo4j.kettle.steps.mongodb.changes;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import com.mongodb.client.model.changestream.UpdateDescription;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import java.util.Arrays;
import java.util.Collections;

public class MongoDbChanges
  extends BaseStep
  implements StepInterface {
  public MongoDbChanges(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
    super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
  }

  public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
    MongoDbChangesMeta meta = (MongoDbChangesMeta)smi;
    MongoDbChangesData data = (MongoDbChangesData)sdi;
    String user = this.getTrans().environmentSubstitute(meta.getUser());
    String password = this.getTrans().environmentSubstitute(meta.getPassword());
    String authenticationDatabase = this.getTrans().environmentSubstitute(meta.getAuthenticationDatabase());
    String servers = this.getTrans().environmentSubstitute(meta.getServers());
    String database = this.getTrans().environmentSubstitute(meta.getDatabase());
    String collection = this.getTrans().environmentSubstitute(meta.getCollection());
    this.logDetailed("Authenticating to Mongo using user : '" + user + "', database '" + authenticationDatabase + "'");
    this.logDetailed("Reading from servers: '" + servers + "', database '" + database + "', collection '" + collection + "'");
    try {
      String url = "mongodb://" + user + ":" + password + "@" + servers + "/?authSource=" + authenticationDatabase + "&authMechanism=SCRAM-SHA-1";
      data.mongoClient = new MongoClient(new MongoClientURI(url));
      this.logDetailed("Created Mongo client to " + url);
      data.db = data.mongoClient.getDatabase(database);
      this.logDetailed("Got database '" + database + "'");
      data.collection = data.db.getCollection(collection);
      this.logDetailed("Got collection '" + collection + "'");
    }
    catch (Exception e) {
      this.log.logError("Unable to open collection '" + collection + "' on database '" + database + "'", (Throwable)e);
      return false;
    }
    return super.init(smi, sdi);
  }

  public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
    super.dispose(smi, sdi);
  }

  public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    MongoDbChangesMeta meta = (MongoDbChangesMeta)smi;
    MongoDbChangesData data = (MongoDbChangesData)sdi;
    if (this.first) {
      this.first = false;
      data.outputRowMeta = new RowMeta();
      meta.getFields(data.outputRowMeta, this.getStepname(), null, this.getStepMeta(), (VariableSpace)this, this.repository, this.metaStore);
      data.pipeline = Collections.singletonList(Aggregates.match((Bson)Filters.in((String)"operationType", Arrays.asList("delete", "insert", "replace", "update", "invalidate"))));
      this.logDetailed("Successfully obtained pipeline");
      data.cursor = data.collection.watch(data.pipeline).iterator();
      this.logDetailed("Successfully obtained cursor");
    }
    if (!data.cursor.hasNext()) {
      data.cursor.close();
      this.setOutputDone();
      return false;
    }
    ChangeStreamDocument changeStreamDocument = (ChangeStreamDocument)data.cursor.next();
    Document document = (Document)changeStreamDocument.getFullDocument();
    OperationType operationType = changeStreamDocument.getOperationType();
    BsonDocument documentKey = changeStreamDocument.getDocumentKey();
    UpdateDescription updateDescription = changeStreamDocument.getUpdateDescription();
    Object[] row = RowDataUtil.allocateRowData((int)data.outputRowMeta.size());
    int index = 0;
    row[index++] = operationType.getValue();
    row[index++] = documentKey.getFirstKey();
    row[index++] = document.toJson();
    row[index++] = updateDescription.toString();
    this.putRow(data.outputRowMeta, row);
    return true;
  }
}