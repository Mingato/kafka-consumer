package com.abinbev.beesforce.kafkaconsumer.listener;


import com.abinbev.b2b.data.ingestion.feedback.layer.service.FeedbackLayerService;
import com.abinbev.beesforce.kafkaconsumer.constants.ApiConstants;
import com.abinbev.beesforce.kafkaconsumer.helpers.ErrorMessageTranslator;
import com.abinbev.beesforce.kafkaconsumer.helpers.GenericHelper;
import com.abinbev.beesforce.kafkaconsumer.service.MongoService;
import com.abinbev.beesforce.kafkaconsumer.service.ServiceInterface;
import com.newrelic.agent.deps.org.slf4j.MDC;
import com.newrelic.api.agent.NewRelic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class GenericKafkaListener {

    private static final String SOURCE_SYSTEM = "FORCE_GENERIC_RELAY";

    private final ServiceInterface service;

    private final GenericHelper genericHelper;

    private final FeedbackLayerService feedbackLayerService;


    public GenericKafkaListener(MongoService genericService, ServiceInterface service, GenericHelper genericHelper, FeedbackLayerService feedbackLayerService) {
        this.service = service;
        this.genericHelper = genericHelper;
        this.feedbackLayerService = feedbackLayerService;
    }


    public void consume(final ConsumerRecord<String, Object> record) {
        loadMDC(record);
        try {
            log.info(
                    "Consumed from topic: "
                            + record.topic()
                            + " offset: "
                            + record.offset()
                            + " partition: "
                            + record.partition());

            createTrackingRecord(record);

            service.storeObject(record);

            feedBackSuccess(record);
            log.info("Consumed message -> {} ", record.headers());
        } catch (Exception e) {
            feedBackError(record, e);
            throw e;
        } finally {
            feedBackFinally(record);
            log.info("sending to feedback layer.");
        }
    }


    public void consumeRetry(final ConsumerRecord<String, Object> record) {
        loadMDC(record);
        try {
            log.info(
                    "Consumed from topic: "
                            + record.topic()
                            + " offset: "
                            + record.offset()
                            + " partition: "
                            + record.partition());

            createTrackingRecord(record);

            service.storeObject(record);

            feedBackSuccess(record);
            log.info("Consumed message -> {} ", record.headers());
        } catch (Exception e) {
            feedBackError(record, e);
            throw e;
        } finally {
            feedBackFinally(record);
            log.info("sending to feedback layer!");
        }
    }


    private void feedBackSuccess(ConsumerRecord<String, Object> record) {
        feedbackLayerService.setSuccess(getRequestTraceId(record));
    }

    private void feedBackError(ConsumerRecord<String, Object> record, Exception e) {
        feedbackLayerService.setFailure(getRequestTraceId(record));
        feedbackLayerService.setErrorMessage(getRequestTraceId(record),
                ErrorMessageTranslator.getErrorMessageForLocale(e.getMessage(), Locale.US));
        feedbackLayerService.setPayload(
                getRequestTraceId(record),
                genericHelper.getObjectObjectMap(record.value()).toString());
    }

    private void feedBackFinally(ConsumerRecord<String, Object> record) {
        feedbackLayerService.publish(getRequestTraceId(record));
    }

    private String getRequestTraceId(ConsumerRecord<String, Object> record) {
        return getHeaderPropertyByName(record.value(), ApiConstants.REQUEST_TRACE_ID_HEADER);
    }


    private void loadMDC(final ConsumerRecord<String, Object> record) {
        MDC.put(ApiConstants.REQUEST_TRACE_ID_HEADER,
                getRequestTraceId(record));
        MDC.put(ApiConstants.COUNTRY_HEADER,
                getHeaderPropertyByName(record, ApiConstants.COUNTRY_HEADER));
        MDC.put(ApiConstants.VENDOR_ID_HEADER,
                getHeaderPropertyByName(record, ApiConstants.VENDOR_ID_HEADER));
        MDC.put(ApiConstants.ENTITY_HEADER,
                getHeaderPropertyByName(record, ApiConstants.ENTITY_HEADER));
        MDC.put(ApiConstants.OPERATION_HEADER,
                getHeaderPropertyByName(record, ApiConstants.OPERATION_HEADER));
        MDC.put(ApiConstants.VERSION_HEADER,
                getHeaderPropertyByName(record, ApiConstants.VERSION_HEADER));
        NewRelic.addCustomParameter(ApiConstants.REQUEST_TRACE_ID_HEADER,
                getRequestTraceId(record));
        NewRelic.addCustomParameter(ApiConstants.COUNTRY_HEADER,
                getHeaderPropertyByName(record, ApiConstants.COUNTRY_HEADER));
    }

    private void createTrackingRecord(
            final ConsumerRecord<String, Object> record) {
        feedbackLayerService.createTrackingRecord(
                getRequestTraceId(record),
                getRequestTraceId(record),
                getHeaderPropertyByName(record, ApiConstants.OPERATION_HEADER),
                getHeaderPropertyByName(record, ApiConstants.VERSION_HEADER),
                getHeaderPropertyByName(record, ApiConstants.ENTITY_HEADER),
                getHeaderPropertyByName(record, ApiConstants.COUNTRY_HEADER),
                getHeaderPropertyByName(record, ApiConstants.VENDOR_ID_HEADER),
                SOURCE_SYSTEM);
    }



    private String getHeaderPropertyByName(Object object, String propertyName) {
        var objectMap = genericHelper.getObjectObjectMap(object);

        return ((Map<Object, Object>)objectMap.get("headers")).get(propertyName).toString();
    }

}
