package top.flzjkl.fluency;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.flzjkl.fluency.controller.HelloController;
import top.flzjkl.fluency.simple.annotation.RpcScan;


@RpcScan(basePackage = {"top.flzjkl.fluency.controller"})
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
