package com.yoku.arya.netty;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Rpc 请求
 * @author HODO
 */
@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {

    private String requestId;
    private Class aClass;
    private Method method;
    private Object[] params;
}
