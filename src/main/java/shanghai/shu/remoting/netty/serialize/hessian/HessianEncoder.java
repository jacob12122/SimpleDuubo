package shanghai.shu.remoting.netty.serialize.hessian;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import shanghai.shu.common.serialize.hessian.HessianCodecUtil;

/**
 * Hessian编码器
 */
public class HessianEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(final ChannelHandlerContext ctx,final Object msg,final ByteBuf out) throws Exception {
        byte[] body = HessianCodecUtil.encode(msg);
        int dataLength = body.length;
        out.writeInt(dataLength);
        out.writeBytes(body);
    }
}
