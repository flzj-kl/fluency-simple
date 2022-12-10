
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.flzjkl.fluency.api.HelloService;
import top.flzjkl.fluency.server.HelloServiceImpl;
import top.flzjkl.fluency.simple.annotation.RpcScan;
import top.flzjkl.fluency.simple.config.RpcServiceConfig;
import top.flzjkl.fluency.simple.remoting.transport.netty.server.NettyRpcServer;


@RpcScan(basePackage = {"top.flzjkl.fluency.server"})
public class NettyServerMain {
    public static void main(String[] args) {
        // Register service via annotation
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        // Register service manually
        HelloService helloService = new HelloServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test1").version("version1").service(helloService).build();
        // nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
