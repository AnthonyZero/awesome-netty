package com.anthonyzero;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anthonyzero.entity.User;
import com.anthonyzero.serialize.JSONSerializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpHeaders headers;
    private HttpRequest request;
    private FullHttpRequest fullRequest;

    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        User user = new User();
        user.setUserName("anthonyzero");
        user.setCity("chongqing");
        user.setDate(new Date());

        if (httpObject instanceof HttpRequest) {
            request = (HttpRequest) httpObject;
            headers = request.headers();
            String uri = request.uri();
            System.out.println("http uri :" + uri);
            if (uri.equals(FAVICON_ICO)) {
                return; //图标
            }

            HttpMethod httpMethod = request.method();
            if (httpMethod.equals(HttpMethod.GET)) {
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
                Map<String, List<String>> uriAttributes = queryStringDecoder.parameters();
                //此处仅打印请求参数（你可以根据业务需求自定义处理）
                for (Map.Entry<String, List<String>> entry : uriAttributes.entrySet()) {
                    for (String attrVal : entry.getValue()) {
                        System.out.println(entry.getKey() + "=" + attrVal); //键值对
                    }
                }
                user.setMethod("get");
            } else if (httpMethod.equals(HttpMethod.POST)) {
                //POST请求,由于你需要从消息体中获取数据,因此有必要把msg转换成FullHttpRequest
                fullRequest = (FullHttpRequest)httpObject;
                //根据不同的Content_Type处理body数据
                dealWithContentType();
                user.setMethod("post");
            }

            JSONSerializer jsonSerializer = new JSONSerializer();
            byte[] content = jsonSerializer.serialize(user);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (!keepAlive) {
                channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                channelHandlerContext.write(response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 简单处理常用几种 Content-Type 的 POST 内容（可自行扩展）
     * @throws Exception
     */
    private void dealWithContentType() throws Exception{
        String contentType = getContentType();
        //可以使用HttpJsonDecoder
        if(contentType.equals("application/json")){
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            JSONObject obj = JSON.parseObject(jsonStr);
            for(Map.Entry<String, Object> item : obj.entrySet()){
                System.out.println(item.getKey()+"="+item.getValue().toString());
            }

        }else if(contentType.equals("application/x-www-form-urlencoded")){
            //方式一：使用 QueryStringDecoder
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            QueryStringDecoder queryDecoder = new QueryStringDecoder(jsonStr, false);
            Map<String, List<String>> uriAttributes = queryDecoder.parameters();
            for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    System.out.println(attr.getKey() + "=" + attrVal);
                }
            }

        }else if(contentType.equals("multipart/form-data")){
            //TODO 用于文件上传
        }else{
            //do nothing...
        }
    }
    private String getContentType(){
        String typeStr = headers.get("Content-Type").toString();
        String[] list = typeStr.split(";");
        return list[0];
    }
}
