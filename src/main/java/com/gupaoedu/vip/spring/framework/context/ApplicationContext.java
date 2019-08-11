package com.gupaoedu.vip.spring.framework.context;

import com.gupaoedu.vip.spring.framework.annotation.Autowired;
import com.gupaoedu.vip.spring.framework.annotation.Controller;
import com.gupaoedu.vip.spring.framework.annotation.Service;
import com.gupaoedu.vip.spring.framework.aop.AopProxy;
import com.gupaoedu.vip.spring.framework.aop.CglibAopProxy;
import com.gupaoedu.vip.spring.framework.aop.JdkDynamicAopProxy;
import com.gupaoedu.vip.spring.framework.aop.config.AopConfig;
import com.gupaoedu.vip.spring.framework.aop.support.AdvisedSupport;
import com.gupaoedu.vip.spring.framework.beans.BeanWrapper;
import com.gupaoedu.vip.spring.framework.beans.config.BeanDefinition;
import com.gupaoedu.vip.spring.framework.beans.config.BeanPostProcessor;
import com.gupaoedu.vip.spring.framework.beans.support.BeanDefinitionReader;
import com.gupaoedu.vip.spring.framework.beans.support.DefaultListableBeanFactory;
import com.gupaoedu.vip.spring.framework.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC容器实现,全局上下文实现
 *
 * @author tzf
 */
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {
    /**
     * 配置文件路径
     */
    private String[] configLocations;
    /**
     * 读取配置文件
     */
    private BeanDefinitionReader reader;
    /**
     * 单例实例IOC容器
     */
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(256);
    /**
     * BeanWarpper容器
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>(256);

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        //1、定位，定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);
        //2、加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //3、注册，把配置信息放到容器里面(伪IOC容器)
        doRegistBeanDefinition(beanDefinitions);
        //4、把不是延时加载的类,提前初始化
        doAutowrited();
    }

    private void doRegistBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The \"" + beanDefinition.getFactoryBeanName() + "\" is exist!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
        //到这里为止，容器初始化完毕
    }

    private void doAutowrited() {
        for (Map.Entry<String, BeanDefinition> entry : super.beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            if (!entry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    //依赖注入，从这里开始，通过读取BeanDefinition中的信息
    //然后，通过反射机制创建一个实例并返回
    //Spring做法是，不会把最原始的对象放出去，会用一个BeanWrapper来进行一次包装
    //装饰器模式：
    //1、保留原来的OOP关系
    //2、我需要对它进行扩展，增强（为了以后AOP打基础）
    @Override
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;

        //TODO 这个逻辑还不严谨，自己可以去参考Spring源码
        //工厂模式 + 策略模式
        BeanPostProcessor postProcessor = new BeanPostProcessor();
        postProcessor.postProcessBeforeInitialization(instance, beanName);
        instance = initBean(beanName, beanDefinition);

        //3、把这个对象封装到BeanWrapper中
        BeanWrapper beanWrapper = new BeanWrapper(instance);

        //4、把BeanWrapper存到IOC容器里面
        //class A{ B b;}
        //class B{ A a;}
        //先有鸡还是先有蛋的问题，一个方法是搞不定的，要分两次
        this.factoryBeanInstanceCache.put(beanName,beanWrapper);
        postProcessor.postProcessAfterInitialization(instance,beanName);

        //5、依赖注入
        populateBean(beanName,beanDefinition,beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWarppedInstance();
    }

    private Object initBean(String beanName, BeanDefinition beanDefinition) {
        //1、拿到要实例化的对象的类名
        String className = beanDefinition.getBeanClassName();
        //2、反射实例化，得到一个对象
        Object instance = null;
        try {
            //假设默认就是单例,细节暂且不考虑，先把主线拉通
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                AdvisedSupport config = initAopConfig();
                config.setTarget(instance);
                config.setTargetClass(clazz);
                //如果符合PointCut的规则的话,创建代理对象
                if (config.pointCutMatch()) {
                    instance = createProxy(config).getProxy();
                }
                this.factoryBeanObjectCache.put(className,instance);
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
    /**
     * 解析AOP配置
     */
    private AdvisedSupport initAopConfig() {
        AopConfig aopConfig = new AopConfig();
        Properties config = this.reader.getConfig();
        aopConfig.setPointCut(config.getProperty("pointCut"));
        aopConfig.setAspectClass(config.getProperty("aspectClass"));
        aopConfig.setAspectBefore(config.getProperty("aspectBefore"));
        aopConfig.setAspectAfter(config.getProperty("aspectAfter"));
        aopConfig.setAspectRound(config.getProperty("aspectRound"));
        aopConfig.setAspectAfterThrow(config.getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(config.getProperty("aspectAfterThrowingName"));
        return new AdvisedSupport(aopConfig);
    }

    private AopProxy createProxy(AdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if (targetClass.isInterface()|| targetClass.getInterfaces().length>0){
            return new JdkDynamicAopProxy(config);
        }
        return new CglibAopProxy(config);
    }
    /**
     * 依赖注入
     */
    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWarppedInstance();

        Class<?> clazz = beanWrapper.getWarppedClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(Controller.class)||clazz.isAnnotationPresent(Service.class))) return;

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            if (!field.isAnnotationPresent(Autowired.class)) continue;
            Autowired autowired = field.getAnnotation(Autowired.class);
            //通过名称进行注入，没有名称则通过类型进行注入
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            //强制访问
            field.setAccessible(true);
            try {
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) continue;
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWarppedInstance());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }


}
