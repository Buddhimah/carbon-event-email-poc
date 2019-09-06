package org.wso2.carbon.event.email.poc.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.event.email.poc.TenantAwareAxis2ConfigurationContextObserver;
import org.wso2.carbon.event.email.poc.TenantEmailEventAdapterFactory;
import org.wso2.carbon.event.publisher.core.EventPublisherService;
import org.wso2.carbon.utils.AbstractAxis2ConfigurationContextObserver;
import org.wso2.carbon.utils.Axis2ConfigurationContextObserver;

/**
 * @scr.component component.name="org.wso2.carbon.event.email.poc" immediate="true"
 * @scr.reference name="eventPublisherService.service"
 * interface="org.wso2.carbon.event.publisher.core.EventPublisherService" cardinality="1..1"
 * policy="dynamic" bind="setEventPublisherService" unbind="unsetEventPublisherService"
 */

public class EmailEventAdapterFactoryServiceDS extends AbstractAxis2ConfigurationContextObserver {

    private static final Log log = LogFactory.getLog(EmailEventAdapterFactoryServiceDS.class);

    /**
     * initialize the email service here service here.
     *
     * @param context
     */
    protected void activate(ComponentContext context) {

        try {
            TenantEmailEventAdapterFactory tenantEmailEventAdapterFactory = new TenantEmailEventAdapterFactory();
            context.getBundleContext().registerService(TenantEmailEventAdapterFactory.class.getName(),
                    tenantEmailEventAdapterFactory, null);

            TenantAwareAxis2ConfigurationContextObserver tenantAwareAxis2ConfigurationContextObserver =
                    new TenantAwareAxis2ConfigurationContextObserver();

            context.getBundleContext().registerService(Axis2ConfigurationContextObserver.class.getName(),
                    tenantAwareAxis2ConfigurationContextObserver, null);
            if (log.isDebugEnabled()) {
                log.debug("Successfully deployed the  org.wso2.carbon.event.email.pocservice");
            }
        } catch (RuntimeException e) {
            log.error("Can not create the org.wso2.carbon.event.email.poc service ", e);
        }
    }

    protected void setEventPublisherService(EventPublisherService eventPublisherService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the Event Publisher Service");
        }
        EmailEventAdapterFactoryDataHolder.getInstance().setEventPublisherService(eventPublisherService);
    }

    protected void unsetEventPublisherService(EventPublisherService eventPublisherService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Event Publisher Service");
        }
        EmailEventAdapterFactoryDataHolder.getInstance().setEventPublisherService(null);
    }

}
