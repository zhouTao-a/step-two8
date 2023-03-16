package com.cloud.servlet;

import com.cloud.annotation.*;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


public class GPDispatcherServlet extends HttpServlet {

    //保存用户配置好的配置文件
    private Properties contextConfig = new Properties();

    //缓存从包路径下扫描的全类名
    private List<String> classNames = new ArrayList<String>();


    //保存所有扫描的类的实例
    private Map<String,Object> ioc = new HashMap<String,Object>();

    //保存Controller中URL和Method的对应关系
    private Map<String, Method> handlerMapping = new HashMap<String, Method>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {this.doPost(req,resp);}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+","/");

        if(!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 Not Found!!!");
            return;
        }

        Method method = this.handlerMapping.get(url);


        //1、先把形参的位置和参数名字建立映射关系，并且缓存下来
        Map<String,Integer> paramIndexMapping = new HashMap<String, Integer>();

        Annotation [][] pa = method.getParameterAnnotations();
        for (int i = 0; i < pa.length; i ++) {
            for (Annotation a : pa[i]) {
                if(a instanceof GPRequestParam){
                    String paramName = ((GPRequestParam) a).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName,i);
                    }
                }
            }
        }

        Class<?> [] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if(type == HttpServletRequest.class || type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }

        //2、根据参数位置匹配参数名字，从url中取到参数名字对应的值
        Object[] paramValues = new Object[paramTypes.length];

        //http://localhost/demo/query?name=Tom&name=Tomcat&name=Mic
        Map<String,String[]> params = req.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue())
                    .replaceAll("\\[|\\]","")
                    .replaceAll("\\s","");

            if(!paramIndexMapping.containsKey(param.getKey())){continue;}

            int index = paramIndexMapping.get(param.getKey());

            //涉及到类型强制转换
            paramValues[index] = value;
        }

        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }

        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        //3、组成动态实际参数列表，传给反射调用
        method.invoke(ioc.get(beanName),paramValues);
    }


    //init方法肯定干得的初始化的工作
    //inti首先我得初始化所有的相关的类，IOC容器、servletBean
    @Override
    public void init(ServletConfig config) throws ServletException {


        //1、获取配置文件中的路径地址
        doLoadConfig("application.properties");

        //2、根据路径地址扫描所有的.class文件放到List的中，需要完整路径供后续反射
        doScanner(contextConfig.getProperty("scanPackage"));

        //3、IoC 将类名和实例存到Map中
        doInstance();

        //4、DI 给类中的属性进行赋值
        doAutowired();

        //5、MVC 初始化HandlerMapping，建立URL和方法的实例的对应关系
        doInitHandlerMapping();

        System.out.print("Customer GP MVC Framework is init");
    }

    private void doInitHandlerMapping() {
        if(ioc.isEmpty()){ return; }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if(!clazz.isAnnotationPresent(GPController.class)){ continue; }


            String baseUrl = "";
            if(clazz.isAnnotationPresent(GPRequestMapping.class)){
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            //只迭代public方法
            for (Method method : clazz.getMethods()) {
                if(!method.isAnnotationPresent(GPRequestMapping.class)){ continue; }

                GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                //  //demo//query
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+","/");
                handlerMapping.put(url,method);
                System.out.println("Mapped : " + url + " --> " + method);

            }
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //忽略字段的修饰符，不管你是 private / protected / public / default
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                //根据注解判断
                if (!field.isAnnotationPresent(GPAutowired.class)) {
                    continue;
                }

                GPAutowired autowired = field.getAnnotation(GPAutowired.class);
                String beanName = autowired.value().trim();
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                }

                //反射
                field.setAccessible(true);

                try {
                    //相当于 demoAction.demoService = ioc.get("com.gupaoedu.demo.service.IDemoService");
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
    }

    /**
     * 3、IoC 将类名和实例存到Map中
     */
    @SneakyThrows
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //需要判断类型 不同注解的类的实例放到容器中
                //1、默认类名首字母小写
                String simpleName = toLowerFirstCase(clazz.getSimpleName());
                if (clazz.isAnnotationPresent(GPController.class)) {
                    ioc.put(simpleName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(GPService.class)) {
                    //如果在多个包下出现了相同的类名，优先是用别名（自定义命名）
                    GPService annotation = clazz.getAnnotation(GPService.class);
                    if (!"".equals(annotation.value())) {
                        simpleName = annotation.value();
                    }
                    ioc.put(simpleName, clazz.newInstance());


                    //如果是接口,只能初始化的它的实现类
                    /**
                     * 当Java虚拟机初始化一个类时，要求它的所有父类都已经初始化，但是这条规则不适于接口
                     * 1) 当初始化一个类时，并不会先初始化它所实现的类的接口。
                     * 2) 在初始化一个接口时，并不会先初始化它的父接口
                     * 因此，一个父接口并不会因为它的子接口或者实现类的初始化而初始化。只有当程序首次使用特定接口的镜头变量时，才会导致该接口的初始化。
                     */
                    for (Class<?> i : clazz.getInterfaces()) {
                        if(ioc.containsKey(i.getName())){
                            throw new RuntimeException("The " + i.getName() + " is exists,please use alies!!");
                        }
                        ioc.put(i.getName(), clazz.newInstance());
                    }

                }else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * 根据路径地址扫描所有的.class文件放到List的中，需要完整路径供后续反射
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        //读取当前目录下的所有目录及文件

        //将目录中的 . 替换成 /
        String s = scanPackage.replaceAll("\\.", "/");
        URL resource = this.getClass().getClassLoader().getResource("/" + s);
        File files = new File(resource.getFile());

        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                //为文件夹时，循环调用
                doScanner(scanPackage + "." + file.getName());
            }else {
                //只读取.class 结尾的文件
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                classNames.add(scanPackage + "." + file.getName().replace(".class", ""));
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        //读取配置文件
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;     //利用了ASCII码，大写字母和小写相差32这个规律
        return String.valueOf(chars);
    }

}