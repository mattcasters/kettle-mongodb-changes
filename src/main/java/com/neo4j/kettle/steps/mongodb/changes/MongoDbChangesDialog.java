package com.neo4j.kettle.steps.mongodb.changes;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class MongoDbChangesDialog
  extends BaseStepDialog
  implements StepDialogInterface {
  private static Class<?> PKG = MongoDbChangesMeta.class;
  private Label wlStepname;
  private Text wStepname;
  private Label wlUser;
  private TextVar wUser;
  private Label wlPassword;
  private TextVar wPassword;
  private Label wlAuthDb;
  private TextVar wAuthDb;
  private Label wlServers;
  private TextVar wServers;
  private Label wlDatabase;
  private TextVar wDatabase;
  private Label wlCollection;
  private TextVar wCollection;
  private MongoDbChangesMeta input;

  public MongoDbChangesDialog(Shell parent, Object inputMetadata, TransMeta transMeta, String stepname) {
    super(parent, (BaseStepMeta)inputMetadata, transMeta, stepname);
    this.input = (MongoDbChangesMeta)((Object)inputMetadata);
  }

  public String open() {
    Shell parent = this.getParent();
    Display display = parent.getDisplay();
    this.shell = new Shell(parent, 3312);
    this.props.setLook((Control)this.shell);
    this.setShellImage(this.shell, (StepMetaInterface)this.input);
    ModifyListener lsMod = new ModifyListener(){

      public void modifyText(ModifyEvent e) {
        MongoDbChangesDialog.this.input.setChanged();
      }
    };
    this.changed = this.input.hasChanged();
    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = 5;
    formLayout.marginHeight = 5;
    this.shell.setLayout((Layout)formLayout);
    this.shell.setText("MongoDB Changes Stream Reader");
    int middle = this.props.getMiddlePct();
    int margin = 4;
    this.wlStepname = new Label((Composite)this.shell, 131072);
    this.wlStepname.setText("Step name");
    this.props.setLook((Control)this.wlStepname);
    this.fdlStepname = new FormData();
    this.fdlStepname.left = new FormAttachment(0, 0);
    this.fdlStepname.right = new FormAttachment(middle, - margin);
    this.fdlStepname.top = new FormAttachment(0, margin);
    this.wlStepname.setLayoutData((Object)this.fdlStepname);
    this.wStepname = new Text((Composite)this.shell, 18436);
    this.props.setLook((Control)this.wStepname);
    this.wStepname.addModifyListener(lsMod);
    this.fdStepname = new FormData();
    this.fdStepname.left = new FormAttachment(middle, 0);
    this.fdStepname.top = new FormAttachment((Control)this.wlStepname, 0, 16777216);
    this.fdStepname.right = new FormAttachment(100, 0);
    this.wStepname.setLayoutData((Object)this.fdStepname);
    Control lastControl = this.wStepname;

    this.wlUser = new Label((Composite)this.shell, 131072);
    this.wlUser.setText("Username");
    this.props.setLook((Control)this.wlUser);
    FormData fdlUser = new FormData();
    fdlUser.left = new FormAttachment(0, 0);
    fdlUser.right = new FormAttachment(middle, - margin);
    fdlUser.top = new FormAttachment((Control)lastControl, margin);
    this.wlUser.setLayoutData((Object)fdlUser);
    this.wUser = new TextVar((VariableSpace)this.transMeta, (Composite)this.shell, 18436);
    this.props.setLook((Control)this.wUser);
    this.wUser.addModifyListener(lsMod);
    FormData fdUser = new FormData();
    fdUser.left = new FormAttachment(middle, 0);
    fdUser.top = new FormAttachment((Control)this.wlUser, 0, 16777216);
    fdUser.right = new FormAttachment(100, 0);
    this.wUser.setLayoutData((Object)fdUser);
    lastControl = this.wUser;

    this.wlPassword = new Label((Composite)this.shell, 131072);
    this.wlPassword.setText("Password");
    this.props.setLook((Control)this.wlPassword);
    FormData fdlPassword = new FormData();
    fdlPassword.left = new FormAttachment(0, 0);
    fdlPassword.right = new FormAttachment(middle, - margin);
    fdlPassword.top = new FormAttachment((Control)lastControl, margin);
    this.wlPassword.setLayoutData((Object)fdlPassword);
    this.wPassword = new TextVar((VariableSpace)this.transMeta, (Composite)this.shell, 18436);
    this.wPassword.setEchoChar('*');
    this.props.setLook((Control)this.wPassword);
    this.wPassword.addModifyListener(lsMod);
    FormData fdPassword = new FormData();
    fdPassword.left = new FormAttachment(middle, 0);
    fdPassword.top = new FormAttachment((Control)this.wlPassword, 0, 16777216);
    fdPassword.right = new FormAttachment(100, 0);
    this.wPassword.setLayoutData((Object)fdPassword);
    lastControl = this.wPassword;

    this.wlAuthDb = new Label((Composite)this.shell, 131072);
    this.wlAuthDb.setText("Authentication Database");
    this.props.setLook((Control)this.wlAuthDb);
    FormData fdlAuthDb = new FormData();
    fdlAuthDb.left = new FormAttachment(0, 0);
    fdlAuthDb.right = new FormAttachment(middle, - margin);
    fdlAuthDb.top = new FormAttachment((Control)lastControl, margin);
    this.wlAuthDb.setLayoutData((Object)fdlAuthDb);
    this.wAuthDb = new TextVar((VariableSpace)this.transMeta, (Composite)this.shell, 18436);
    this.props.setLook((Control)this.wAuthDb);
    this.wAuthDb.addModifyListener(lsMod);
    FormData fdAuthDb = new FormData();
    fdAuthDb.left = new FormAttachment(middle, 0);
    fdAuthDb.top = new FormAttachment((Control)this.wlAuthDb, 0, 16777216);
    fdAuthDb.right = new FormAttachment(100, 0);
    this.wAuthDb.setLayoutData((Object)fdAuthDb);
    lastControl = this.wAuthDb;

    this.wlServers = new Label((Composite)this.shell, 131072);
    this.wlServers.setText("Servers");
    this.props.setLook((Control)this.wlServers);
    FormData fdlServers = new FormData();
    fdlServers.left = new FormAttachment(0, 0);
    fdlServers.right = new FormAttachment(middle, - margin);
    fdlServers.top = new FormAttachment((Control)lastControl, margin);
    this.wlServers.setLayoutData((Object)fdlServers);
    this.wServers = new TextVar((VariableSpace)this.transMeta, (Composite)this.shell, 18436);
    this.props.setLook((Control)this.wServers);
    this.wServers.addModifyListener(lsMod);
    FormData fdServers = new FormData();
    fdServers.left = new FormAttachment(middle, 0);
    fdServers.top = new FormAttachment((Control)this.wlServers, 0, 16777216);
    fdServers.right = new FormAttachment(100, 0);
    this.wServers.setLayoutData((Object)fdServers);
    lastControl = this.wServers;

    this.wlDatabase = new Label((Composite)this.shell, 131072);
    this.wlDatabase.setText("Database");
    this.props.setLook((Control)this.wlDatabase);
    FormData fdlDatabase = new FormData();
    fdlDatabase.left = new FormAttachment(0, 0);
    fdlDatabase.right = new FormAttachment(middle, - margin);
    fdlDatabase.top = new FormAttachment((Control)lastControl, margin);
    this.wlDatabase.setLayoutData((Object)fdlDatabase);
    this.wDatabase = new TextVar((VariableSpace)this.transMeta, (Composite)this.shell, 18436);
    this.props.setLook((Control)this.wDatabase);
    this.wDatabase.addModifyListener(lsMod);
    FormData fdDatabase = new FormData();
    fdDatabase.left = new FormAttachment(middle, 0);
    fdDatabase.top = new FormAttachment((Control)this.wlDatabase, 0, 16777216);
    fdDatabase.right = new FormAttachment(100, 0);
    this.wDatabase.setLayoutData((Object)fdDatabase);
    lastControl = this.wDatabase;

    this.wlCollection = new Label((Composite)this.shell, 131072);
    this.wlCollection.setText("Collection");
    this.props.setLook((Control)this.wlCollection);
    FormData fdlCollection = new FormData();
    fdlCollection.left = new FormAttachment(0, 0);
    fdlCollection.right = new FormAttachment(middle, - margin);
    fdlCollection.top = new FormAttachment((Control)lastControl, margin);
    this.wlCollection.setLayoutData((Object)fdlCollection);
    this.wCollection = new TextVar((VariableSpace)this.transMeta, (Composite)this.shell, 18436);
    this.props.setLook((Control)this.wCollection);
    this.wCollection.addModifyListener(lsMod);
    FormData fdCollection = new FormData();
    fdCollection.left = new FormAttachment(middle, 0);
    fdCollection.top = new FormAttachment((Control)this.wlCollection, 0, 16777216);
    fdCollection.right = new FormAttachment(100, 0);
    this.wCollection.setLayoutData((Object)fdCollection);
    lastControl = this.wCollection;

    this.wOK = new Button((Composite)this.shell, 8);
    this.wOK.setText(BaseMessages.getString(PKG, (String)"System.Button.OK", (String[])new String[0]));
    this.wCancel = new Button((Composite)this.shell, 8);
    this.wCancel.setText(BaseMessages.getString(PKG, (String)"System.Button.Cancel", (String[])new String[0]));
    this.setButtonPositions(new Button[]{this.wOK, this.wCancel}, margin, (Control)lastControl);
    this.lsCancel = new Listener(){

      public void handleEvent(Event e) {
        MongoDbChangesDialog.this.cancel();
      }
    };
    this.lsOK = new Listener(){

      public void handleEvent(Event e) {
        MongoDbChangesDialog.this.ok();
      }
    };
    this.wCancel.addListener(13, this.lsCancel);
    this.wOK.addListener(13, this.lsOK);
    this.lsDef = new SelectionAdapter(){

      public void widgetDefaultSelected(SelectionEvent e) {
        MongoDbChangesDialog.this.ok();
      }
    };
    this.wUser.addSelectionListener(this.lsDef);
    this.wPassword.addSelectionListener(this.lsDef);
    this.wAuthDb.addSelectionListener(this.lsDef);
    this.wStepname.addSelectionListener((SelectionListener)this.lsDef);
    this.wServers.addSelectionListener(this.lsDef);
    this.wDatabase.addSelectionListener(this.lsDef);
    this.wCollection.addSelectionListener(this.lsDef);
    this.shell.addShellListener((ShellListener)new ShellAdapter(){

      public void shellClosed(ShellEvent e) {
        MongoDbChangesDialog.this.cancel();
      }
    });
    this.setSize();
    this.getData();
    this.input.setChanged(this.changed);
    this.shell.open();
    while (!this.shell.isDisposed()) {
      if (display.readAndDispatch()) continue;
      display.sleep();
    }
    return this.stepname;
  }

  private void cancel() {
    this.stepname = null;
    this.input.setChanged(this.changed);
    this.dispose();
  }

  public void getData() {
    this.wStepname.setText(Const.NVL((String)this.stepname, (String)""));
    this.wUser.setText(Const.NVL((String)this.input.getUser(), (String)""));
    this.wPassword.setText(Const.NVL((String)this.input.getPassword(), (String)""));
    this.wAuthDb.setText(Const.NVL((String)this.input.getAuthenticationDatabase(), (String)""));
    this.wServers.setText(Const.NVL((String)this.input.getServers(), (String)""));
    this.wDatabase.setText(Const.NVL((String)this.input.getDatabase(), (String)""));
    this.wCollection.setText(Const.NVL((String)this.input.getCollection(), (String)""));
  }

  private void ok() {
    if (StringUtils.isEmpty((String)this.wStepname.getText())) {
      return;
    }
    this.stepname = this.wStepname.getText();
    this.input.setUser(this.wUser.getText());
    this.input.setPassword(this.wPassword.getText());
    this.input.setAuthenticationDatabase(this.wAuthDb.getText());
    this.input.setServers(this.wServers.getText());
    this.input.setDatabase(this.wDatabase.getText());
    this.input.setCollection(this.wCollection.getText());
    this.dispose();
  }

}