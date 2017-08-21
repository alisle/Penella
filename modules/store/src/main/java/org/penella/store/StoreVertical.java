package org.penella.store;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.penella.Errors;
import org.penella.store.messages.AddString;
import org.penella.store.messages.AddStringResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by alisle on 8/14/17.
 */
public class StoreVertical extends AbstractVerticle {
    //private final IStore store;
    private IStore store;


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("STORE_ADD_STRING").handler(msg -> {
            try {
                AddString addString = (AddString)msg.body();

                final String value = addString.getValue();
                final long hash = store.add(addString.getValue());

                msg.reply(new AddStringResponse(hash, value));

            } catch (ExecutionException exception) {
                msg.fail(Errors.STORE_ADD_EXECUTION_ERROR.getErrorno(), Errors.STORE_ADD_EXECUTION_ERROR.getDescription());
            } catch (IOException exception) {
                msg.fail(Errors.STORE_ADD_IO_ERROR.getErrorno(), Errors.STORE_ADD_IO_ERROR.getDescription());
            }
        });

        startFuture.complete();

        return;
    }
}
