package com.example.android_server_mapviewerclient;

import android.os.Handler;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Marshals.MarshalDouble;

public class MapRequestHandler {
    private static final String NAMESPACE = "MapProvider"; // com.service.ServiceImpl
    private static final String URL = "http://192.168.0.32:8080/STM-MapProvider/MapProviderService?wsdl";
    private static final String METHOD_NAME = "getEncodedMap";
    private static final String SOAP_ACTION = "http://192.168.0.32:8080/STM-MapProvider/MapProviderService/getEncodedMap";

    private String webResponse = "";
    private Thread thread;

    public void SendRequest(IMapResponseListener listener, Double latMin, Double latMax, Double longMin, Double longMax ) {
        thread = new Thread() {
            public void run() {
                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    PropertyInfo propInfoArg0 = new PropertyInfo();
                    PropertyInfo propInfoArg1 = new PropertyInfo();
                    PropertyInfo propInfoArg2 = new PropertyInfo();
                    PropertyInfo propInfoArg3 = new PropertyInfo();
                    propInfoArg0.setName("latMin");
                    propInfoArg1.setName("latMax");
                    propInfoArg2.setName("longMin");
                    propInfoArg3.setName("longMax");
                    propInfoArg0.setType(Double.class);
                    propInfoArg1.setType(Double.class);
                    propInfoArg2.setType(Double.class);
                    propInfoArg3.setType(Double.class);
                    propInfoArg0.setValue(latMin);
                    propInfoArg1.setValue(latMax);
                    propInfoArg2.setValue(longMin);
                    propInfoArg3.setValue(longMax);
                    request.addProperty(propInfoArg0);
                    request.addProperty(propInfoArg1);
                    request.addProperty(propInfoArg2);
                    request.addProperty(propInfoArg3);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);
                    MarshalDouble md = new MarshalDouble();
                    md.register(envelope);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapObject objectResult = (SoapObject) envelope.bodyIn;
                    webResponse = objectResult.toString();

                    int startIndex = webResponse.indexOf("=") + 1;
                    int endIndex = webResponse.lastIndexOf("=") + 1;
                    String formattedResponse = webResponse.substring(startIndex, endIndex);

                    listener.OnMapProvided(formattedResponse);

                } catch (SoapFault sp) {
                    sp.getMessage();
                    System.out.println("error = " + sp.getMessage());
                } catch (Exception e) {
                    System.out.println("problem8");
                    e.printStackTrace();
                    webResponse = "Connection/Internet problem";
                }
            }
        };

        thread.start();
    }
}
