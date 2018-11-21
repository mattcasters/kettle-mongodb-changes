package com.neo4j.kettle.steps.mongodb.changes;

import com.neo4j.kettle.steps.mongodb.changes.MongoDbChanges;
import com.neo4j.kettle.steps.mongodb.changes.MongoDbChangesData;
import com.neo4j.kettle.steps.mongodb.changes.MongoDbChangesDialog;
import java.util.List;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(id="MongoChanges", name="MongoDB Changes", description="Read information from MongoDB Changes Stream", image="TIP.svg", categoryDescription="Streaming")
public class MongoDbChangesMeta
  extends BaseStepMeta
  implements StepMetaInterface {
  private String user;
  private String password;
  private String authenticationDatabase;
  private String servers;
  private String database;
  private String collection;

  public void setDefault() {
  }

  public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int i, TransMeta transMeta, Trans trans) {
    return new MongoDbChanges(stepMeta, stepDataInterface, i, transMeta, trans);
  }

  public StepDataInterface getStepData() {
    return new MongoDbChangesData();
  }

  public String getDialogClassName() {
    return MongoDbChangesDialog.class.getName();
  }

  public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space, Repository repository, IMetaStore metaStore) {
    inputRowMeta.addValueMeta((ValueMetaInterface)new ValueMetaString("OperationType"));
    inputRowMeta.addValueMeta((ValueMetaInterface)new ValueMetaString("DocumentKey"));
    inputRowMeta.addValueMeta((ValueMetaInterface)new ValueMetaString("Document"));
    inputRowMeta.addValueMeta((ValueMetaInterface)new ValueMetaString("UpdateDescription"));
  }

  public String getXML() {
    StringBuilder xml = new StringBuilder();
    xml.append(XMLHandler.addTagValue((String)"user", (String)this.user));
    xml.append(XMLHandler.addTagValue((String)"password", (String)Encr.encryptPasswordIfNotUsingVariables((String)this.password)));
    xml.append(XMLHandler.addTagValue((String)"auth_db", (String)this.authenticationDatabase));
    xml.append(XMLHandler.addTagValue((String)"servers", (String)this.servers));
    xml.append(XMLHandler.addTagValue((String)"database", (String)this.database));
    xml.append(XMLHandler.addTagValue((String)"collection", (String)this.collection));
    return xml.toString();
  }

  public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
    this.user = XMLHandler.getTagValue((Node)stepnode, (String)"user");
    this.password = Encr.decryptPasswordOptionallyEncrypted((String)XMLHandler.getTagValue((Node)stepnode, (String)"password"));
    this.authenticationDatabase = XMLHandler.getTagValue((Node)stepnode, (String)"auth_db");
    this.servers = XMLHandler.getTagValue((Node)stepnode, (String)"servers");
    this.database = XMLHandler.getTagValue((Node)stepnode, (String)"database");
    this.collection = XMLHandler.getTagValue((Node)stepnode, (String)"collection");
    super.loadXML(stepnode, databases, metaStore);
  }

  public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    rep.saveStepAttribute(id_transformation, id_step, "user", this.user);
    rep.saveStepAttribute(id_transformation, id_step, "password", Encr.encryptPasswordIfNotUsingVariables((String)this.password));
    rep.saveStepAttribute(id_transformation, id_step, "auth_db", this.authenticationDatabase);
    rep.saveStepAttribute(id_transformation, id_step, "servers", this.servers);
    rep.saveStepAttribute(id_transformation, id_step, "database", this.database);
    rep.saveStepAttribute(id_transformation, id_step, "collection", this.collection);
  }

  public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException {
    this.user = rep.getStepAttributeString(id_step, "user");
    this.password = Encr.decryptPasswordOptionallyEncrypted((String)rep.getStepAttributeString(id_step, "password"));
    this.authenticationDatabase = rep.getStepAttributeString(id_step, "auth_db");
    this.servers = rep.getStepAttributeString(id_step, "servers");
    this.database = rep.getStepAttributeString(id_step, "database");
    this.collection = rep.getStepAttributeString(id_step, "collection");
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAuthenticationDatabase() {
    return this.authenticationDatabase;
  }

  public void setAuthenticationDatabase(String authenticationDatabase) {
    this.authenticationDatabase = authenticationDatabase;
  }

  public String getServers() {
    return this.servers;
  }

  public void setServers(String servers) {
    this.servers = servers;
  }

  public String getDatabase() {
    return this.database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getCollection() {
    return this.collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }
}