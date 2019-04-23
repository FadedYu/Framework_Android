package com.bonait.bnframework.common.http.callback.json;

import android.text.TextUtils;

import com.bonait.bnframework.common.http.GsonConvert;
import com.bonait.bnframework.common.http.exception.TokenException;
import com.bonait.bnframework.common.model.BaseCodeJson;
import com.bonait.bnframework.common.model.BaseJson;
import com.bonait.bnframework.common.model.SimpleBaseJson;
import com.bonait.bnframework.common.model.SimpleCodeJson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by LY on 2019/4/2.
 */
public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }


    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现。
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 不同的业务，这里的代码逻辑都不一样，要按需修改

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback


        /*
        * 由于项目需要，App端请求网络必需添加一个Token验证，所以登录成功之后会在OkGo全局添加一个Token的param
        * 每次请求网络后台都会验证这个token是否过期，如果过期，则返回{"code":102,"msg":"token过期"}
        * */
        checkIfTheTokenHasExpired(response);

        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    /**
     * 检查Token是否过期，如果过期抛出TokenException异常并返回登录界面。
     *
     * 由于写这个方法时，出现了response.body()只调用一次就会close掉，导致后面方法无法继续获取response而发生异常
     * 下面是一种解决方案，在读取buffer之前，先对buffer进行clone一下。这时候就可以拿到返回的数据，然后就可以继续调用response.body()。
     * */
    private void checkIfTheTokenHasExpired(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return ;

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
        buffer.close();

        JSONObject jsonObject = new JSONObject(responseBodyString);
        if (jsonObject.optInt("code") == 102) {
            throw new TokenException("token已过期");
        }
    }


    /**
     * 根据class解析json数据
     * */
    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
            T t = GsonConvert.fromJson(jsonReader, rawType);
            response.close();
            return t;
        }
    }

    /**
     * 根据Type，解析json数据
     * */
    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = GsonConvert.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型的实际类型
        Type rawType = type.getRawType();
        // 泛型的参数
        Type typeArgument = type.getActualTypeArguments()[0];

        if (rawType == BaseJson.class) {
            // success json格式
            return convertBaseJson(response,type,jsonReader,typeArgument);
        } else if (rawType == BaseCodeJson.class) {
            // code json格式
            return convertBaseCodeJson(response,type,jsonReader,typeArgument);
        } else {
            // 其他json格式
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = GsonConvert.fromJson(jsonReader, type);
            response.close();
            return t;
        }
    }

    /**
     * 解析BaseLoginJson与其属性为泛型的参数
     * 根据BaseLoginJson解析出来的数据，实现不同的业务逻辑
     * 要点：code : 0/101/104   根据code实现不同的业务逻辑
     *
     * */
    private T convertBaseCodeJson(Response response,ParameterizedType type,JsonReader jsonReader,Type typeArgument) {

        //code返回的数据,0：表示成功，其他失败的有：101,102,900
        int code;
        // msg信息
        String msg;

        if (typeArgument == Void.class) {
            // 泛型格式如下： new JsonCallback<BaseCodeJson<Void>>(this)
            SimpleCodeJson simpleCodeJson = GsonConvert.fromJson(jsonReader,SimpleCodeJson.class);
            response.close();

            code = simpleCodeJson.getCode();
            msg = simpleCodeJson.getMsg();

            // code为0: 表示成功
            if (code == 0) {
                //noinspection unchecked
                return (T) simpleCodeJson.toBaseCodeJson();
            }
        } else {
            // 泛型格式如下： new JsonCallback<BaseCodeJson<内层JavaBean>>(this)
            BaseCodeJson baseCodeJson = GsonConvert.fromJson(jsonReader,type);
            response.close();

            code = baseCodeJson.getCode();
            msg = baseCodeJson.getMsg();

            // code为0: 表示成功
            if (code == 0) {
                //noinspection unchecked
                return (T) baseCodeJson;
            }
        }

        if (TextUtils.isEmpty(msg)) {
            msg = "未知错误，请稍后重试！";
        }

        // 当code返回不是0时，表示错误数据，根据数据返回不同的错误信息
        if (code == 101) {
            throw new IllegalStateException(msg+"");
        } else if (code == 102) {
            throw new TokenException(msg+"");
        } else if (code == 900) {
            throw new IllegalStateException(msg+"");
        } else {
            throw new IllegalStateException(msg+"");
        }
    }

    /**
     * 解析BaseJson与其属性为泛型的参数
     * 根据BaseJson解析出来的数据，实现不同的业务逻辑
     * 要点：success : true/false 根据success实现不同的业务逻辑
     *
     * */
    private T convertBaseJson(Response response,ParameterizedType type,JsonReader jsonReader,Type typeArgument) {

        // 一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
        // success为true则表示成功，false表示失败
        boolean success;
        // msg信息
        String msg;

        if (typeArgument == Void.class) {
            // 泛型格式如下： new JsonCallback<BaseJson<Void>>(this)
            SimpleBaseJson simpleBaseJson = GsonConvert.fromJson(jsonReader, SimpleBaseJson.class);
            response.close();
            success = simpleBaseJson.isSuccess();
            msg = simpleBaseJson.getMsg();
            // success为true时，表示成功
            if (success) {
                //noinspection unchecked
                return (T) simpleBaseJson.toBaseJson();
            }

        } else {
            // 泛型格式如下： new JsonCallback<BaseJson<内层JavaBean>>(this)
            BaseJson baseJson = GsonConvert.fromJson(jsonReader, type);
            response.close();
            success = baseJson.isSuccess();
            msg = baseJson.getMsg();
            // success为true时，表示成功
            if (success) {
                //noinspection unchecked
                return (T) baseJson;
            }
        }

        // 当success为false时，会走以下方法，显示不同的错误信息
        if (msg != null) {
            throw new IllegalStateException(msg+"");
        } else {
            throw new IllegalStateException("请求服务器失败，请稍后重试！");
        }
    }
}
