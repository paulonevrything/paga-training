package com.apposit.training.video.rental.data;

import com.apposit.training.video.rental.exception.ExceptionCode;
import com.apposit.training.video.rental.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

/**
 * This Aspect will cause methods to retry if there is a notion of a deadlock.
 *
 * <emf>Note that the aspect implements the Ordered interface so we can set the
 * precedence of the aspect higher than the transaction advice (we want a fresh
 * transaction each time we retry).</emf>
 *
 * @author Eric Chijioke -- modified from version provided by Jelle Victoor
 */
@Aspect
public class DeadlockRetryAspect implements Ordered {
	
	final Log log = LogFactory.getLog(DeadlockRetryAspect.class);
	
    private int order = -1;
     
    /**
     * Deadlock retry. The aspect applies to every service method with the
     * annotation {@link DeadLockRetry}
     *
     * @param pjp the joinpoint
     * @param deadLockRetry  the concurrency retry
     * @return
     *
     * @throws Throwable the throwable
     */
    @Around(value = "@annotation(deadLockRetry)", argNames = "deadLockRetry")
    public Object concurrencyRetry(final ProceedingJoinPoint pjp, final DeadlockRetry deadLockRetry) throws Throwable {
        
    	final Integer retryCount = deadLockRetry.retryCount();
        Integer deadlockCounter = 0;
        Object result = null;
       
        while (deadlockCounter < retryCount) {
            try {
            	
            	
                result = pjp.proceed();
                break;
            } catch (final FatalException exception) {
                deadlockCounter = handleException(exception, deadlockCounter, retryCount);
            }
        }
        return result;
    }
 
    /**
     * handles the persistence exception. Performs checks to see if the
     * exception is a deadlock and check the retry count.
     *
     * @param exception
     *            the persistence exception that could be a deadlock
     * @param deadlockCounter
     *            the counter of occurred deadlocks
     * @param retryCount
     *            the max retry count
     * @return the deadlockCounter that is incremented
     */
    private Integer handleException(final FatalException exception, Integer deadlockCounter, final Integer retryCount) {
       
    	if (ExceptionCode.PERSISTENCE_DEADLOCK_ERROR == exception.getCode()) {
           
        	if(log.isInfoEnabled()) {
        		log.info("Deadlock caught - retrying transaction " + retryCount + " times. Attempt " + (deadlockCounter+1), exception);
        	}
        	
        	deadlockCounter++;
                    	
            if (deadlockCounter == (retryCount - 1)) {
                
            	log.error("Deadlock caught - retries failed", exception);
            	
                throw exception;
            }
            
        } else {
        	
            throw exception;
        }
    	
        return deadlockCounter;
    }
 
 
    /** {@inheritDoc} */
    public int getOrder() {
        return order;
    }
 
    /**
     * Sets the order.
     *
     * @param order
     *            the order to set
     */
    public void setOrder(final int order) {
        this.order = order;
    }
}