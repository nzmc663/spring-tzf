package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Locale;

/**
 * @author tzf
 */
public class ViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFX =".html";

    private File templateRootDir;

    public ViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (StringUtils.isBlank(viewName)) return null;
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName+DEFAULT_TEMPLATE_SUFFX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+","/"));
        return  new View(templateFile);
    }
}
