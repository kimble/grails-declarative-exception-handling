package grails.plugins.ctrlex

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.groovy.grails.web.errors.GrailsExceptionResolver
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingData;
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingInfo;
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Kim A. Betti
 */
class ControlledExceptionHandler extends GrailsExceptionResolver {
	
	ExceptionMapper exceptionMapper
	ServletContext servletContext
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, 
		HttpServletResponse response, Object handler, Exception ex) {
		
		ex = GrailsExceptionResolver.getRootCause(ex)
		ExceptionMapping mapping = exceptionMapper.getMapping(ex)
		
		return (mapping != null 
			? resolveExceptionUsing(mapping, request, response, handler, ex) 
			: super.resolveException(request, response, handler, ex))
	}
	
	private ModelAndView resolveExceptionUsing(ExceptionMapping mapping, 
		HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		
		Map properties = ClosurePropertyReader.from(mapping.handler)
		String controllerName = properties.remove("controller")
		String actionName = properties.remove("action")
		
		Map model = [ exception: ex ]
		UrlMappingInfo info = new DefaultUrlMappingInfo(controllerName, actionName, null, properties, null, servletContext)
		WebUtils.forwardRequestForUrlMappingInfo(request, response, info, model, true)

		return new ModelAndView()
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext)
		this.servletContext = servletContext
	}	
	
}