package com.shah.soap.securewithcertclient;

import com.shah.soap.securewithcertclient.config.UTPasswordCallbackHandler;
import com.shah.soap.securewithcertservice.service.CalculateSumRequest;
import com.shah.soap.securewithcertservice.service.CalculateSumResponse;
import com.shah.soap.securewithcertservice.service.CalculatorService;
import com.shah.soap.securewithcertservice.service.impl.CalculatorServiceImplService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.wss4j.dom.WSConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CalculatorServiceTestClient  {

    public static void main(String[] args) {
        try {
            CalculatorServiceImplService calculatorService = new CalculatorServiceImplService(new URL("http://localhost:8080/secure/calculatorservice?wsdl"));
            CalculatorService calculatorServiceImplPort = calculatorService.getCalculatorServiceImplPort();
            Client client = ClientProxy.getClient(calculatorServiceImplPort);
            Endpoint endpoint = client.getEndpoint();

            Map<String, Object> outProps= new HashMap<>();
            outProps.put(ConfigurationConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
            outProps.put(ConfigurationConstants.ACTION, ConfigurationConstants.USERNAME_TOKEN +" Encrypt "+ConfigurationConstants.SIGNATURE +" Timestamp");
            outProps.put(ConfigurationConstants.USER, "cxf");
            outProps.put(ConfigurationConstants.PW_CALLBACK_CLASS, UTPasswordCallbackHandler.class.getCanonicalName());
            outProps.put(ConfigurationConstants.ENCRYPTION_USER, "myservicekey");
            outProps.put(ConfigurationConstants.ENC_PROP_FILE, "etc/clientkeystore.properties");

            outProps.put(ConfigurationConstants.SIGNATURE_USER, "myclientkey");
            outProps.put(ConfigurationConstants.SIG_PROP_FILE,"etc/clientkeystore.properties" );
            WSS4JOutInterceptor wss4JOutInterceptor = new WSS4JOutInterceptor(outProps);
            endpoint.getOutInterceptors().add(wss4JOutInterceptor);


            Map<String, Object> inProps= new HashMap<>();
            inProps.put(ConfigurationConstants.ACTION, "Encrypt Signature Timestamp");
            inProps.put(ConfigurationConstants.PW_CALLBACK_CLASS, UTPasswordCallbackHandler.class.getCanonicalName());
            inProps.put(ConfigurationConstants.DEC_PROP_FILE, "etc/clientkeystore.properties");
            inProps.put(ConfigurationConstants.SIG_PROP_FILE,"etc/clientkeystore.properties" );
            WSS4JInInterceptor wss4JInInterceptor = new WSS4JInInterceptor(inProps);
            endpoint.getInInterceptors().add(wss4JInInterceptor);

            CalculateSumRequest calculateSumRequest = new CalculateSumRequest();
            calculateSumRequest.setNumber1(10);
            calculateSumRequest.setNumber2(40);
            CalculateSumResponse sum = calculatorServiceImplPort.sum(calculateSumRequest);
            System.out.println("response = "+sum.getResponse());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


}
