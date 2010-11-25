package grails.plugins.ctrlex

class ExceptionMapping {
	
	String name
	Class<? extends Exception> exceptionClass
	Closure handler
	
	@Override
	String toString() {
		"ExceptionMapping $name for ${exceptionClass.simpleName}"	
	}
	
}
