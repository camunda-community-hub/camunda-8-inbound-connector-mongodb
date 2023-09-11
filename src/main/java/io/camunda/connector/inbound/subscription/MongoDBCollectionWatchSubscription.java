package io.camunda.connector.inbound.subscription;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.bson.Document;
import com.mongodb.ConnectionString;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class MongoDBCollectionWatchSubscription {

    private final static Logger LOG = LoggerFactory.getLogger(MongoDBCollectionWatchSubscription.class);
    private BsonString token;
    private BsonTimestamp startAtOperationTime;

    public MongoDBCollectionWatchSubscription(String mongoDBUrl, String mongoDBDatabase, String mongoDBCollection, String mongoDBEvent, Consumer<MongoDBCollectionWatchSubscriptionEvent> callback) {
        LOG.info("Activating MongoDB Collection Watch subscription");

        ConnectionString connectionString = new ConnectionString(mongoDBUrl);

        try {
            MongoClientSettings clientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            MongoClient mongoClient = MongoClients.create(clientSettings);
            MongoDatabase db = mongoClient.getDatabase(mongoDBDatabase);

            //MongoCollection<Document> collection = db.getCollection(mongoDBCollection);
            BsonDocument resumeToken = null;

            while(true) {

                ChangeStreamIterable<Document> changeStream;

                // Monitor collection or database
                if(mongoDBCollection != null) {
                    LOG.info("collection not null");
                    changeStream = db.getCollection(mongoDBCollection).watch();
                } else {
                    LOG.info("collection null");
                    changeStream = db.watch();
                }
                changeStream.forEach(event -> {
                    LOG.info("Received a change to the collection: " + event.toString());

                    if(mongoDBEvent.equalsIgnoreCase("any") || (mongoDBEvent.equalsIgnoreCase(event.getOperationTypeString()))) {
                        // Start new process or send message to intermediate event
                        Object before;
                        Object after;

                        if(event.getFullDocumentBeforeChange() != null) {
                            before = new JSONObject(event.getFullDocumentBeforeChange()).toMap();
                        } else {
                            before = "{}";
                        }

                        if(event.getFullDocument() != null) {
                            after = new JSONObject(event.getFullDocument()).toMap();
                        } else {
                            after = "{}";
                        }
                        try {
                            MongoDBCollectionWatchSubscriptionEvent mdbwse = new MongoDBCollectionWatchSubscriptionEvent(event.getOperationTypeString(), before, after);
                            callback.accept(mdbwse);
                        } catch(Exception e) {
                            LOG.error("Problem with subscription event "+e);
                        }
                    }
                });

                LOG.info("out of while");
            }
        } catch (Exception e) {
            LOG.error("Problem with connector "+e);
        }

    }

    public void stop() {
        LOG.info("Deactivating Postgres inbound service");
    }


}
