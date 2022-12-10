package top.flzjkl.fluency.simple.jmeter;

import top.flzjkl.fluency.api.Hello;
import top.flzjkl.fluency.api.HelloService;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcRequest;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcResponse;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.transport.RpcRequestTransport;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.transport.netty.client.NettyRpcClient;


import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class QuickTest {
    private static RpcRequest rpcRequest;

    private static RpcResponse rpcResponse;
    private static RpcRequestTransport rpcRequestTransport;

    public static void main(String[] args) {
        rpcRequestTransport = new NettyRpcClient();
        try {
            Method method = HelloService.class.getMethod("hello", Hello.class);
            rpcRequest = RpcRequest.builder().methodName(method.getName())
                    .parameters(new Object[]{new Hello()})
                    .interfaceName(method.getDeclaringClass().getName())
                    .paramTypes(method.getParameterTypes())
                    .requestId(UUID.randomUUID().toString())
                    .group("test1")
                    .version("version1")
                    .build();
            CompletableFuture<RpcResponse<Object>> completableFuture =
                    (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(rpcResponse.getMessage());
    }
}
