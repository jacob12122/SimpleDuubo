package shanghai.shu.remoting.netty.serialize.hessian;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import shanghai.shu.common.serialize.hessian.HessianCodecUtil;
import shanghai.shu.remoting.netty.MessageCodecConstant;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hessian解码器
 */
public class HessianDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //消息头长度不对就直接返回
        if (in.readableBytes()<MessageCodecConstant.MESSAGE_LENGTH){
            return;
        }
        in.markReaderIndex();
        int messageLength = in.readInt();
        if (messageLength<0){
            ctx.close();
        }
        if (in.readableBytes()<messageLength){
            in.resetReaderIndex();
            return;
        }else{
            byte[] messageBody = new byte[messageLength];
            in.readBytes(messageBody);
            try {
                Object obj = HessianCodecUtil.decode(messageBody);
                out.add(obj);
            }catch (IOException ex){
                Logger.getLogger(HessianDecoder.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }
}
