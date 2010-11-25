package grails.plugins.ctrlex

import demo.NoMoreMilkException;
import grails.plugin.spock.UnitSpec;

class ExceptionMapperSpec extends UnitSpec {

	def "read single mapping"() {
		when:
			List mappings = new ExceptionMapper().readExceptionMapping {
				"no more milk" NoMoreMilkException, { ex ->
					controller = "shoppinglist"
					action = "addItem"
					item = "milk"
				}
			}	
		
		then:
			mappings.size() == 1
			ExceptionMapping missingMilk = mappings.get(0)
			missingMilk.name == "no more milk"
			missingMilk.exceptionClass == NoMoreMilkException
			missingMilk.handler != null
	}
	
}
