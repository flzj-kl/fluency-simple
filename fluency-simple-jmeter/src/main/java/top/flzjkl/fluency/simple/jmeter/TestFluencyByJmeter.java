package top.flzjkl.fluency.simple.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;


import top.flzjkl.fluency.api.Hello;
import top.flzjkl.fluency.api.HelloService;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcRequest;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcResponse;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.transport.RpcRequestTransport;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.transport.netty.client.NettyRpcClient;



import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TestFluencyByJmeter extends AbstractJavaSamplerClient {

    private RpcRequest rpcRequest;

    private RpcResponse rpcResponse;
    private RpcRequestTransport rpcRequestTransport;

    // 填参数用的
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("ip","127.0.0.1");
        arguments.addArgument("port","3939");
        arguments.addArgument("group","test1");
        arguments.addArgument("version","version1");
        arguments.addArgument("interfaceName","hello");
        return arguments;
    }

    // 初始化方法，用于初始化性能测试时的每个线程
    @Override
    public void setupTest(JavaSamplerContext context) {
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
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        super.setupTest(context);
    }
    // 性能测试时的线程运行体
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        result.sampleStart();
        try {
            CompletableFuture<RpcResponse<Object>> completableFuture =
                    (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
            if (rpcResponse.getCode() != 200) {
                result.setSuccessful(false);
                result.setResponseCode("error code");
                return result;
            } else {
                result.setResponseCode(rpcResponse.getMessage());
                result.setSuccessful(true);
            }
        } catch (Exception e) {
            result.setSuccessful(false);
            e.printStackTrace();
        }  finally {
            result.sampleEnd();
        }
        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }


}
