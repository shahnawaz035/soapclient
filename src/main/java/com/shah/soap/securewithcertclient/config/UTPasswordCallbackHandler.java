package com.shah.soap.securewithcertclient.config;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UTPasswordCallbackHandler implements CallbackHandler {

    private final Map<String, String> passwords = new HashMap<>();

    public UTPasswordCallbackHandler() {
        passwords.put("shah", "shah");
        passwords.put("cxf", "shah");
        passwords.put("myservicekey", "password1");
        passwords.put("myclientkey", "password1");
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback: callbacks) {
            WSPasswordCallback passwordCallback = (WSPasswordCallback) callback;
            String password = passwords.get(passwordCallback.getIdentifier());
            if(password!=null){
                passwordCallback.setPassword(password);
            }
        }
    }
}
