package top.flzjkl.fluency.simple.provider;


import top.flzjkl.fluency.simple.common.extension.SPI;
import top.flzjkl.fluency.simple.config.RpcServiceConfig;


public interface ServiceProvider {


    void addService(RpcServiceConfig rpcServiceConfig);


    Object getService(String rpcServiceName);


    void publishService(RpcServiceConfig rpcServiceConfig);

}
