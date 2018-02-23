package netty.protocol.stack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    // 定义了重复登录保护和IP认证白名单列表，主要用于提升握手的可靠性
    private String[] whiteList = {"127.0.0.1", "192.168.1.104"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手请求消息，处理，其他消息透传
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            // 重复登录，拒绝，防止由于客户端重复登录导致的句柄泄漏
            if(nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else{
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whiteList) {
                    if(WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
                // 校验通过，握手成功
                if(isOK) {
                    nodeCheck.put(nodeIndex, true);
                }
            }
            System.out.println("The login response is : " + loginResp + " body [" + loginResp.getBody() + "]");
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 删除缓存
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
