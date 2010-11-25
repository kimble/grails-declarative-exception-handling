package grails.plugins.ctrlex

import grails.plugin.spock.UnitSpec;

class ClosurePropertyReaderSpec extends UnitSpec {

	
	
	def "extract properties from closure"() {
		when:
			Map closureProperties = ClosurePropertyReader.from {
				name = "Kim"
				age = 25
			}	
		
		then:
			closureProperties != null
			closureProperties.name == "Kim"
			closureProperties.age == 25
	}
	
}
