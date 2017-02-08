package org.springframework.web.servlet.view.velocity;

import org.apache.velocity.context.Context;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * @author langyi
 * @date 2017/2/7.
 */
public class LikVelocityLayoutViewResolver extends VelocityLayoutViewResolver {

    public String merge(String viewName, ModelMap modelMap) throws Exception {
        VelocityLayoutView view = (VelocityLayoutView) createView(viewName, Locale.getDefault());
        Context velocityContext = view.createVelocityContext(modelMap);

        final StringWriter stringWriter = new StringWriter();
        view.doRender(velocityContext, new MockHttpServletResponse(){
            @Override
            public PrintWriter getWriter() throws UnsupportedEncodingException {
                return new PrintWriter(stringWriter);
            }
        });
        return stringWriter.toString();
    }
}
