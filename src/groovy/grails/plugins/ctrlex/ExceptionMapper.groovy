package grails.plugins.ctrlex

import org.springframework.util.Assert;

/**
 * 
 * @author Kim A. Betti
 */
class ExceptionMapper {
	
	List mappings = []

	List readExceptionMapping(Closure closure) {
		closure.delegate = this
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.call()
		return mappings
	}
	
	void methodMissing(String mappingName, args) {
		Assert.isTrue (args.length == 2, "Expected two arguments for $mappingName, "
			+ "the first should be the exception and the last a closure defining the mapping")
			
		mappings << new ExceptionMapping(name: mappingName, exceptionClass: args[0], handler: args[1])
	}
	
	ExceptionMapping getMapping(Exception ex) {
		mappings.find { ExceptionMapping mapping ->
			mapping.exceptionClass.isInstance ex
		}
	}
	
}
