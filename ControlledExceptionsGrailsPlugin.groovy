import grails.plugins.ctrlex.ExceptionMapper;
import grails.plugins.ctrlex.ControlledExceptionHandler;
import demo.*

class ControlledExceptionsGrailsPlugin {

    def version = "0.1"
    def grailsVersion = "1.3.5 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp",
			"**/demo/**"
    ]

	def loadAfter = [ 'controllers' ] 
	
    def author = "Kim A. Betti"
    def authorEmail = "kim@developer-b.com"
    def title = "Exception mapping"
    def description = '''\\

'''

    def documentation = "http://grails.org/plugin/controlled-exceptions"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        exceptionMapper(ExceptionMapper)
		exceptionHandler(ControlledExceptionHandler) {
			exceptionMapper = ref("exceptionMapper")
			exceptionMappings = ['java.lang.Exception': '/error']
		}
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
		
		def mapper = applicationContext.getBean("exceptionMapper")
		mapper.readExceptionMapping {
			"no more milk" NoMoreMilkException, {
				controller = "shoppingList"
				action = "addItem"
				item = "milk"
			}
			
			"catch all" HumanRelatedException, {
				controller = "human"
				action = "problem"
			}
		}
		
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
	
}
