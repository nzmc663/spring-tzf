package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import com.gupaoedu.vip.spring.framework.annotation.Controller;
import com.gupaoedu.vip.spring.framework.annotation.RequestMapping;
import com.gupaoedu.vip.spring.framework.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tzf
 */
@Slf4j
public class DispatchServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private ApplicationContext context;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、Spring上下文初始化,启动Spring容器
        this.context = new ApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        //2、初始化Spring MVC九大组件
        initStrategies(context);
    }

    /**
     * 初始化策略--九大组件
     */
    protected void initStrategies(ApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);

        //初始化本地语言环境
        initLocaleResolver(context);

        //初始化模板处理器
        initThemeResolver(context);

        //handlerMapping，必须实现
        initHandlerMappings(context);

        //初始化参数适配器，必须实现
        initHandlerAdapters(context);

        //初始化异常拦截器
        initHandlerExceptionResolvers(context);

        //初始化视图预处理器
        initRequestToViewNameTranslator(context);

        //初始化视图转换器，必须实现
        initViewResolvers(context);

        //参数缓存器
        initFlashMapManager(context);
    }

    /**
     * 1、初始化多文件上传的组件
     */
    private void initMultipartResolver(ApplicationContext context) {
    }

    /**
     * 2、初始化本地语言环境
     */
    private void initLocaleResolver(ApplicationContext context) {
    }

    /**
     * 3、初始化模板处理器
     */
    private void initThemeResolver(ApplicationContext context) {
    }

    /**
     * 4、初始化地址与Handler关系映射
     */
    private void initHandlerMappings(ApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) continue;
                String baseUrl = "";
                //获取Controller 类级别的url配置
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value();
                }
                //获取Controller 方法级别的url配置
                for (Method method :  clazz.getMethods()){
                    //没有加RequestMapping注解的方法直接忽略
                    if (!method.isAnnotationPresent(RequestMapping.class)) continue;
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    //错误：//demo//query 正确：/demo/query
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*",".*"))
                            .replaceAll("/+","/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new HandlerMapping(pattern,controller,method));
                    log.info("Mapped " + regex + "," + method.getDeclaringClass()+"#"+method.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 5、初始化参数适配器
     */
    private void initHandlerAdapters(ApplicationContext context) {
        //把一个requet请求变成一个handler，参数都是字符串的，自动配到handler中的形参
        //可想而知，他要拿到HandlerMapping才能干活
        //就意味着，有几个HandlerMapping就有几个HandlerAdapter
        for (HandlerMapping handlerMapping : this.handlerMappings){
            this.handlerAdapters.put(handlerMapping,new HandlerAdapter());
        }
    }

    /**
     * 6、初始化异常拦截器
     */
    private void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    /**
     * 7、初始化视图预处理器
     */
    private void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    /**
     * 8、初始化视图转换器
     */
    private void initViewResolvers(ApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRooDir = new File(templateRootPath);
        String[] templates = templateRooDir.list();
        for (int i = 0; i < templates.length; i++){
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }
    }

    /**
     * 9、参数缓存器
     */
    private void initFlashMapManager(ApplicationContext context) {
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1、通过从request中拿到URL，去匹配一个HandlerMapping
        HandlerMapping handler = getHandler(req);
        if (handler == null){
            processDispatchResult(req,resp,new ModelAndView("404"));
            return;
        }

        //2、准备调用前的参数
        HandlerAdapter adapter = getHandlerAdapter(handler);

        //3、真正的调用方法,返回ModelAndView存储了要穿页面上值，和页面模板的名称
        ModelAndView modelAndView = adapter.handle(req,resp,handler);

        //这一步才是真正的输出
        processDispatchResult(req, resp, modelAndView);
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) return null;
        HandlerAdapter adapter = this.handlerAdapters.get(handler);
        return adapter.support(handler) ? adapter : null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null || this.viewResolvers.isEmpty()) return;

        for (ViewResolver resolver : this.viewResolvers){
           View view = resolver.resolveViewName(modelAndView.getViewName(),null);
           view.render(modelAndView.getModel(),req,resp);
           return;
        }
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) return null;
        String url = req.getRequestURI();
        String contextPath =req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");

        for (HandlerMapping handler : this.handlerMappings){
            Matcher matcher = handler.getPattern().matcher(url);
            if (matcher.matches()){
                return handler;
            }
        }
        return null;
    }


}
