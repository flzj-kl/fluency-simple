package top.flzjkl.fluency.simple.registry;


import top.flzjkl.fluency.simple.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
