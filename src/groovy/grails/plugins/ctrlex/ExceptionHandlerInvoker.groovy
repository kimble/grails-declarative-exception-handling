package grails.plugins.ctrlex

import groovy.lang.Closure;

/**
 * 
 * @author Kim A. Betti
 */
class ExceptionHandlerInvoker {
	
	private Map _closureProperties = [:]
	
	protected ExceptionHandlerInvoker(Exception ex, Closure closure) {
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure.delegate = this
		closure.call(ex)
	}
	
	public Map getClosureProperties() {
		return _closureProperties
	}

	static Map<String, Object> invoke(Exception ex, Closure closure) {
		ExceptionHandlerInvoker closureReader = new ExceptionHandlerInvoker(ex, closure)
		return closureReader.getClosureProperties()
	}
	
	void propertyMissing(String name, value) {
		_closureProperties[name] = value
	}
	
}
