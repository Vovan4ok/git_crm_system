package org.volodymyrzganiaiko.gym.crm.system;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.volodymyrzganiaiko.gym.crm.system.config.AppConfig;
import org.volodymyrzganiaiko.gym.crm.system.config.WebConfig;
import org.volodymyrzganiaiko.gym.crm.system.filter.TransactionIdFilter;

import javax.servlet.Filter;
import java.util.Locale;

public class WebApplication {
    public static void main(String[] args) throws LifecycleException {
        Locale.setDefault(Locale.ENGLISH);
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class, AppConfig.class);
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("localhost");
        tomcat.getConnector();
        Context tomcatContext = tomcat.addContext("", null);
        tomcatContext.setParentClassLoader(Thread.currentThread().getContextClassLoader());
        Tomcat.addServlet(tomcatContext, "dispatcher", new DispatcherServlet(context)).setLoadOnStartup(1);
        tomcatContext.addServletMappingDecoded("/",  "dispatcher");
        Filter filter = new TransactionIdFilter();
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("TransactionIdFilter");
        filterDef.setFilter(filter);
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("TransactionIdFilter");
        filterMap.addURLPattern("/*");
        tomcatContext.addFilterDef(filterDef);
        tomcatContext.addFilterMap(filterMap);
        tomcat.start();
        tomcat.getServer().await();
    }
}
