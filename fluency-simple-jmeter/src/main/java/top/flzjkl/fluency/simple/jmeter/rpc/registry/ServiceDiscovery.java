package top.flzjkl.fluency.simple.jmeter.rpc.registry;



import top.flzjkl.fluency.simple.jmeter.rpc.common.extension.SPI;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcRequest;


import java.net.InetSocketAddress;


@SPI
public interface ServiceDiscovery {

    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
