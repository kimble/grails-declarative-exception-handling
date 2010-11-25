import grails.plugins.ctrlex.ExceptionMapper
import grails.plugins.ctrlex.ControlledExceptionHandler
import org.codehaus.groovy.grails.commons.UrlMappingsArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.commons.GrailsApplication

import demo.*

class ControlledExceptionsGrailsPlugin {

    def version = "0.1"
    def grailsVersion = "1.3.5 > *"
    def dependsOn = [:]
    def pluginExcludes = [
		"grails-app/views/error.gsp",
		"**/demo/**"
    ]

	def loadAfter = [ "controllers", "urlMappings" ] 
	
	def watchedResources = [ 
		"file:./grails-app/conf/*UrlMappings.groovy",
		"file:./grails-app/conf/**/*UrlMappings.groovy",
		"file:./plugins/*/grails-app/conf/*UrlMappings.groovy",
		"file:./plugins/*/grails-app/conf/**/*UrlMappings.groovy"
	]
	
    def author = "Kim A. Betti"
    def authorEmail = "kim@developer-b.com"
    def title = "Exception mapping"
    def description = 'Declerative exception mapping'

    def documentation = "http://grails.org/plugin/controlled-exceptions"

    def doWithSpring = {
        exceptionMapper(ExceptionMapper)
		exceptionHandler(ControlledExceptionHandler) {
			exceptionMapper = ref("exceptionMapper")
			exceptionMappings = ['java.lang.Exception': '/error']
		}
    }

    def doWithApplicationContext = { applicationContext ->
		GrailsApplication grailsApplication = applicationContext.getBean("grailsApplication")
		ExceptionMapper exceptionMapper = applicationContext.getBean("exceptionMapper")
		readUrlMappings(grailsApplication, exceptionMapper)
    }

    def onChange = { event ->
        if (application.isArtefactOfType(UrlMappingsArtefactHandler.TYPE, event.source)) {
			ExceptionMapper mapper = event.ctx.getBean("exceptionMapper")
			GrailsApplication grailsApplication = event.ctx.getBean("grailsApplication")
			
			mapper.resetMappings()
			readUrlMappings(grailsApplication, mapper)
		}
    }
	
	private void readUrlMappings(GrailsApplication grailsApplication, ExceptionMapper mapper) {
		grailsApplication.getArtefacts(UrlMappingsArtefactHandler.TYPE).each { GrailsClass gc ->
			Class<?> urlMappingClass = gc.getClazz()
			if (GrailsClassUtils.isStaticProperty(urlMappingClass, "exceptionMappings"))
				mapper.readExceptionMapping urlMappingClass.exceptionMappings
		}
	}

	def doWithDynamicMethods = { ctx -> }
	def onConfigChange = { event -> }
	def doWithWebDescriptor = { xml -> }
	
}
