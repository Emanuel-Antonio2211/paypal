import 'package:flutter/services.dart';

class Paypal{
  final channel = MethodChannel("paypal");

  List resultados=List();
  List infoDato = List();
  String _result;
  String clientId = "AXIAUMr5Wvw1ArUNzexxNr_BzxecGVZhCDkKe66lXqAhw4CHRMIyr6oTXsdSiBWgcZjFFWWVlBpZJkKI";

  String result = '';

  List info = List();
  List datos = List();


  Paypal(){
    channel.setMethodCallHandler((MethodCall call)async{
      switch(call.method){
        case "payment":
        break;
        
      }
    });
  }

  Future<void> payment()async{
    await channel.invokeMethod('payment',{
      "precio": 10.0,
      "descripcion": "Este es una prueba",
      "lista": datos
      //"clientid": clientId
    });
  }
  Future<List> enlistar()async{


   infoDato = await channel.invokeListMethod('listaresult');
    print(infoDato);
   return infoDato;
  }
}