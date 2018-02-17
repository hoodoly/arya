package com.yoku.arya;

import lombok.Data;

/**
 * @author HODO
 */
@Data
public class RpcResponse {

    private String requestId;
    private Exception exception;
    private Object object;
}
