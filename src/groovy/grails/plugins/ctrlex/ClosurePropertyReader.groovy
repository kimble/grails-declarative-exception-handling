package grails.plugins.ctrlex

import groovy.lang.Closure;

class ClosurePropertyReader {
	
	private Map _closureProperties = [:]
	
	protected ClosurePropertyReader(Closure closure) {
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure.delegate = this
		closure.call()
	}
	
	public Map getClosureProperties() {
		return _closureProperties
	}

	static Map<String, Object> from(Closure closure) {
		ClosurePropertyReader closureReader = new ClosurePropertyReader(closure)
		return closureReader.getClosureProperties()
	}
	
	void propertyMissing(String name, value) {
		_closureProperties[name] = value
	}
	
}
