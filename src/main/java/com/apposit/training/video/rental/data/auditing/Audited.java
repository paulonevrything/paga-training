package com.apposit.training.video.rental.data.auditing;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * 
 * @author Eric Chijioke
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface Audited {

}
