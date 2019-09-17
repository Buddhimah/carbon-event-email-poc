package org.wso2.carbon.event.email.poc.internal;

import org.wso2.carbon.event.output.adapter.core.internal.CarbonOutputEventAdapterService;
import org.wso2.carbon.event.publisher.core.EventPublisherService;
import org.wso2.carbon.event.publisher.core.internal.CarbonEventPublisherService;

public class EmailEventAdapterFactoryDataHolder {

    private static volatile EmailEventAdapterFactoryDataHolder instance = new EmailEventAdapterFactoryDataHolder();

    private  EmailEventAdapterFactoryDataHolder(){


    }

    public static EmailEventAdapterFactoryDataHolder getInstance() {
        return instance;
    }

    private EventPublisherService eventPublisherService = null;
    private CarbonOutputEventAdapterService carbonOutputEventAdapterService = null;
    private CarbonEventPublisherService carbonEventPublisherService = null;

    public void setEventPublisherService(EventPublisherService eventPublisherService) {
        this.eventPublisherService = eventPublisherService;
    }

    public EventPublisherService getEventPublisherService() {
        return eventPublisherService;
    }

    public CarbonOutputEventAdapterService getCarbonOutputEventAdapterService() {
        return carbonOutputEventAdapterService;
    }

    public void setCarbonOutputEventAdapterService(CarbonOutputEventAdapterService carbonOutputEventAdapterService) {
        this.carbonOutputEventAdapterService = carbonOutputEventAdapterService;
    }

    public CarbonEventPublisherService getCarbonEventPublisherService() {
        return carbonEventPublisherService;
    }

    public void setCarbonEventPublisherService(CarbonEventPublisherService carbonEventPublisherService) {
        this.carbonEventPublisherService = carbonEventPublisherService;
    }
}
