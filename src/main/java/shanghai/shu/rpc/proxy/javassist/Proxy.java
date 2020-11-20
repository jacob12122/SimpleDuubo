package shanghai.shu.rpc.proxy.javassist;

import javassist.*;
import javassist.bytecode.AccessFlag;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.atomic.AtomicInteger;

public class Proxy {
    /**
     * 生成的代理类名前缀
     */
    private static final String PROXY_CLASSNAME_PREFIX="$Proxy";
    /**
     * 类后缀数字生成器
     */
    private static final AtomicInteger SUFFIX_GENERATOR=new AtomicInteger();
    public static Object newProxyInstance(ClassLoader classLoader, Class<?> targetClass, InvocationHandler invocationHandler) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        //创建代理类
        String quailfiedName = targetClass.getName() + PROXY_CLASSNAME_PREFIX + SUFFIX_GENERATOR.getAndIncrement();
        CtClass proxyCc = pool.makeClass(quailfiedName);
        //给代理类添加字段
        CtClass handlerCc = pool.get(InvocationHandler.class.getName());
        CtField handlerField = new CtField(handlerCc, "h", proxyCc);
        handlerField.setModifiers(AccessFlag.PRIVATE);
        proxyCc.addField(handlerField);
        //添加构造函数
        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{handlerCc}, proxyCc);
        ctConstructor.setBody("$0.h=$1;");
        proxyCc.addConstructor(ctConstructor);
        //为代理类添加相应接口方法及实现
        CtClass interfaceCc = pool.get(targetClass.getName());
        proxyCc.addInterface(interfaceCc);
        //为代理类添加相应方法及实现
        CtMethod[] ctMethods = interfaceCc.getDeclaredMethods();
        for (int i = 0; i < ctMethods.length; i++) {
            String methodFieldName="m"+i;
            String classParamsStr="new Class[0]";
            if (ctMethods[i].getParameterTypes().length>0){
                for (CtClass clazz : ctMethods[i].getParameterTypes()) {
                    classParamsStr = ((classParamsStr == "new Class[0]") ? clazz.getName() : classParamsStr + "," + clazz.getName()) + ".class";
                }
                classParamsStr = "new Class[] {" + classParamsStr + "}";
            }
            String methodFieldTpl = "private static java.lang.reflect.Method %s=Class.forName(\"%s\").getDeclaredMethod(\"%s\",%s);";
            String methodFieldBody = String.format(methodFieldTpl, "m" + i, targetClass.getName(), ctMethods[i].getName(), classParamsStr);
            // 为代理类添加反射方法字段
            CtField methodField = CtField.make(methodFieldBody, proxyCc);
            proxyCc.addField(methodField);
            String methodBody = "$0.h.invoke($0," + methodFieldName + ",$args)";
            // 如果有返回类型，则需要转换为相应类型后返回
            if (CtPrimitiveType.voidType != ctMethods[i].getReturnType()) {
                if (ctMethods[i].getReturnType() instanceof CtPrimitiveType) {
                    CtPrimitiveType ctPrimitiveType = (CtPrimitiveType) ctMethods[i].getReturnType();
                    methodBody = "return ((" + ctPrimitiveType.getWrapperName() + ") " + methodBody + ")." + ctPrimitiveType.getGetMethodName() + "()";
                } else { // 对于非基本类型直接转型即可
                    methodBody = "return (" + ctMethods[i].getReturnType().getName() + ") " + methodBody;
                }
            }
            methodBody += ";";
            /* 为代理类添加方法 */
            CtMethod newMethod = new CtMethod(ctMethods[i].getReturnType(), ctMethods[i].getName(),
                    ctMethods[i].getParameterTypes(), proxyCc);
            newMethod.setBody(methodBody);
            proxyCc.addMethod(newMethod);

        }
        Object proxy = proxyCc.toClass(classLoader, null).getConstructor(InvocationHandler.class).newInstance(invocationHandler);

        return proxy;

    }
}
