package top.flzjkl.fluency.simple.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import top.flzjkl.fluency.simple.annotation.RpcReference;
import top.flzjkl.fluency.simple.annotation.RpcService;
import top.flzjkl.fluency.simple.common.extension.ExtensionLoader;
import top.flzjkl.fluency.simple.common.factory.SingletonFactory;
import top.flzjkl.fluency.simple.config.RpcServiceConfig;
import top.flzjkl.fluency.simple.provider.ServiceProvider;
import top.flzjkl.fluency.simple.provider.impl.RedisProviderImpl;
import top.flzjkl.fluency.simple.proxy.RpcClientProxy;
import top.flzjkl.fluency.simple.remoting.transport.RpcRequestTransport;
import top.flzjkl.fluency.simple.remoting.transport.netty.client.NettyRpcClient;

import java.lang.reflect.Field;


@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {


    private final ServiceProvider serviceProvider;
    private final RpcRequestTransport rpcClient;

    public SpringBeanPostProcessor() {
         this.serviceProvider = SingletonFactory.getInstance(RedisProviderImpl.class);
         this.rpcClient = ExtensionLoader.getExtensionLoader(RpcRequestTransport.class).getExtension("netty");
        // this.rpcClient = new NettyRpcClient();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessBeforeInitialization");
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // get RpcService annotation
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            // build RpcServiceProperties
            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                    .group(rpcService.group())
                    .version(rpcService.version())
                    .service(bean).build();
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                        .group(rpcReference.group())
                        .version(rpcReference.version()).build();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }


}
