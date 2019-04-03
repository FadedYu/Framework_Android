package com.bonait.bnframework.common.http;

import android.text.TextUtils;

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

import okhttp3.Response;
import okhttp3.ResponseBody;

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
            T t = GsonUtils.fromJson(jsonReader, rawType);
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
        T t = GsonUtils.fromJson(jsonReader, type);
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
            T t = GsonUtils.fromJson(jsonReader, type);
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
        if (typeArgument == Void.class) {
            // 泛型格式如下： new JsonCallback<BaseCodeJson<Void>>(this)
            SimpleCodeJson simpleCodeJson = GsonUtils.fromJson(jsonReader,SimpleCodeJson.class);
            response.close();
            //noinspection unchecked
            return (T) simpleCodeJson.toBaseCodeJson();
        } else {
            // 泛型格式如下： new JsonCallback<BaseCodeJson<内层JavaBean>>(this)
            BaseCodeJson baseCodeJson = GsonUtils.fromJson(jsonReader,type);
            response.close();

            int code = baseCodeJson.getCode();
            String msg = baseCodeJson.getMsg();
            if (TextUtils.isEmpty(msg)) {
                msg = "未知错误，请稍后重试！";
            }

            // 根据code返回不同的数据
            // 0: 表示成功
            if (code == 0) {
                //noinspection unchecked
                return (T) baseCodeJson;
            } else if (code == 101) {
                throw new IllegalStateException(msg+"");
            } else if (code == 102) {
                throw new TokenException(msg+"");
            } else if (code == 900) {
                throw new IllegalStateException(msg+"");
            } else {
                throw new IllegalStateException(msg+"");
            }
        }
    }

    /**
     * 解析BaseJson与其属性为泛型的参数
     * 根据BaseJson解析出来的数据，实现不同的业务逻辑
     * 要点：success : true/false 根据success实现不同的业务逻辑
     *
     * */
    private T convertBaseJson(Response response,ParameterizedType type,JsonReader jsonReader,Type typeArgument) {
        if (typeArgument == Void.class) {
            // 泛型格式如下： new JsonCallback<BaseJson<Void>>(this)
            SimpleBaseJson simpleBaseJson = GsonUtils.fromJson(jsonReader, SimpleBaseJson.class);
            response.close();
            //noinspection unchecked
            return (T) simpleBaseJson.toBaseJson();

        } else {
            // 泛型格式如下： new JsonCallback<BaseJson<内层JavaBean>>(this)
            BaseJson baseJson = GsonUtils.fromJson(jsonReader, type);
            response.close();
            boolean success = baseJson.isSuccess();
            String msg = baseJson.getMsg();
            //这里的success是以下意思,success为true则表示成功，false表示失败
            //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
            if (success) {
                //noinspection unchecked
                return (T) baseJson;
            } else {
                if (msg != null) {
                    throw new IllegalStateException(baseJson.getMsg()+"");
                } else {
                    throw new IllegalStateException("请求服务器失败，请稍后重试！");
                }
            }

        }
    }
}
