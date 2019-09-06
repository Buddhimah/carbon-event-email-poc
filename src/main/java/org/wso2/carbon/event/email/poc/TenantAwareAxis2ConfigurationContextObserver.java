package org.wso2.carbon.event.email.poc;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.event.email.poc.internal.EmailEventAdapterFactoryDataHolder;
import org.wso2.carbon.event.output.adapter.core.OutputEventAdapterConfiguration;
import org.wso2.carbon.event.publisher.core.exception.EventPublisherConfigurationException;
import org.wso2.carbon.utils.AbstractAxis2ConfigurationContextObserver;

public class TenantAwareAxis2ConfigurationContextObserver extends AbstractAxis2ConfigurationContextObserver {

    private static final Log log = LogFactory.getLog(TenantAwareAxis2ConfigurationContextObserver.class);

    public void creatingConfigurationContext(int tenantId) {
        log.info("creating configuration context for tenant id: " + tenantId);
        try {

            OutputEventAdapterConfiguration toAdapterConfiguration =
                    EmailEventAdapterFactoryDataHolder.getInstance().getEventPublisherService()
                    .getActiveEventPublisherConfiguration("EmailPublisher", -1234).getToAdapterConfiguration();

            EmailEventAdapterFactoryDataHolder.getInstance().getCarbonOutputEventAdapterService().create(toAdapterConfiguration);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createdConfigurationContext(ConfigurationContext configContext) {
        log.info("created configuration context is called");
    }

    public void terminatingConfigurationContext(ConfigurationContext configCtx) {

        EmailEventAdapterFactoryDataHolder.getInstance().getCarbonOutputEventAdapterService().destroy("email");

    }

}
