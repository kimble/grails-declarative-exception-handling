package grails.plugins.ctrlex

import grails.plugin.spock.UnitSpec

/**
 *
 * @author Kim A. Betti
 */
class ExceptionHandlerInvokerSpec extends UnitSpec {

	def "extract properties from closure"() {
		when:
			Map closureProperties = ExceptionHandlerInvoker.invoke(null) {
				controller = "error"
				action = "handler"
			}
		
		then:
			closureProperties?.controller == "error"
			closureProperties?.action == "handler"
	}
	
	def "closure is invoked with the exception"() {
		when:
			def arg = null
			ExceptionHandlerInvoker.invoke(new Exception("testing")) { ex ->
				arg = ex
			}
			
		then: 
			arg?.message == "testing"
	}
	
}
