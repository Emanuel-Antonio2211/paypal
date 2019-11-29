package com.example.paypal;

import android.app.Activity;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugins.GeneratedPluginRegistrant;

import android.content.Intent;
import android.view.View;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FlutterActivity {
  private static final String CHANNEL = "paypal";
    private MethodChannel.Result result;

  PayPalConfiguration m_configuration;
  //String m_paypalClientId = "TuClientIdPaypal"; //tener una cuenta de paypal developer
  Intent m_service;
  String tipoMoneda = "USD";
  Double precio;
  String descripcionCompra;
  int m_paypalRequestCode = 999;
  List datosResultantes= new ArrayList();
  List datos = new ArrayList();
  
String estado;
String idTransaccion;
String idPago;
String fechaPago;

String clientId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    m_configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) //sandbox para test, production para real
            .clientId(clientId);

    m_service = new Intent(this, PayPalService.class);
    m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration); //configuration above
      startService(m_service); //paypal service, listening to calls to paypal app
MethodChannel channel = new MethodChannel(getFlutterView(),"paypal");
channel.setMethodCallHandler((call, result) -> { //call obtiene los parámetros y que metodos se están ejecutando
    // desde el código dart,
    // result para enviar datos resultantes
    switch (call.method){
        case "payment":
            Double precio = call.argument("precio");
            String descripcion = call.argument("descripcion");
            datos = call.argument("lista");
            clientId = call.argument("clientid");
            payment(precio,descripcion);
           /* List resultado = payment(precio,descripcion);
            if(resultado != null){
                result.success(resultado);//Aquí se envía un dato válido

            }else{
                result.error("No válido","Dato nulo",null);
            }*/

        break;

        case "listaresult":

            result.success(listaresult(datos));
        break;

        default:
            result.notImplemented();
    }} );

  }


public void payment(Double valor, String descripcion){
    precio = valor;
    descripcionCompra = descripcion;

    //String descripcionCompra = "Es una compra";
    PayPalPayment payment = new PayPalPayment(new BigDecimal(precio), tipoMoneda, descripcionCompra,
            PayPalPayment.PAYMENT_INTENT_SALE);
    Intent intent = new Intent(this,PaymentActivity.class); // its not paypalpayment , its paymentactivity
    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
    intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
    startActivityForResult(intent,m_paypalRequestCode);

    //return payment.toString();


    System.out.println(datos);

   // return datos;
}

public List listaresult(List info){
      return info;
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data){
  //  super.onActivityResult(requestCode,resultCode,data);

    //MethodCall methodCall;
    //String estado;
   // String idTransaccion;
   // String idPago;
    //String fechaPago;
    // String estado = methodCall.argument("state");
    if(requestCode == m_paypalRequestCode){
        if(resultCode == Activity.RESULT_OK && data != null){
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            
            idTransaccion = confirmation.getProofOfPayment().getTransactionId();
            idPago = confirmation.getProofOfPayment().getPaymentId();
            fechaPago = confirmation.getProofOfPayment().getCreateTime();
            
            if(confirmation != null){
                estado = confirmation.getProofOfPayment().getState();
                if(estado.equals("approved")){
                    
                   // datosResultantes[0] = idPago;
                    datosResultantes.add(idPago);
                    //datosResultantes[1] = idTransaccion;
                    datosResultantes.add(idTransaccion);
                    //datosResultantes[2] = precio.toString();
                    datosResultantes.add(precio.toString());
                    //datosResultantes[3] = tipoMoneda;
                    datosResultantes.add(tipoMoneda);
                    //datosResultantes[4] = descripcionCompra;
                    datosResultantes.add(descripcionCompra);
                    //datosResultantes[5] = estado;
                    datosResultantes.add(estado);
                    //datosResultantes[6] = fechaPago;
                    datosResultantes.add(fechaPago);
                    for(int i = 0; i<listaresult(datosResultantes).size(); i++){
                        System.out.println(i + ":" + listaresult(datosResultantes).get(i));

                        datos = listaresult(datosResultantes);

                    }

                   // listaDatos(datosResultantes);

                    System.out.println(datos);



                }else{
                    System.out.println(estado);
                }
            }else{
                System.out.println("Confirmación es nulo");
            }


        }
    }
}

}
