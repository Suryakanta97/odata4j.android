package org.odata4j.android.activity;

import org.odata4j.android.AndroidLogger;
import org.odata4j.android.ProgressDialogTask;
import org.odata4j.android.model.ServiceVM;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.jersey.consumer.behaviors.AllowSelfSignedCertsBehavior;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

public class EntityActivity extends Activity {

  private final AndroidLogger log = AndroidLogger.get(getClass());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ServiceVM service = (ServiceVM) getIntent().getExtras().getSerializable("service");
    final ORelatedEntityLink link = (ORelatedEntityLink) getIntent().getExtras().getSerializable("link");

    log.info("link " + link.getHref()); //  Titles('13kZA')/Season

    setTitle("Entity");

    new ProgressDialogTask<OEntity>(this, log, "Loading...") {
      protected OEntity doStuff() {
        ODataConsumer c = ODataConsumers.newBuilder(service.getUri()).setClientBehaviors(AllowSelfSignedCertsBehavior.allowSelfSignedCerts()).build();
        return c.getEntity(link).execute();
      }
      protected void showStuff(OEntity entity) {
        log.info("entity:" + entity);
        if (entity == null) {
          TextView tv = new TextView(EntityActivity.this);
          tv.setText("no content");
          setContentView(tv);
          return;
        }
        TableLayout table = EntityViews.newEntityTable(EntityActivity.this, entity, service);
        setContentView(table);
      }}.execute();
  }

}
