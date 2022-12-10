package top.flzjkl.fluency.simple.jmeter.rpc.remoting.transport;



import top.flzjkl.fluency.simple.jmeter.rpc.common.extension.SPI;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcRequest;


/**
 * send RpcRequest。
 *
 * @author shuang.kou
 * @createTime 2020年05月29日 13:26:00
 */
@SPI
public interface RpcRequestTransport {
    /**
     * send rpc request to server and get result
     *
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
