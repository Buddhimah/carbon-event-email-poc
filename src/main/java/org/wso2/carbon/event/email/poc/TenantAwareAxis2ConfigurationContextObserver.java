package org.wso2.carbon.event.email.poc;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.event.email.poc.internal.EmailEventAdapterFactoryDataHolder;
import org.wso2.carbon.event.output.adapter.core.OutputEventAdapterConfiguration;
import org.wso2.carbon.event.publisher.core.config.EventPublisherConfiguration;
import org.wso2.carbon.event.stream.core.EventStreamConfiguration;
import org.wso2.carbon.utils.AbstractAxis2ConfigurationContextObserver;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

public class TenantAwareAxis2ConfigurationContextObserver extends AbstractAxis2ConfigurationContextObserver {

    private static final Log log = LogFactory.getLog(TenantAwareAxis2ConfigurationContextObserver.class);

    public void creatingConfigurationContext(int tenantId) {
        log.info("creating configuration context for tenant id: " + tenantId);
        String tenantDomain = "";
        EventPublisherConfiguration eventPublisherConfiguration = null;
        EventStreamConfiguration eventStreamConfiguration = null;
        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            tenantDomain = carbonContext.getTenantDomain();
            carbonContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
            carbonContext.setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);

            eventPublisherConfiguration =
                    EmailEventAdapterFactoryDataHolder.getInstance().getEventPublisherService()
                            .getActiveEventPublisherConfiguration("EmailPublisher");
            eventStreamConfiguration =
                    EmailEventAdapterFactoryDataHolder.getInstance().getCarbonEventStreamService().getEventStreamConfiguration("id_gov_notify_stream");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PrivilegedCarbonContext.endTenantFlow();

        }

        try {


            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantId);
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(tenantDomain);

            OutputEventAdapterConfiguration toAdapterConfiguration =
                    eventPublisherConfiguration.getToAdapterConfiguration();

            EmailEventAdapterFactoryDataHolder.getInstance().getCarbonEventStreamService().addEventStreamConfig(eventStreamConfiguration);
            EmailEventAdapterFactoryDataHolder.getInstance().getCarbonEventPublisherService()
             .addEventPublisherConfiguration(eventPublisherConfiguration);


            EmailEventAdapterFactoryDataHolder.getInstance().getCarbonOutputEventAdapterService().create
             (toAdapterConfiguration);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PrivilegedCarbonContext.endTenantFlow();

        }
    }

    public void createdConfigurationContext(ConfigurationContext configContext) {
        log.info("created configuration context is called");
    }

    public void terminatingConfigurationContext(ConfigurationContext configCtx) {

        EmailEventAdapterFactoryDataHolder.getInstance().getCarbonOutputEventAdapterService().destroy("email");

    }

}
