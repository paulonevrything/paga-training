package com.apposit.training.video.rental.data.sql;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

import java.sql.SQLException;

/**
 * This class pauses execution of the current thread until a connection to the configured database can be successfully established.
 * Intended for configuration in a system to ensure that distributed database connections are started up before other system resources
 * attempt to use the connection.
 * 
 * @author Eric Chijioke
 *
 */
public class DatabaseConnectionSequencer implements BeanFactoryPostProcessor, Ordered, InitializingBean {

	private static final int RETRY_TIMEOUT_SECONDS_DEFAULT = 5;

	final Log logger = LogFactory.getLog(DatabaseConnectionSequencer.class);
	
	private int retryTimeoutSeconds = RETRY_TIMEOUT_SECONDS_DEFAULT;
	
	private String jdbcDriverClassName;
	private String jdbcUrl;
	private String jdbcCatalog;
	private String jdbcUsername;
	private String jdbcPassword;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(jdbcDriverClassName);
        ds.setUrl(jdbcUrl);
        ds.setDefaultCatalog(jdbcCatalog);
        ds.setUsername(jdbcUsername);
        ds.setPassword(jdbcPassword);
        
        while(true){
        	
        	try {
        		
        		try {
        			
        			ds.getConnection();
        			
        			break;
        			
        		} catch(SQLException se) {

        			//failed to connect. retry....
        			logger.error("Failed to connect to database: " + jdbcCatalog + ".  Retrying in " + retryTimeoutSeconds + " seconds..."
        					+ se.getMessage());
        		}
        		
        		Thread.sleep(retryTimeoutSeconds * DateUtils.MILLIS_PER_SECOND);
        		
        	} catch(InterruptedException e){}
        }
	}
			
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException {
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setJdbcCatalog(String jdbcCatalog) {
		this.jdbcCatalog = jdbcCatalog;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
	
	public void setRetryTimeoutSeconds(int retryTimeoutSeconds) {
		this.retryTimeoutSeconds = retryTimeoutSeconds;
	}
}
