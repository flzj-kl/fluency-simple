package top.flzjkl.fluency.simple.registry;


import top.flzjkl.fluency.simple.common.extension.SPI;
import top.flzjkl.fluency.simple.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;


@SPI
public interface ServiceDiscovery {

    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
