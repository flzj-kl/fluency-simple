package top.flzjkl.fluency.simple.jmeter.rpc.remoting.handler;


import lombok.extern.slf4j.Slf4j;

import top.flzjkl.fluency.simple.jmeter.rpc.common.exception.RpcException;
import top.flzjkl.fluency.simple.jmeter.rpc.common.factory.SingletonFactory;
import top.flzjkl.fluency.simple.jmeter.rpc.provider.ServiceProvider;
import top.flzjkl.fluency.simple.jmeter.rpc.provider.impl.RedisProviderImpl;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcRequest;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(RedisProviderImpl.class);
    }

    /**
     * Processing rpcRequest: call the corresponding method, and then return the method
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * get method execution results
     *
     * @param rpcRequest client request
     * @param service    service object
     * @return the result of the target method execution
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            // log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
