package grails.plugins.ctrlex

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 
 * @author Kim A. Betti
 */
class ExceptionHandlerPostProcessor implements BeanPostProcessor {

	private static final Log log = LogFactory.getLog(ExceptionHandlerPostProcessor)
	
	ControlledExceptionHandler controlledExceptionHandler
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
		if (name == "exceptionHandler") {
			log.debug "Replacing exceptionHandler"
			controlledExceptionHandler.grailsExceptionResolver = bean
			bean = controlledExceptionHandler
		} 
			
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
		return bean;
	}

}
