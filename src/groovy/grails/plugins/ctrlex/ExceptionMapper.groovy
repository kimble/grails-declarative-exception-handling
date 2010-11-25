package grails.plugins.ctrlex

import org.springframework.util.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Kim A. Betti
 */
class ExceptionMapper {
	
	private static final Log log = LogFactory.getLog(ExceptionMapper)
	
	List mappings = []

	List readExceptionMapping(Closure closure) {
		closure.delegate = this
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.call()
		return mappings
	}
	
	void resetMappings() {
		log.debug "Resetting exception mappings"
		mappings = []
	}
	
	void methodMissing(String mappingName, args) {
		Assert.isTrue (args.length == 2, "Expected two arguments for $mappingName, "
			+ "the first should be the exception and the last a closure defining the mapping")
		
		log.debug "Adding exception mapping $mappingName"
		mappings << new ExceptionMapping(name: mappingName, exceptionClass: args[0], handler: args[1])
	}
	
	ExceptionMapping getMapping(Exception ex) {
		mappings.find { ExceptionMapping mapping ->
			mapping.exceptionClass.isInstance ex
		}
	}
	
}