package cloud.fengdu.ticketing.eventregistry;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.nats.client.Connection;
import io.nats.client.JetStreamApiException;
import io.nats.client.JetStreamManagement;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;

public class NatsJsUtils {

    private static final Log logger = LogFactory.getLog(NatsJsUtils.class);
    
    // ----------------------------------------------------------------------------------------------------
    // STREAM INFO / CREATE / UPDATE
    // ----------------------------------------------------------------------------------------------------
    public static StreamInfo getStreamInfoOrNullWhenNotExist(JetStreamManagement jsm, String streamName) throws IOException, JetStreamApiException {
        try {
            return jsm.getStreamInfo(streamName);
        }
        catch (JetStreamApiException jsae) {
            if (jsae.getErrorCode() == 404) {
                return null;
            }
            throw jsae;
        }
    } 

    public static StreamInfo createStream(JetStreamManagement jsm, String streamName, StorageType storageType, String[] subjects) throws IOException, JetStreamApiException {
        // Create a stream, here will use a file storage type, and one subject,
        // the passed subject.
        StreamConfiguration sc = StreamConfiguration.builder()
            .name(streamName)
            .storageType(storageType)
            .subjects(subjects)
            .build();

        // Add or use an existing stream.
        StreamInfo si = jsm.addStream(sc);
        logger.info(String.format("Created stream '%s' with subject(s) %s\n",
            streamName, si.getConfiguration().getSubjects()));

        return si;
    }

    public static StreamInfo createStream(JetStreamManagement jsm, String streamName, String... subjects)
            throws IOException, JetStreamApiException {
        return createStream(jsm, streamName, StorageType.Memory, subjects);
    }

    public static StreamInfo createStream(Connection nc, String stream, String... subjects) throws IOException, JetStreamApiException {
        return createStream(nc.jetStreamManagement(), stream, StorageType.Memory, subjects);
    }


    public static StreamInfo createStreamOrUpdateSubjects(JetStreamManagement jsm, String streamName, StorageType storageType, String... subjects)
            throws IOException, JetStreamApiException {

        StreamInfo si = getStreamInfoOrNullWhenNotExist(jsm, streamName);
        if (si == null) {
            return createStream(jsm, streamName, storageType, subjects);
        }

        // check to see if the configuration has all the subject we want
        StreamConfiguration sc = si.getConfiguration();
        boolean needToUpdate = false;
        for (String sub : subjects) {
            if (!sc.getSubjects().contains(sub)) {
                needToUpdate = true;
                sc.getSubjects().add(sub);
            }
        }

        if (needToUpdate) {
            sc = StreamConfiguration.builder(sc).subjects(sc.getSubjects()).build();
            si = jsm.updateStream(sc);
            logger.info(String.format("Existing stream '%s' was updated, has subject(s) %s\n",
                streamName, si.getConfiguration().getSubjects()));
        }
        else
        {
            logger.info(String.format("Existing stream '%s' already contained subject(s) %s\n",
                streamName, si.getConfiguration().getSubjects()));
        }

        return si;
    }

    public static StreamInfo createStreamOrUpdateSubjects(JetStreamManagement jsm, String streamName, String... subjects)
        throws IOException, JetStreamApiException {
        return createStreamOrUpdateSubjects(jsm, streamName, StorageType.Memory, subjects);
    }

    public static StreamInfo createStreamOrUpdateSubjects(Connection nc, String stream, String... subjects) throws IOException, JetStreamApiException {
        return createStreamOrUpdateSubjects(nc.jetStreamManagement(), stream, StorageType.Memory, subjects);
    }


    
}

