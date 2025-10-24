package io.github.alirostom1.logismart;

import jakarta.servlet.FilterRegistration;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {
    public static void main(String[] args) throws LifecycleException {
//        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);
            tomcat.getConnector();
            Context context = tomcat.addContext("",null);

            WebApplicationContext webApplicationContext = createWebApplicationContext();
            DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);

            Tomcat.addServlet(context,"dispatcher",dispatcherServlet).setLoadOnStartup(1);
            context.addServletMappingDecoded("/*","dispatcher");



            tomcat.start();
            tomcat.getServer().await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static WebApplicationContext createWebApplicationContext(){
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation("classpath:applicationContext.xml");
        context.refresh();
        return context;
    }
}