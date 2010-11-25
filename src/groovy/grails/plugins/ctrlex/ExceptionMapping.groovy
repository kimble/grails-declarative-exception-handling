package grails.plugins.ctrlex

/**
 * 
 * @author Kim A. Betti
 */
class ExceptionMapping {
	
	String name
	Class<? extends Exception> exceptionClass
	Closure handler
	
	@Override
	String toString() {
		"ExceptionMapping $name for ${exceptionClass.simpleName}"	
	}
	
}
