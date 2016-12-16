package com.zkq.rpcclient;/*
 * Automatically generated by jrpcgen 1.1.3 on 16-12-16 ����10:24
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */

import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.XdrAble;
import org.acplt.oncrpc.XdrDecodingStream;
import org.acplt.oncrpc.XdrEncodingStream;

import java.io.IOException;

public class RpcBitmap implements XdrAble {

    public byte [] value;

    public RpcBitmap() {
    }

    public RpcBitmap(byte [] value) {
        this.value = value;
    }

    public RpcBitmap(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeByteVector(value);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        value = xdr.xdrDecodeByteVector();
    }

}
// End of RpcBitmap.java