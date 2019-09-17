package org.wso2.carbon.event.email.poc;

import org.apache.axis2.transport.mail.MailConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.event.output.adapter.core.OutputEventAdapterConfiguration;
import org.wso2.carbon.event.output.adapter.core.exception.ConnectionUnavailableException;
import org.wso2.carbon.event.output.adapter.email.EmailEventAdapter;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class TenantAwareEmailEventAdapter extends EmailEventAdapter {
    private static final Log log = LogFactory.getLog(TenantAwareEmailEventAdapter.class);
    private static ThreadPoolExecutor threadPoolExecutor;
    private static Session session;
    private OutputEventAdapterConfiguration eventAdapterConfiguration;
    private Map<String, String> globalProperties;
    private int tenantId;
    /**
     * Default from address for outgoing messages.
     */
    private InternetAddress smtpFromAddress = null;
    public TenantAwareEmailEventAdapter(OutputEventAdapterConfiguration eventAdapterConfiguration,
            Map<String, String> globalProperties) {
        super(eventAdapterConfiguration, globalProperties);
    }

    public void connect() throws ConnectionUnavailableException {

        if (session == null) {

            /**
             * Default SMTP properties for outgoing messages.
             */
            String smtpFrom;
            String smtpHost;
            String smtpPort;


            /**
             *  Default from username and password for outgoing messages.
             */
            final String smtpUsername;
            final String smtpPassword;


            // initialize SMTP session.
            Properties props = new Properties();
            props.putAll(globalProperties);

            //Verifying default SMTP properties of the SMTP server.

            smtpFrom = props.getProperty(MailConstants.MAIL_SMTP_FROM);
            smtpHost = props.getProperty("mail.smtp.host");
            smtpPort = props.getProperty("mail.smtp.port");

            if (smtpFrom == null) {
                String msg = "failed to connect to the mail server due to null smtpFrom value";
                throw new ConnectionUnavailableException("The adapter " +
                        eventAdapterConfiguration.getName() + " " + msg);

            }

            if (smtpHost == null) {
                String msg = "failed to connect to the mail server due to null smtpHost value";
                throw new ConnectionUnavailableException
                        ("The adapter " + eventAdapterConfiguration.getName() + " " + msg);
            }

            if (smtpPort == null) {
                String msg = "failed to connect to the mail server due to null smtpPort value";
                throw new ConnectionUnavailableException
                        ("The adapter " + eventAdapterConfiguration.getName() + " " + msg);
            }


            try {
                smtpFromAddress = new InternetAddress(smtpFrom);
            } catch (AddressException e) {
                log.error("Error in retrieving smtp address : " +
                        smtpFrom, e);
                String msg = "failed to connect to the mail server due to error in retrieving " +
                        "smtp from address";
                throw new ConnectionUnavailableException
                        ("The adapter " + eventAdapterConfiguration.getName() + " " + msg, e);
            }

            //Retrieving username and password of SMTP server.
            smtpUsername = props.getProperty(MailConstants.MAIL_SMTP_USERNAME);
            smtpPassword = props.getProperty(MailConstants.MAIL_SMTP_PASSWORD);


            //initializing SMTP server to create session object.
            if (smtpUsername != null && smtpPassword != null && !smtpUsername.isEmpty() && !smtpPassword.isEmpty()) {
                session = Session.getInstance(props, new Authenticator() {
                    public PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUsername, smtpPassword);
                    }
                });
            } else {
                session = Session.getInstance(props);
                log.info("Connecting adapter " + eventAdapterConfiguration.getName() + "without user authentication for tenant " + tenantId);
            }
        }
    }
}
